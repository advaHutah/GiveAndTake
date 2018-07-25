package com.example.win10.giveandtake.DBLogic;

import android.util.Log;

import com.example.win10.giveandtake.Logic.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private static UserService singletonUserService = null;
    private FirebaseDatabase database;
    private DatabaseReference db;

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private long balance;
    private User currentUser;
    private ArrayList<User> users = new ArrayList<>();


    private UserService() {
        //init db connection
        database = FirebaseDatabase.getInstance();
        db = database.getReference();
        db.keepSynced(true);
    }

    public static UserService getInstance() {
        if (singletonUserService == null) {
            singletonUserService = new UserService();
        }
        return singletonUserService;
    }


    //    public void addUserInfoToDB(String uid, String email, String firstName, String lastName, String phoneNumber, String gender , int balance) {
//        db.child("Users").child(uid).child("email").setValue(email);
//        db.child("Users").child(uid).child("firstName").setValue(firstName);
//        db.child("Users").child(uid).child("lastName").setValue(lastName);
//        db.child("Users").child(uid).child("phoneNumber").setValue(phoneNumber);
//        db.child("Users").child(uid).child("gender").setValue(gender);
//        db.child("Users").child(uid).child("balance").setValue(balance);
//    }
    public void addUserInfoToDB(User newUser) {
        db.child("Users").setValue(newUser);
    }

    public void updateUserInfoInDB() {
        //TODO
    }

    public void getUserDetailFromDB() {
        //TODO
    }

    public void addRequestToDB() {
        //TODO
    }

    public User getCurrentUserInfoFromDB(final String uid) {

        database.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    User u = postSnapshot.getValue(User.class);
//                    users.add(u);
//                    Log.d("user:", u.toString());
//                }
                email = dataSnapshot.child(uid).child("email").getValue(String.class);
                firstName = (String) dataSnapshot.child(uid).child("firstName").getValue(String.class);
                lastName = (String) dataSnapshot.child(uid).child("lastName").getValue(String.class);
                phoneNumber = (String) dataSnapshot.child(uid).child("phoneNumber").getValue(String.class);
                gender = (String) dataSnapshot.child(uid).child("gender").getValue(String.class);
                balance = (long) dataSnapshot.child(uid).child("balance").getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //currentUser=users.get(uid);
        return currentUser;
    }
}
