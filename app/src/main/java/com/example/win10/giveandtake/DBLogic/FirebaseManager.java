package com.example.win10.giveandtake.DBLogic;

import android.util.Log;

import com.example.win10.giveandtake.Logic.GiveRequest;
import com.example.win10.giveandtake.Logic.TakeRequest;
import com.example.win10.giveandtake.Logic.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseManager {

    private static final String TAG = FirebaseManager.class.getSimpleName();

    public interface FirebaseCallback<T> {
        void onDataArrived(T value);
    }

    private static FirebaseManager singletonUserService = null;
    private FirebaseDatabase database;
    private DatabaseReference db;
    private FirebaseAuth auth;

//    private String uid;


    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private int balance;
    private User currentUser;
    private ArrayList<User> users = new ArrayList<>();


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

//        db.child("Users").child(uid).child("email").setValue(email);
//        db.child("Users").child(uid).child("firstName").setValue(firstName);
//        db.child("Users").child(uid).child("lastName").setValue(lastName);
//        db.child("Users").child(uid).child("phoneNumber").setValue(phoneNumber);
//        db.child("Users").child(uid).child("gender").setValue(gender);
//        db.child("Users").child(uid).child("balance").setValue(balance);
    }
//    public void addUserInfoToDB(User newUser) {
//        db.child("Users").setValue(newUser);
//    }

    public void updateUserInfoInDB() {
        //TODO
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



    public void setUserLogin(String currentUserId) {

        db.child("Users").child(currentUserId).child("isLogin").setValue(1);
    }

    public void setUserLogout(String currentUserId) {
        db.child("Users").child(currentUserId).child("isLogin").setValue(0);

    }

    public void addGiveRequestToDB(GiveRequest newGiveRequest) {
        db.child("GiveRequest").push().setValue(newGiveRequest);

    }
    public void addTakeRequestToDB(TakeRequest takeRequest) {
        db.child("TakeRequest").push().setValue(takeRequest);
    }

    private class Keys {
        public static final String USERS = "users";
    }
}
