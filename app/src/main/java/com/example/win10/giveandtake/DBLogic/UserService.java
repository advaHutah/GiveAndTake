package com.example.win10.giveandtake.DBLogic;

import android.util.Log;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.GiveRequest;
import com.example.win10.giveandtake.Logic.TakeRequest;
import com.example.win10.giveandtake.Logic.User;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth auth;

    private String uid;


    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private int balance;
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


    public void addUserInfoToDB(String uid, String email, String firstName, String lastName, String phoneNumber, String gender, int balance) {
        db.child("Users").child(uid).child("email").setValue(email);
        db.child("Users").child(uid).child("firstName").setValue(firstName);
        db.child("Users").child(uid).child("lastName").setValue(lastName);
        db.child("Users").child(uid).child("phoneNumber").setValue(phoneNumber);
        db.child("Users").child(uid).child("gender").setValue(gender);
        db.child("Users").child(uid).child("balance").setValue(balance);
    }
//    public void addUserInfoToDB(User newUser) {
//        db.child("Users").setValue(newUser);
//    }

    public void updateUserInfoInDB() {
        //TODO
    }

    public void getUserDetailFromDB() {
        //TODO
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

}
