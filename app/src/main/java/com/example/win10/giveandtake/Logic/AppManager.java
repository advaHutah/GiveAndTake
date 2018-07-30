package com.example.win10.giveandtake.Logic;

import android.util.Log;
import android.widget.Toast;

import com.example.win10.giveandtake.DBLogic.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Handles all app actions and DB read/write
public class AppManager {

    private static AppManager singletonAppManager = null;
    private UserService userService;
    private User currentUser;

    private FirebaseAuth auth;


    private AppManager() {
        userService = UserService.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    public static AppManager getInstance() {
        if (singletonAppManager == null)
            singletonAppManager = new AppManager();
        return singletonAppManager;
    }


    public void createNewUser(String uid, String email, String firstName, String lastName, String phoneNumber, String gender) {
        // User newUser = new User(uid,email, firstName, lastName, phoneNumber, gender);
        userService.addUserInfoToDB(uid, email, firstName, lastName, phoneNumber, gender, User.INIT_BALANCE);
    }



    public void readCurrentUserFromDB(String uid) {
        //todo
    }


    public void findMatch() {
        //TODO
    }

    public void getCurrentUserServices(User currentUser) {
        //TODO
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void setUserLogin(String currentUserId) {
        userService.setUserLogin(currentUserId);
    }

    public void setUserLogout(String currentUserId) {
        userService.setUserLogout(currentUserId);

    }
    public void addTakeRequest(String text){
        TakeRequest newTakeRequest = new TakeRequest(text,auth.getCurrentUser().getUid());
        //currentUser.addTakeRequest(newTakeRequest);//todo remove when fix user info from db
        userService.addTakeRequestToDB(newTakeRequest);
    }

    public void addGiveRequest(String text){
        GiveRequest newGiveRequest = new GiveRequest(text,auth.getCurrentUser().getUid());
       // currentUser.addGiveRequest(newGiveRequest);//todo remove when fix user info from db
        userService.addGiveRequestToDB(newGiveRequest);

    }
}
