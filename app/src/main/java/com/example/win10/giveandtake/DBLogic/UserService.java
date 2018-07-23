package com.example.win10.giveandtake.DBLogic;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserService {

    private static UserService singletonUserService = null;
    private FirebaseDatabase database;
    private DatabaseReference db;

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


    public void addUserInfoToDB() {
        //TODO
    }

    public void updateUserInfoInDB() {
        //TODO
    }

    public void getUserDetailFromDB() {
        //TODO
    }
}
