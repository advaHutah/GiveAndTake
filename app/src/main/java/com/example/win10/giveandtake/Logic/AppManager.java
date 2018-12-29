package com.example.win10.giveandtake.Logic;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private FirebaseAuth mAuth;
    private Service selectedService;

    private AppManager() {
        firebaseManager = FirebaseManager.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    public static AppManager getInstance() {
        if (singletonAppManager == null)
            singletonAppManager = new AppManager();
        return singletonAppManager;
    }


    public void createNewUser(String uid, String email, String fullName, String phoneNumber, String gender) {
        User user = new User(uid, email, fullName, phoneNumber, gender, User.INIT_BALANCE);
        setCurrentUser(user);
        this.firebaseManager.addUserInfoToDB(user);
    }

    public void findMatch(final Request newRequest) {

        final Map<String, ArrayList<TagUserInfo>> match = new HashMap<>();//key = tag, value = arraylist of uid
        // for each tag in the new request tags find the  all users that match the tags..
        FirebaseManager.getInstance().getAllTagsFromDB(new FirebaseManager.FirebaseCallback<Map<String, ArrayList<TagUserInfo>>>() {
            @Override
            public void onDataArrived(Map<String, ArrayList<TagUserInfo>> value) {
                //value is a map: key = tag, value = another map (key = uid , value = request type)
                for (String tag : newRequest.getTags()) {
                    if (value.keySet().contains(tag)) {
                        ArrayList<TagUserInfo> filterdTagsUser = new ArrayList<>();
//                        HashMap<String,TagUserInfo> tagChildren = value.get(tag);
//                        get from map all the user with different type then newRequset type and save it in tagsUser
                        for (TagUserInfo tagUserInfo : value.get(tag)) {
                            if (!tagUserInfo.getUid().equals(newRequest.getUid()) && tagUserInfo.getRequestType() != newRequest.requestType) {
                                filterdTagsUser.add(tagUserInfo);
                            }
                        }
                        match.put(tag, filterdTagsUser);
                    }
                }

            }
        });
        newRequest.setMatch(match);
    }


    public void getTokenFromFCM() {
        //todo

    }

    public void getCurrentUserServices(User currentUser) {
        //TODO
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
        //find match
        //TODO find match
    }

    public void getRequestTags(Request.RequestType requestType, final AppManagerCallback<ArrayList<String>> callback) {
        this.firebaseManager.getTagsFromRequest(currentUser.getId(), requestType, new FirebaseManager.FirebaseCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                callback.onDataArrived(value);
            }
        });
    }

    public void setSelectedService(Service selectedService) {
        this.selectedService = selectedService;
    }

    public Service getSelectedService() {
        return selectedService;
    }

    public void serviceEnd(Service service, int minuts) {
        //send notification to other user
        //todo
        //update service minuts
        // service.setMinuts(minuts);
        //update service status
        // service.setStatus(Service.Status.COMPLETED);
        //update service in db
        firebaseManager.updateServiceInDB(service);
        //update users info in db
        //firebaseManager.updateUserServcieInDB(service.getTakeRequest().getUid(), service);
        //firebaseManager.updateUserServcieInDB(service.getGiveRequest().getUid(), service);
    }

    public void removeService(Service theService) {
        currentUser.getMyServices().remove(theService);
        firebaseManager.removeService(theService);
    }

    public void signOut(AppManagerCallback<Object> callback) {
        firebaseManager.signOut();
        callback.onDataArrived(null);
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
                    createNewUser(theAccount.getUid(), theAccount.getEmail(), theAccount.getDisplayName(), theAccount.getPhoneNumber(), "male");

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
                    firebaseManager.getGiveRequestFromDB(otherUser.getId(), new FirebaseManager.FirebaseCallback<Request>() {
                        @Override
                        public void onDataArrived(Request value) {
                              otherUser.setMyGiveRequests(value);
                        }
                    });
                    firebaseManager.getTakeRequestFromDB(otherUser.getId(), new FirebaseManager.FirebaseCallback<Request>() {
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
}
