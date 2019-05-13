package com.finalproject.giveandtake.Logic;

import com.finalproject.giveandtake.DBLogic.FirebaseManager;
import com.finalproject.giveandtake.DBLogic.GiveAndTakeMessagingService;
import com.finalproject.giveandtake.Util.GeneralUtil;
import com.finalproject.giveandtake.Util.MyConstants;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import androidx.annotation.Nullable;

//Handles all app actions and DB read/write
public class AppManager {




    public interface AppManagerCallback<T> {
        void onDataArrived(T value);
    }


    private static AppManager singletonAppManager = null;
    private FirebaseManager firebaseManager;
    private com.finalproject.giveandtake.Logic.User currentUser;
    private com.finalproject.giveandtake.Logic.User otherUser;
    private ArrayList<com.finalproject.giveandtake.Logic.TagUserInfo> notificationUsers;
    private FirebaseAuth mAuth;
    private Session selectedSession;
    private GoogleSignInClient googleSignInClient;
    private ArrayList<String> myTakeRequestTags;
    private ArrayList<String> myGiveRequestTags;
    private GiveAndTakeMessagingService messagingService;

    private AppManager() {
        firebaseManager = FirebaseManager.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public static AppManager getInstance() {
        if (singletonAppManager == null)
            singletonAppManager = new AppManager();
        return singletonAppManager;
    }

    public void setMessagingService() {
        messagingService = GiveAndTakeMessagingService.getInstance();
    }

    public void createNewUser(String uid, String email, String fullName, String phoneNumber, String photoURL) {
        com.finalproject.giveandtake.Logic.User user = new com.finalproject.giveandtake.Logic.User(uid, email, fullName, phoneNumber, photoURL);
        setCurrentUser(user);
        this.firebaseManager.addUserInfoToDB(user);
    }

    public void updateUserPhoneNumber(String phoneNumber) {
        currentUser.setPhoneNumber(phoneNumber);
        firebaseManager.updateUserPhoneNumber(currentUser.getId(), phoneNumber);
    }

    @Nullable
    public com.finalproject.giveandtake.Logic.User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(com.finalproject.giveandtake.Logic.User user) {
        currentUser = user;
    }

    public void addRequestNotFinal(String text, com.finalproject.giveandtake.Logic.Request.RequestType requestType) {
        //create new request with the relevant type
        Request newRequest = new Request(text, currentUser.getId(), currentUser.getFullName(), requestType);
        // add this request to the user
        this.currentUser.addRequest(newRequest);
        //add the request to firebase
        this.firebaseManager.addRequestToDB(newRequest);
        //cloud function generates tags in DB
    }

    //after set the tags that will describe his request , update the request DB and change isFinal property to 1
    public void setFinalRequest(ArrayList<String> keyWords, ArrayList<String> stopWords, com.finalproject.giveandtake.Logic.Request.RequestType requestType) {
        this.updateRequestTagsUserValidated(keyWords, stopWords, requestType);
    }

    public void updateRequestTagsUserValidated(ArrayList<String> keyWords, ArrayList<String> stopWords, com.finalproject.giveandtake.Logic.Request.RequestType requestType) {
        Request myRequest;
        //update current user request
        if (requestType == com.finalproject.giveandtake.Logic.Request.RequestType.TAKE)
            myRequest = currentUser.getMyTakeRequest();
        else
            myRequest = currentUser.getMyGiveRequest();
        myRequest.setKeyWords(keyWords);
        myRequest.setStopWords(stopWords);

        //update firebase
        firebaseManager.addRequestToDB(myRequest);
        firebaseManager.addUserInfoToDB(currentUser);
    }

    //get the keywords
    public void getMyRequestKeyWords(com.finalproject.giveandtake.Logic.Request.RequestType requestType, final AppManagerCallback<ArrayList<String>> callback) {
        this.firebaseManager.getKeyWordsFromRequest(currentUser.getId(), requestType, new FirebaseManager.FirebaseCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                callback.onDataArrived(value);
            }
        });
    }


    //get the suggested tags
    public void getMyRequestSuggestedTags(final com.finalproject.giveandtake.Logic.Request.RequestType requestType, final AppManagerCallback<ArrayList<String>> callback) {
        this.firebaseManager.getSuggestedTagsFromRequest(currentUser.getId(), requestType, new FirebaseManager.FirebaseCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                callback.onDataArrived(value);
                //update current user request
                if (requestType == com.finalproject.giveandtake.Logic.Request.RequestType.TAKE)
                    getCurrentUser().getMyTakeRequest().setSuggestedTags(value);
                else
                    getCurrentUser().getMyGiveRequest().setSuggestedTags(value);
            }
        });
    }

    public void getMatchUsers(String tag, com.finalproject.giveandtake.Logic.Request.RequestType requestType, final AppManagerCallback<ArrayList<com.finalproject.giveandtake.Logic.TagUserInfo>> callback) {
        if (currentUser != null) {
            this.firebaseManager.getMatchUsers(currentUser.getId(), tag, requestType, new FirebaseManager.FirebaseCallback<ArrayList<com.finalproject.giveandtake.Logic.TagUserInfo>>() {
                @Override
                public void onDataArrived(ArrayList<com.finalproject.giveandtake.Logic.TagUserInfo> value) {
                    callback.onDataArrived(value);
                }
            });
        }
    }

    public void getSelectedTagUsers(String tag, com.finalproject.giveandtake.Logic.Request.RequestType requestType, final AppManagerCallback<ArrayList<com.finalproject.giveandtake.Logic.TagUserInfo>> callback) {
        String uid = "";
        if (currentUser != null)
            uid = currentUser.getId();
        this.firebaseManager.getMatchUsers(uid, tag, requestType, new FirebaseManager.FirebaseCallback<ArrayList<com.finalproject.giveandtake.Logic.TagUserInfo>>() {
            @Override
            public void onDataArrived(ArrayList<com.finalproject.giveandtake.Logic.TagUserInfo> value) {
                callback.onDataArrived(value);
            }
        });

    }

    //get all tags in DB based on request type
    public void getTags(Request.RequestType requestType, final AppManagerCallback<ArrayList<String>> callback) {
        this.firebaseManager.getTags(requestType, new FirebaseManager.FirebaseCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                callback.onDataArrived(value);
            }
        });
    }

    public void getTagsBulk(Request.RequestType requestType, String lastTagShowed, final AppManagerCallback<ArrayList<String>> callback) {
        this.firebaseManager.getTagsBulk(requestType, lastTagShowed, MyConstants.TAGS_BULK_COUNT, new FirebaseManager.FirebaseCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                callback.onDataArrived(value);
            }
        });
    }

    //get user match tags based on request type
    public void getMyTakeMatchTags(final AppManagerCallback<ArrayList<String>> callback) {
        Request.RequestType otherRequestType = com.finalproject.giveandtake.Logic.Request.RequestType.GIVE;
        if (currentUser != null && currentUser.getMyTakeRequest() != null && currentUser.getMyTakeRequest().getKeyWords() != null) {
            myTakeRequestTags = currentUser.getMyTakeRequest().getKeyWords();

            this.firebaseManager.getTags(otherRequestType, new FirebaseManager.FirebaseCallback<ArrayList<String>>() {
                @Override
                public void onDataArrived(ArrayList<String> value) {
                    ArrayList<String> matchTags = new ArrayList<>();
                    for (String myTag : myTakeRequestTags) {
                        if (value.contains(myTag)) {
                            matchTags.add(myTag);
                        }
                    }
                    callback.onDataArrived(matchTags);
                }
            });

        } else {
            callback.onDataArrived(new ArrayList<String>());
        }
    }

    public void getMyGiveMatchTags(final AppManagerCallback<ArrayList<String>> callback) {
        Request.RequestType otherRequestType = Request.RequestType.TAKE;
        if (currentUser != null && currentUser.getMyGiveRequest() != null && currentUser.getMyGiveRequest().getKeyWords() != null) {
            myGiveRequestTags = currentUser.getMyGiveRequest().getKeyWords();
            this.firebaseManager.getTags(otherRequestType, new FirebaseManager.FirebaseCallback<ArrayList<String>>() {
                @Override
                public void onDataArrived(ArrayList<String> value) {
                    ArrayList<String> matchTags = new ArrayList<>();
                    for (String myTag : myGiveRequestTags) {
                        if (value.contains(myTag)) {
                            matchTags.add(myTag);
                        }
                    }
                    callback.onDataArrived(matchTags);
                }
            });
        } else {
            callback.onDataArrived(new ArrayList<String>());
        }
    }

        public void setSelectedSession(Session selectedSession) {
        if (selectedSession.getInitiator().equals(Session.SessionInitiator.GIVER)) {
            firebaseManager.getUserDetailFromDB(selectedSession.getGiveRequest().getUid(), new FirebaseManager.FirebaseCallback<com.finalproject.giveandtake.Logic.User>() {
                @Override
                public void onDataArrived(com.finalproject.giveandtake.Logic.User value) {
                    otherUser = value;
                }
            });

        } else {
            firebaseManager.getUserDetailFromDB(selectedSession.getTakeRequest().getUid(), new FirebaseManager.FirebaseCallback<com.finalproject.giveandtake.Logic.User>() {
                @Override
                public void onDataArrived(com.finalproject.giveandtake.Logic.User value) {
                    otherUser = value;
                }
            });
        }
        this.selectedSession = selectedSession;
    }

    public void setSelectedSessionRestored(Session selectedSession) {
        if (selectedSession.getInitiator().equals(Session.SessionInitiator.GIVER)) {
            firebaseManager.getUserDetailFromDB(selectedSession.getTakeRequest().getUid(), new FirebaseManager.FirebaseCallback<com.finalproject.giveandtake.Logic.User>() {
                @Override
                public void onDataArrived(com.finalproject.giveandtake.Logic.User value) {
                    otherUser = value;
                }
            });

        } else {
            firebaseManager.getUserDetailFromDB(selectedSession.getGiveRequest().getUid(), new FirebaseManager.FirebaseCallback<com.finalproject.giveandtake.Logic.User>() {
                @Override
                public void onDataArrived(com.finalproject.giveandtake.Logic.User value) {
                    otherUser = value;
                }
            });
        }
        this.selectedSession = selectedSession;
    }

    public void setSelectedSessionByID(String sessionId) {
        this.firebaseManager.getSessionFromDB(sessionId, new FirebaseManager.FirebaseCallback<Session>() {
            @Override
            public void onDataArrived(Session value) {
                if (value != null) {
                    selectedSession = value;
                    if (selectedSession.getInitiator().equals(com.finalproject.giveandtake.Logic.Session.SessionInitiator.GIVER)) {
                        firebaseManager.getUserDetailFromDB(selectedSession.getGiveRequest().getUid(), new FirebaseManager.FirebaseCallback<com.finalproject.giveandtake.Logic.User>() {
                            @Override
                            public void onDataArrived(com.finalproject.giveandtake.Logic.User value) {
                                otherUser = value;
                            }
                        });

                    } else {
                        firebaseManager.getUserDetailFromDB(selectedSession.getTakeRequest().getUid(), new FirebaseManager.FirebaseCallback<com.finalproject.giveandtake.Logic.User>() {
                            @Override
                            public void onDataArrived(com.finalproject.giveandtake.Logic.User value) {
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


    public void checkForOpenSession(boolean isTake, final AppManagerCallback<Session> callback) {
        if (currentUser != null) {
            this.firebaseManager.checkForOpenSession(currentUser.getId(), isTake, new FirebaseManager.FirebaseCallback<Session>() {
                @Override
                public void onDataArrived(Session value) {
                    callback.onDataArrived(value);
                }
            });
        }
    }

    public void signOut() {
        firebaseManager.signOut();
        this.setCurrentUser(null);
    }

    public void userLogedIn(FirebaseUser account, final AppManager.AppManagerCallback<Boolean> callback) {
        final FirebaseUser theAccount = account;
        //get user info from db - if exist get info else create new user
        firebaseManager.getUserDetailFromDB(account.getUid(), new FirebaseManager.FirebaseCallback<com.finalproject.giveandtake.Logic.User>() {
            @Override
            public void onDataArrived(com.finalproject.giveandtake.Logic.User value) {
                if (value != null)
                    AppManager.getInstance().setCurrentUser(value);
                else
                    createNewUser(theAccount.getUid(), theAccount.getEmail(), theAccount.getDisplayName(), theAccount.getPhoneNumber(), theAccount.getPhotoUrl().toString());

                callback.onDataArrived(true);
            }
        });
    }


    public void setOtherUser(String uid, final AppManager.AppManagerCallback<Boolean> callback) {
        firebaseManager.getUserDetailFromDB(uid, new FirebaseManager.FirebaseCallback<com.finalproject.giveandtake.Logic.User>() {
            @Override
            public void onDataArrived(com.finalproject.giveandtake.Logic.User value) {
                if (value != null) {
                    otherUser = value;
                }
                callback.onDataArrived(true);
            }
        });
    }

    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
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

    public void saveSession() {
        if (selectedSession != null)
            firebaseManager.saveSession(selectedSession);
    }

    public void updateSessionStatus(Session.Status status) {
        selectedSession.setStatus(status);

        if (status == Session.Status.active) {
            refreshTimestampDelta(new AppManagerCallback<Boolean>() {
                @Override
                public void onDataArrived(Boolean value) {
                    selectedSession.setStartTimeStamp(GeneralUtil.now());
                    firebaseManager.saveSessionStartTimeStamp(selectedSession.getId(), GeneralUtil.now());
                }
            });

        }
        firebaseManager.updateSessionStatus(selectedSession.getId(), status);
    }

    public void sessionStatusChanged(final AppManagerCallback<com.finalproject.giveandtake.Logic.Session.Status> callback) {
        firebaseManager.sessionStatusChanged(selectedSession.getId(), new FirebaseManager.FirebaseCallback<com.finalproject.giveandtake.Logic.Session.Status>() {
            @Override
            public void onDataArrived(com.finalproject.giveandtake.Logic.Session.Status value) {
                callback.onDataArrived(value);
            }
        });
    }


    public void finishSession() {
        updateSessionStatus(Session.Status.terminated);
        refreshTimestampDelta(new AppManagerCallback<Boolean>() {
            @Override
            public void onDataArrived(Boolean value) {
                selectedSession.setEndTimeStamp(GeneralUtil.now());
                firebaseManager.updateSessionEndTimeStamp(selectedSession.getId(), GeneralUtil.now());
            }
        });
        updateMyBalance();


    }


    public void resetOtherUserAndSession() {
        otherUser = null;
        selectedSession = null;
    }

    public void getMySessionHistory(boolean isTake, final AppManagerCallback<ArrayList<Session>> callback) {
        if (currentUser != null) {
            this.firebaseManager.getHistoricalSessionsFromDB(currentUser.getId(), isTake, new FirebaseManager.FirebaseCallback<ArrayList<com.finalproject.giveandtake.Logic.Session>>() {
                @Override
                public void onDataArrived(ArrayList<Session> value) {
                    callback.onDataArrived(value);
                }
            });
        }
    }

    public void refreshTimestampDelta(final AppManagerCallback<Boolean> callback) {
        if (currentUser != null) {
            firebaseManager.refreshTimestampDelta(currentUser.getId(), new FirebaseManager.FirebaseCallback<Boolean>() {
                @Override
                public void onDataArrived(Boolean value) {
                    callback.onDataArrived(true);
                }
            });
        }
    }

    public void updateMyBalance() {
        firebaseManager.updateBalanceOnDB(currentUser.getId(), currentUser.getBalance());
    }

    public void rateOtherUser(String currentUserUid, String otherUserUid, float rating) {
        firebaseManager.rateOtherUserOnDB(currentUserUid, otherUserUid, rating);
    }

    public boolean isUserLoggedIn() {
        return FirebaseManager.getInstance().isUserLoggedIn();
        //return FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseInstanceId.getInstance().getToken() != null;
    }

    public void setGoogleSignInClient(GoogleSignInClient googleSignInClient) {
        this.googleSignInClient = googleSignInClient;
    }


    public GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }

    public void setNewPhonePermission(User currentUser,User otherUser) {
        //save phone request object in other user phone permission array
        firebaseManager.setNewPhonePermission(currentUser.getId(),otherUser.getId(), MyConstants.PhonePermissionStatus.PENDING);
    }

    public void updatePhonePermissionStatus(String currentUserId, String otherUserId, MyConstants.PhonePermissionStatus status) {
        firebaseManager.updatePhonePermissionStatus(currentUserId, otherUserId,  status);
    }
}
