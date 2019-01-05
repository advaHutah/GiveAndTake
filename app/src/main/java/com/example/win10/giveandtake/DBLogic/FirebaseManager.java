package com.example.win10.giveandtake.DBLogic;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.Logic.Service;
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

    public boolean isUserLoggedIn() {
        return isLoggedIn;
    }


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

    //    public void matchListener(final Context context){
//        db.child(Keys.SERVICES).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    //TODO
//                    //send notification
//                    for (DataSnapshot child: dataSnapshot.getChildren()){
//                        if(((child.getValue(Service.class)).getGiveRequest().getUid())
//
//                    }
//                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                            .setSmallIcon(R.drawable.temp_logo)
//                            .setContentTitle("Match")
//                            .setContentText("we found a match for you")
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                    //send notification to sender
//                    //add service to giver
//                    //add service to taker
//                    //add action when tapping on notification
//                }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
    public void updateUserInfoInDB(String uid, String firstName, String lastName, String phoneNumber) {
        db.child(Keys.USERS).child(uid).child(Keys.FIRTS_NAME).setValue(firstName);
        db.child(Keys.USERS).child(uid).child(Keys.LAST_NAME).setValue(lastName);
        db.child(Keys.USERS).child(uid).child(Keys.PHONE).setValue(phoneNumber);
        db.child(Keys.USERS).child(uid).child(Keys.FULL_NAME).setValue(firstName + " " + lastName);


    }

    public void addTagsToDB(ArrayList<String> selectedTags, TagUserInfo tagUserInfo) {
        for (String tag : selectedTags) {
            db.child(Keys.TAGS).child(tag).child(tagUserInfo.getUid()).setValue(tagUserInfo);
        }
    }

    public void getUserDetailFromDB(String uid, final FirebaseCallback<User> callback) {
        db.child(Keys.USERS).child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                callback.onDataArrived(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
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

    public void getAllTagsFromDB(final FirebaseCallback<Map<String, ArrayList<TagUserInfo>>> callback) {
        db.child(Keys.TAGS).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, ArrayList<TagUserInfo>> tags = new HashMap<>();
                for (DataSnapshot tag : dataSnapshot.getChildren()) {
                    ArrayList<TagUserInfo> aTagUserInfo = new ArrayList<>();
                    for (DataSnapshot tagUserInfo : tag.getChildren()) {
                        aTagUserInfo.add(tagUserInfo.getValue(TagUserInfo.class));
                    }
                    tags.put(tag.getKey(), aTagUserInfo);
                }
                // get the values from map.values();
                callback.onDataArrived(tags);
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
    public void updateUserPhoneNumber(String uid,String phoneNumber) {
        db.child(Keys.USERS).child(uid).child(Keys.USER_PHONE).setValue(phoneNumber);
    }

    public void addServiceInDB(String uid, Service newService) {
//        String sKey = db.child(Keys.SERVICES).push().getKey();
//        newService.setSid(sKey);
//        db.child(Keys.SERVICES).child(sKey).setValue(newService);
//        db.child(Keys.USERS).child(uid).child(Keys.MY_SERVICES).child(sKey).setValue(newService);
    }

    public void updateUserServcieInDB(String uid, Service service) {
        // db.child(Keys.USERS).child(uid).child(Keys.MY_SERVICES).child(service.getSid()).setValue(service);
    }

    public void updateServiceInDB(Service service) {
        //  db.child(Keys.SERVICES).child(service.getSid()).setValue(service);
    }

    public void updateToken(String uid, String token) {
        // db.child(Keys.NOTIFICATIONS).child(Keys.USERS_TOKENS).child(uid).child(Keys.FSM_TOKEN).setValue(token);
        //db.child(Keys.NOTIFICATIONS).child(Keys.USERS_TOKENS).child(uid).child(Keys.FSM_TOKEN).setValue(token);
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


    public void getTagsFromRequest(String uid, Request.RequestType requestType, final FirebaseCallback<ArrayList<String>> callback) {

        String sKey = requestType == Request.RequestType.TAKE ? Keys.TAKE_REQUEST : Keys.GIVE_REQUEST;

        db.child(sKey).child(uid).child(Keys.TAGS).addValueEventListener(new ValueEventListener() {

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
                        //todo sometimes falls can be replaced to just -> user.getValue().toString()
                        String name = nameEntry.get("name");
                        TagUserInfo tagUserInfo = new TagUserInfo(user.getKey(), name, requestType);
                        list.add(tagUserInfo);
                    }
                }                // get the values from map.values();
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



    public void removeService(Service theService) {
//        //remove service
//        db.child(Keys.SERVICES).child(theService.getSid()).removeValue();
//        //remoce service from giver my services
//        db.child(Keys.USERS).child(theService.getGiveRequest().getUid()).child(Keys.MY_SERVICES).child(theService.getSid()).removeValue();
//        //remoce service from taker my services
//        db.child(Keys.USERS).child(theService.getTakeRequest().getUid()).child(Keys.MY_SERVICES).child(theService.getSid()).removeValue();
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private class Keys {
        public static final String USERS = "users";
        public static final String TAGS = "tags";
        public static final String GIVE_REQUEST = "giveRequest";
        public static final String TAKE_REQUEST = "takeRequest";
        public static final String SERVICES = "services";
        public static final String MY_SERVICES = "myServices";
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
        public static final String IS_FINAL = "isFinal";
    }


}
