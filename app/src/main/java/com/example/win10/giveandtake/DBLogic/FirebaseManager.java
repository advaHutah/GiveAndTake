package com.example.win10.giveandtake.DBLogic;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.win10.giveandtake.Logic.GiveRequest;
import com.example.win10.giveandtake.Logic.Service;
import com.example.win10.giveandtake.Logic.TakeRequest;
import com.example.win10.giveandtake.Logic.User;
import com.example.win10.giveandtake.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
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
    private DatabaseReference db;
    private FirebaseAuth auth;

    private FirebaseManager() {
        //init db connection
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
    public void updateUserInfoInDB (String uid,String firstName, String lastName, String phoneNumber) {
        db.child(Keys.USERS).child(uid).child(Keys.FIRTS_NAME).setValue(firstName);
        db.child(Keys.USERS).child(uid).child(Keys.LAST_NAME).setValue(lastName);
        db.child(Keys.USERS).child(uid).child(Keys.PHONE).setValue(phoneNumber);
        db.child(Keys.USERS).child(uid).child(Keys.FULL_NAME).setValue(firstName+" "+lastName);


    }

    public void addTagsToDB(ArrayList<String> selectedTags) {
        for (String tag : selectedTags) {
            db.child(Keys.TAGS).child(tag).setValue(tag);
        }
    }

    public void getUserDetailFromDB(String uid, final FirebaseCallback<User> callback) {
        db.child(Keys.USERS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onDataArrived(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });
    }

    public void getAllGiveRequestFromDB(final FirebaseCallback<ArrayList<GiveRequest>> callback) {
        db.child(Keys.GIVE_REQUEST).orderByChild("tags").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<GiveRequest> list = new ArrayList<GiveRequest>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    list.add(child.getValue(GiveRequest.class));
                }                // get the values from map.values();
                callback.onDataArrived((ArrayList<GiveRequest>) list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });
    }

    public void getAllTakeRequestFromDB(final FirebaseCallback<ArrayList<TakeRequest>> callback) {
        db.child(Keys.TAKE_REQUEST).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<TakeRequest> list = new ArrayList<TakeRequest>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    list.add(child.getValue(TakeRequest.class));
                }                // get the values from map.values();
                callback.onDataArrived((ArrayList<TakeRequest>) list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });
    }

    public void getAllTagsFromDB(final FirebaseCallback<ArrayList<String>> callback) {
        db.child(Keys.TAGS).addValueEventListener(new ValueEventListener() {

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


    public void addGiveRequestToDB(GiveRequest newGiveRequest) {

        String rid = db.child(Keys.GIVE_REQUEST).push().getKey();
        newGiveRequest.setRid(rid);
        db.child(Keys.GIVE_REQUEST).child(rid).setValue(newGiveRequest);

    }

    public void addTakeRequestToDB(TakeRequest takeRequest) {
        String rid = db.child(Keys.TAKE_REQUEST).push().getKey();
        takeRequest.setRid(rid);
        db.child(Keys.TAKE_REQUEST).child(rid).setValue(takeRequest);

    }

    public void addServiceInDB(String uid, Service newService) {
        String sKey = db.child(Keys.SERVICES).push().getKey();
        newService.setSid(sKey);
        db.child(Keys.SERVICES).child(sKey).setValue(newService);
        db.child(Keys.USERS).child(uid).child(Keys.MY_SERVICES).child(sKey).setValue(newService);
    }

    public void updateUserServcieInDB(String uid, Service service) {
        db.child(Keys.USERS).child(uid).child(Keys.MY_SERVICES).child(service.getSid()).setValue(service);
    }

    public void updateServiceInDB(Service service) {
        db.child(Keys.SERVICES).child(service.getSid()).setValue(service);
    }

    public void updateToken(String uid, String token) {
        db.child(Keys.NOTIFICATIONS).child(Keys.USERS_TOKENS).child(uid).child(Keys.FSM_TOKEN).setValue(token);
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


    public void removeService(Service theService) {
        //remove service
        db.child(Keys.SERVICES).child(theService.getSid()).removeValue();
        //remoce service from giver my services
        db.child(Keys.USERS).child(theService.getGiveRequest().getUid()).child(Keys.MY_SERVICES).child(theService.getSid()).removeValue();
        //remoce service from taker my services
        db.child(Keys.USERS).child(theService.getTakeRequest().getUid()).child(Keys.MY_SERVICES).child(theService.getSid()).removeValue();
    }
    public void signOut (){
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
        public static final String FIRTS_NAME= "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String PHONE = "phoneNumber";
        public static final String FULL_NAME ="fullName" ;
    }


}
