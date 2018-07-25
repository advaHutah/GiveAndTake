package com.example.win10.giveandtake.Logic;

import com.example.win10.giveandtake.DBLogic.UserService;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Handles all app actions and DB read/write
public class AppManager {

    private static AppManager singletonAppManager = null;
    private UserService userService;
    private User currentUser;

    private AppManager() {
        userService = UserService.getInstance();
    }

    public static AppManager getInstance() {
        if (singletonAppManager == null)
            singletonAppManager = new AppManager();
        return singletonAppManager;
    }


    public void createNewUser(String uid, String email, String firstName, String lastName, String phoneNumber, String gender) {
        User newUser = new User(uid,email, firstName, lastName, phoneNumber, gender);
        userService.addUserInfoToDB(newUser);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void readCurrentUserFromDB(FirebaseUser firebaseUser) {
        currentUser = userService.getCurrentUserInfoFromDB(firebaseUser.getUid());
    }


    public void findMatch() {
        //TODO
    }

    public void getCurrentUserServices(User currentUser) {
        //TODO
    }
}
