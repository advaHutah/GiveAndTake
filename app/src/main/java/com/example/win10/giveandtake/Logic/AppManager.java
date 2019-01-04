package com.example.win10.giveandtake.Logic;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Set;

//Handles all app actions and DB read/write
public class AppManager {




    public interface AppManagerCallback<T> {
        void onDataArrived(T value);
    }


    private static AppManager singletonAppManager = null;
    private FirebaseManager firebaseManager;
    private User currentUser;
    private User otherUser;
    private ArrayList<TagUserInfo> notificationUsers;
    private FirebaseAuth mAuth;
    private Session selectedSession;

    private AppManager() {
        firebaseManager = FirebaseManager.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    public static AppManager getInstance() {
        if (singletonAppManager == null)
            singletonAppManager = new AppManager();
        return singletonAppManager;
    }


    public void createNewUser(String uid, String email, String fullName, String phoneNumber,String photoURL) {
        User user = new User(uid, email, fullName, phoneNumber, User.INIT_BALANCE,photoURL);
        setCurrentUser(user);
        this.firebaseManager.addUserInfoToDB(user);
    }

    public void updateUserPhoneNumer(String phoneNumber) {
        currentUser.setPhoneNumber(phoneNumber);
        firebaseManager.updateUserPhoneNumber(currentUser.getId(),phoneNumber);
    }
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void addRequestNotFinal(String text, Request.RequestType requestType) {
        //create new request with the relevant type
        Request newRequest = new Request(text, currentUser.getId(), currentUser.getFullName(), requestType);
        // add this request to the user
        this.currentUser.addRequest(newRequest);
        //add the request to firebase
        this.firebaseManager.addRequestToDB(newRequest);
        //find match
        // findMatch(newRequest);
    }

    public void addRequestFinal(Set<String> selectedTags, Request.RequestType requestType) {
        this.updateRequestTagsUserValidated(selectedTags, requestType);
    }

    public void getMyRequestTags(Request.RequestType requestType, final AppManagerCallback<ArrayList<String>>callback) {
        this.firebaseManager.getTagsFromRequest(currentUser.getId(), requestType, new FirebaseManager.FirebaseCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                callback.onDataArrived(value);
            }
        });
    }

    public void getMatchUsers(String tag, Request.RequestType requestType, final AppManagerCallback<ArrayList<TagUserInfo>>callback){
        if(currentUser!=null) {
            this.firebaseManager.getMatchUsers(currentUser.getId(), tag, requestType, new FirebaseManager.FirebaseCallback<ArrayList<TagUserInfo>>() {
                @Override
                public void onDataArrived(ArrayList<TagUserInfo> value) {
                    callback.onDataArrived(value);
                }
            });
        }
    }

    public void getTags( Request.RequestType requestType, final AppManagerCallback<ArrayList<String>>callback){
        this.firebaseManager.getTags(requestType, new FirebaseManager.FirebaseCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                callback.onDataArrived(value);
            }
        });
    }

    public void setSelectedSession(Session selectedSession) {
        this.selectedSession = selectedSession;
    }

    public void setSelectedSessionByID(String sessionId) {
        this.firebaseManager.getSessionFromDB(sessionId,new FirebaseManager.FirebaseCallback<Session>() {
            @Override
            public void onDataArrived(Session value) {
                if (value != null) {
                    selectedSession = value;
                    if(selectedSession.getInitiator().equals(Session.SessionInitiator.GIVER)){
                        firebaseManager.getUserDetailFromDB(selectedSession.getGiveRequest().getUid(), new FirebaseManager.FirebaseCallback<User>() {
                            @Override
                            public void onDataArrived(User value) {
                                otherUser = value;
                            }
                        });

                    }
                    else{
                        firebaseManager.getUserDetailFromDB(selectedSession.getTakeRequest().getUid(), new FirebaseManager.FirebaseCallback<User>() {
                            @Override
                            public void onDataArrived(User value) {
                                otherUser = value;
                            }
                        });
                    }
                }
            }
        });
    }
    public Session getSelectedSession() {
        return selectedSession;
    }

    public void serviceEnd(Session session, int minuts) {
        //send notification to other user
        //todo
        //update session minuts
       // session.setMinuts(minuts);
        //update session status
       // session.setStatus(Session.Status.COMPLETED);
        //update session in db
        firebaseManager.updateServiceInDB(session);
        //update users info in db
        //firebaseManager.updateUserServcieInDB(session.getTakeRequest().getUid(), session);
        //firebaseManager.updateUserServcieInDB(session.getGiveRequest().getUid(), session);
    }

    public void removeService(Session theSession) {
        currentUser.getMyServices().remove(theSession);
        firebaseManager.removeService(theSession);
    }

//    public void signOut(AppManagerCallback<Object>callback) {
//        firebaseManager.signOut();
//        callback.onDataArrived(null);
//    }
    public void signOut() {
           firebaseManager.signOut();
    }

    public void updateRequestTagsUserValidated(Set<String> selectedTags, Request.RequestType requestType) {
        Request myRequest;
        //update current user request
        if (requestType == Request.RequestType.TAKE)
            myRequest = currentUser.getMyTakeRequest();
        else
            myRequest = currentUser.getMyGiveRequest();

        myRequest.setTags(new ArrayList<String>(selectedTags));
        myRequest.setIsFinal(1);

        //update firebase
        firebaseManager.addRequestToDB(myRequest);
        firebaseManager.addUserInfoToDB(currentUser);

    }

    public void userLogedIn(FirebaseUser account, final AppManager.AppManagerCallback<Boolean> callback) {
        final FirebaseUser theAccount = account;
        //get user info from db - if exist get info else create new user
        firebaseManager.getUserDetailFromDB(account.getUid(), new FirebaseManager.FirebaseCallback<User>() {
            @Override
            public void onDataArrived(User value) {
                if (value != null)
                    AppManager.getInstance().setCurrentUser(value);
                else
                    createNewUser(theAccount.getUid(), theAccount.getEmail(), theAccount.getDisplayName(), theAccount.getPhoneNumber(), theAccount.getPhotoUrl().toString());

                callback.onDataArrived(true);
            }
        });
    }

    public void setOtherUser(String uid, final AppManager.AppManagerCallback<Boolean> callback) {
        firebaseManager.getUserDetailFromDB(uid, new FirebaseManager.FirebaseCallback<User>() {
            @Override
            public void onDataArrived(User value) {
                if (value != null) {
                    otherUser = value;
                    firebaseManager.getRequestFromDB(otherUser.getId(), Request.RequestType.GIVE, new FirebaseManager.FirebaseCallback<Request>() {
                        @Override
                        public void onDataArrived(Request value) {
                            otherUser.setMyGiveRequests(value);
                        }
                    });
                    firebaseManager.getRequestFromDB(otherUser.getId(), Request.RequestType.TAKE, new FirebaseManager.FirebaseCallback<Request>() {
                        @Override
                        public void onDataArrived(Request value) {
                            otherUser.setMyTakeRequest(value);
                        }
                    });

                }
                callback.onDataArrived(true);
            }
        });
    }

    public User getOtherUser() {
        return otherUser;
    }

    public ArrayList<TagUserInfo> getNotificationUsers() {
        return notificationUsers;
    }

    public void setNotificationUsers(ArrayList<TagUserInfo> notificationUsers) {
        this.notificationUsers = notificationUsers;
    }
}
