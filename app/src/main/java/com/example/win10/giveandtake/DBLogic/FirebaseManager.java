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


    public void addUserInfoToDB(String uid, String email, String firstName, String lastName, String phoneNumber, String gender, int balance) {
        User user = new User(uid, email, firstName, lastName, phoneNumber, gender, balance);
        db.child(Keys.USERS).child(uid).setValue(user);
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
    public void updateUserInfoInDB() {
        //TODO
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

    public void getAllGiveRequestFromDB(final FirebaseCallback <ArrayList<GiveRequest>>callback) {
        db.child(Keys.GIVE_REQUEST).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<GiveRequest> list = new ArrayList<GiveRequest>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    list.add(child.getValue(GiveRequest.class));
                }                // get the values from map.values();
                callback.onDataArrived((ArrayList<GiveRequest>)list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ");
                callback.onDataArrived(null);
            }
        });
    }


//    public void setUserLogin(String currentUserId) {
//
//        db.child("Users").child(currentUserId).child("isLogin").setValue(1);
//    }
//
//    public void setUserLogout(String currentUserId) {
//        db.child("Users").child(currentUserId).child("isLogin").setValue(0);
//
//    }

    public void addGiveRequestToDB(GiveRequest newGiveRequest) {
        db.child(Keys.GIVE_REQUEST).push().setValue(newGiveRequest);

    }

    public void addTakeRequestToDB(TakeRequest takeRequest) {
        db.child(Keys.TAKE_REQUEST).push().setValue(takeRequest);
    }

    public void addServiceInDB(Service newService) {
        db.child(Keys.SERVICES).push().setValue(newService);
    }
    public void matchNotification(Service service)
    {

    }

    private class Keys {
        public static final String USERS = "users";
        public static final String TAGS = "tags";
        public static final String GIVE_REQUEST = "GiveRequest";
        public static final String TAKE_REQUEST = "TakeRequest";
        public static final String SERVICES = "services";
    }
}
