package com.example.win10.giveandtake.Logic;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Handles all app actions and DB read/write
public class AppManager {

    private static AppManager singletonAppManager = null;
    private FirebaseManager firebaseManager;
    private User currentUser;

    private FirebaseAuth auth;

    private Map<Integer ,List<String>> conjunctions = new HashMap<Integer ,List<String>>()
    {
        {
            put(1,Arrays.asList("להן","להם","לכן","לכם","לנו","לה","לו","לך","לי"));
            put(2,Arrays.asList("שלהן","שלהם","שלכן","שלכם","שלנו","שלה","שלו","שלך","שלי"));
            put(3,Arrays.asList("אותן","אותם","אתכן","אתכם","אותנו","אותה","אותו","אותך","אותי"));
            put(4,Arrays.asList ("איתן","איתם","איתכן","איתכם","איתנו","איתך","איתי"));
            put(5,Arrays.asList ("בהן","בהם","בכן","בכם","בה","בנו","בו","בך","בי"));
            put(6,Arrays.asList ("מהן","מהם","מכן","מכם","מאיתנו","ממנה","ממנו","ממך","ממני"));
            put(7,Arrays.asList ("אצלן","אצלם","אצלכן","אצלכם","אצלנו","אצלה","אצלו","אצלי","אצלך","אצל"));
        }
    };

    private AppManager() {
        firebaseManager = FirebaseManager.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    public static AppManager getInstance() {
        if (singletonAppManager == null)
            singletonAppManager = new AppManager();
        return singletonAppManager;
    }


    public void createNewUser(String uid, String email, String firstName, String lastName, String phoneNumber, String gender) {
        // User newUser = new User(uid,email, firstName, lastName, phoneNumber, gender);
        firebaseManager.addUserInfoToDB(uid, email, firstName, lastName, phoneNumber, gender, User.INIT_BALANCE);
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
        firebaseManager.setUserLogin(currentUserId);
    }

    public void setUserLogout(String currentUserId) {
        firebaseManager.setUserLogout(currentUserId);

    }
    public void  addTakeRequest(String text,  ArrayList<String>selectedTags){
        TakeRequest newTakeRequest = new TakeRequest(text,auth.getCurrentUser().getUid(),selectedTags);
//        currentUser.addTakeRequest(newTakeRequest);//todo remove when uploding user info from db
        firebaseManager.addTakeRequestToDB(newTakeRequest);
    }


    public  void  addGiveRequest(String text ,  ArrayList<String>selectedTags){
        GiveRequest newGiveRequest = new GiveRequest(text,auth.getCurrentUser().getUid(),selectedTags);
       // currentUser.addGiveRequest(newGiveRequest);//todo remove when uploding user info from db
        firebaseManager.addGiveRequestToDB(newGiveRequest);
    }

    public ArrayList<String>findTags(String text){
        //convert text to array
        ArrayList<String>tags = new ArrayList<String>(Arrays.asList(text.split(" ")));
        //remove all Conjunctions from the array
        for (int i=0;i<tags.size();i++) {
            for(Map.Entry<Integer, List<String>> e: conjunctions.entrySet()){
                if(e.getValue().contains(tags.get(i)))
                {
                    tags.set(i,"_");
                    break;
                }
            }
        }
        while(tags.contains("_"))
            tags.remove("_");
        //return tag array
        return tags;
    }
}
