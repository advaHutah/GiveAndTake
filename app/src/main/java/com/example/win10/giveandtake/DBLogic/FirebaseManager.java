package com.example.win10.giveandtake.DBLogic;

import androidx.annotation.NonNull;
import android.util.Log;

import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.Logic.Session;
import com.example.win10.giveandtake.Logic.TagUserInfo;
import com.example.win10.giveandtake.Logic.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseManager {

    private static final String TAG = FirebaseManager.class.getSimpleName();

    public interface FirebaseCallback<T> {


        void onDataArrived(T value);
    }
    private static FirebaseManager singletonUserService = null;

    private FirebaseDatabase database;
    private boolean isLoggedIn;
    private DatabaseReference db;
    private FirebaseAuth auth;
    private FirebaseManager() {
        //init db connection
        isLoggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                boolean isLoggedIn = firebaseAuth.getCurrentUser() != null;
                Log.d(TAG, "onAuthStateChanged: " + isLoggedIn);
                FirebaseManager.this.isLoggedIn = isLoggedIn;
            }
        });
        database = FirebaseDatabase.getInstance();
        db = database.getReference();
        db.keepSynced(true);
    }

    public static FirebaseManager getInstance() {
        if (singletonUserService == null) {
            singletonUserService = new FirebaseManager();
        }
        return singletonUserService;
    }

    public void addUserInfoToDB(User user) {
        db.child(Keys.USERS).child(user.getId()).setValue(user);
    }

    public void updateUserInfoInDB(String uid, String firstName, String lastName, String phoneNumber) {
        db.child(Keys.USERS).child(uid).child(Keys.FIRTS_NAME).setValue(firstName);
        db.child(Keys.USERS).child(uid).child(Keys.LAST_NAME).setValue(lastName);
        db.child(Keys.USERS).child(uid).child(Keys.PHONE).setValue(phoneNumber);
        db.child(Keys.USERS).child(uid).child(Keys.FULL_NAME).setValue(firstName + " " + lastName);
    }

    public void getUserDetailFromDB(String uid, final FirebaseCallback<User> callback) {
        db.child(Keys.USERS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onDataArrived(dataSnapshot.getValue(User.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDataArrived(null);

            }
        });
    }

    public void getAllRequestFromDB(Request.RequestType requestType, final FirebaseCallback<ArrayList<Request>> callback) {
        //get all requests according to Request.RequestType
        String key = requestType == Request.RequestType.TAKE ? Keys.TAKE_REQUEST : Keys.GIVE_REQUEST;
        db.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Request> list = new ArrayList<Request>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    list.add(child.getValue(Request.class));
                }                // get the values from map.values();
                callback.onDataArrived((ArrayList<Request>) list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });
    }


    public void addRequestToDB(Request newRequest) {
        if (newRequest.getRequestType() == Request.RequestType.GIVE) {
            db.child(Keys.GIVE_REQUEST).child(newRequest.getUid()).setValue(newRequest);
        } else {
            db.child(Keys.TAKE_REQUEST).child(newRequest.getUid()).setValue(newRequest);
        }
    }

    public void updateUserPhoneNumber(String uid, String phoneNumber) {
        db.child(Keys.USERS).child(uid).child(Keys.USER_PHONE).setValue(phoneNumber);
    }

    public void updateToken(String uid, String token) {
        db.child(Keys.USERS).child(uid).child("instanceId").setValue(token);
    }

    public void getToken(String uid, final FirebaseCallback<String> callback) {
        db.child(Keys.NOTIFICATIONS).child(Keys.USERS_TOKENS).child(uid).child(Keys.FSM_TOKEN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onDataArrived(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });
    }

    public void getKeyWordsFromRequest(String uid, Request.RequestType requestType, final FirebaseCallback<ArrayList<String>> callback) {

        String sKey = requestType == Request.RequestType.TAKE ? Keys.TAKE_REQUEST : Keys.GIVE_REQUEST;

        db.child(sKey).child(uid).child(Keys.KEYWORDS).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    list.add(child.getValue(String.class));
                }                // get the values from map.values();
                callback.onDataArrived((ArrayList<String>) list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });
    }

    public void getSuggestedTagsFromRequest(String uid, Request.RequestType requestType, final FirebaseCallback<ArrayList<String>> callback) {

        String sKey = requestType == Request.RequestType.TAKE ? Keys.TAKE_REQUEST : Keys.GIVE_REQUEST;

        db.child(sKey).child(uid).child(Keys.TAGS).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    list.add(child.getValue(String.class));
                }
                callback.onDataArrived((ArrayList<String>) list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });
    }

    public void getTags(Request.RequestType requestType, final FirebaseCallback<ArrayList<String>> callback) {

        String sKey = requestType == Request.RequestType.TAKE ? Keys.TAKE_TAGS : Keys.GIVE_TAGS;

        db.child(sKey).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    list.add(child.getKey());
                }
                // get the values from map.values();
                callback.onDataArrived((ArrayList<String>) list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });
    }


    public void getMatchUsers(final String uid, final String tag, final Request.RequestType requestType, final FirebaseCallback<ArrayList<TagUserInfo>> callback) {

        String sKey = requestType == Request.RequestType.TAKE ? Keys.TAKE_TAGS : Keys.GIVE_TAGS;

        db.child(sKey).child(tag).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TagUserInfo> list = new ArrayList<TagUserInfo>();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    if (!user.getKey().equals(uid)) {
                        Map<String, String> nameEntry = (Map<String, String>) user.getValue();
                        String name = nameEntry.get("name");
                        TagUserInfo tagUserInfo = new TagUserInfo(user.getKey(), name, requestType);
                        list.add(tagUserInfo);
                    }
                } // get the values from map.values();
                callback.onDataArrived((ArrayList<TagUserInfo>) list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });

    }

    public void getRequestFromDB(String uid, Request.RequestType requestType, final FirebaseCallback<Request> callback) {
        String sKey = requestType == Request.RequestType.TAKE ? Keys.TAKE_REQUEST : Keys.GIVE_REQUEST;

        db.child(sKey).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onDataArrived(dataSnapshot.getValue(Request.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });
    }

    public void updateBalanceOnDB(String uid, long balance) {
        db.child(Keys.USERS).child(uid).child(Keys.BALANCE).setValue(balance);

    }

    public void saveSession(Session session) {
        db.child(Keys.SESSIONS).child(session.getId()).setValue(session);
    }

    public void updateSessionStatus(String sessionId, Session.Status sessionStatus) {
        db.child(Keys.SESSIONS).child(sessionId).child(Keys.SESSION_STATUS).setValue(sessionStatus);
    }

    public void updateSessionMillisPassed(String sessionId, long millisPassed) {
        db.child(Keys.SESSIONS).child(sessionId).child(Keys.SESSION_MILLIS_PASSED).setValue(millisPassed);
    }

    public void getSessionFromDB(String sessionId, final FirebaseCallback<Session> callback) {

        db.child(Keys.SESSIONS).child(sessionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onDataArrived(dataSnapshot.getValue(Session.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });
    }

    public void sessionStatusChanged(String sessionId, final FirebaseCallback<Session.Status> callback) {
        db.child(Keys.SESSIONS).child(sessionId).child(Keys.SESSION_STATUS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Session.Status status = dataSnapshot.getValue(Session.Status.class);
                callback.onDataArrived(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean isUserLoggedIn() {
        return isLoggedIn;
    }


    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private class Keys {
        public static final String USERS = "users";
        public static final String TAGS = "suggestedTags";
        public static final String GIVE_REQUEST = "giveRequest";
        public static final String TAKE_REQUEST = "takeRequest";
        public static final String NOTIFICATIONS = "notifications";
        public static final String USERS_TOKENS = "usersTokens";
        public static final String FSM_TOKEN = "fcmToken";
        public static final String FIRTS_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String PHONE = "phoneNumber";
        public static final String FULL_NAME = "fullName";
        public static final String GIVE_TAGS = "giveTags";
        public static final String TAKE_TAGS = "takeTags";
        public static final String USER_PHONE = "phoneNumber";
        public static final String SESSIONS = "sessions";
        public static final String SESSION_STATUS = "status";
        public static final String SESSION_MILLIS_PASSED = "millisPassed";
        public static final String BALANCE = "balance";
        public static final String KEYWORDS = "keyWords";
    }


}
