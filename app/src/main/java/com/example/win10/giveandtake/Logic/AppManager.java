package com.example.win10.giveandtake.Logic;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.win10.giveandtake.Logic.Service;

//Handles all app actions and DB read/write
public class AppManager {

    private static AppManager singletonAppManager = null;
    private FirebaseManager firebaseManager;
    private User currentUser;
    private FirebaseAuth auth;
    private Service selectedService;

    private Map<Integer, List<String>> conjunctions = new HashMap<Integer, List<String>>() {
        {
            put(1, Arrays.asList("להן", "להם", "לכן", "לכם", "לנו", "לה", "לו", "לך", "לי"));
            put(2, Arrays.asList("שלהן", "שלהם", "שלכן", "שלכם", "שלנו", "שלה", "שלו", "שלך", "שלי"));
            put(3, Arrays.asList("אותן", "אותם", "אתכן", "אתכם", "אותנו", "אותה", "אותו", "אותך", "אותי"));
            put(4, Arrays.asList("איתן", "איתם", "איתכן", "איתכם", "איתנו", "איתך", "איתי"));
            put(5, Arrays.asList("בהן", "בהם", "בכן", "בכם", "בה", "בנו", "בו", "בך", "בי"));
            put(6, Arrays.asList("מהן", "מהם", "מכן", "מכם", "מאיתנו", "ממנה", "ממנו", "ממך", "ממני"));
            put(7, Arrays.asList("אצלן", "אצלם", "אצלכן", "אצלכם", "אצלנו", "אצלה", "אצלו", "אצלי", "אצלך", "אצל"));
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
        firebaseManager.addUserInfoToDB(uid, email, firstName, lastName, phoneNumber, gender, User.INIT_BALANCE);
    }

    public void findMatch(final TakeRequest newTakeRequest) {
        FirebaseManager.getInstance().getAllGiveRequestFromDB(new FirebaseManager.FirebaseCallback<ArrayList<GiveRequest>>() {
            @Override
            public void onDataArrived(ArrayList<GiveRequest> value) {
                Service newService = null;
                for (GiveRequest giveRequest : value) {
                    for (String takeR_tag : newTakeRequest.getTags()) {
                        if (giveRequest.getTags().contains(takeR_tag)) {
                            //create new Service
                            newService = new Service(newTakeRequest, giveRequest, takeR_tag);
                            currentUser.addService(newService);
                            //save service in DB
                            firebaseManager.addServiceInDB(currentUser.getId(), newService);
                            //send notifictaion for service users
                            //TODO
                            break;
                        }
                    }
                    if (newService != null)
                        break;
                }
            }
        });

    }

    public void findMatch(final GiveRequest newGiveRequest) {
        FirebaseManager.getInstance().getAllTakeRequestFromDB(new FirebaseManager.FirebaseCallback<ArrayList<TakeRequest>>() {
            @Override
            public void onDataArrived(ArrayList<TakeRequest> value) {
                Service newService = null;
                for (TakeRequest takeRequest : value) {
                    for (String giveR_tag : newGiveRequest.getTags()) {
                        if (takeRequest.getTags().contains(giveR_tag)) {
                            //create new Service
                            newService = new Service(takeRequest, newGiveRequest, giveR_tag);
                            currentUser.addService(newService);
                            //save service in DB
                            firebaseManager.addServiceInDB(currentUser.getId(), newService);
                            //send notifictaion for service users
                            //TODO
                            break;
                        }
                    }
                    if (newService != null)
                        break;
                }
            }
        });
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

    public void addTakeRequest(String text, ArrayList<String> selectedTags) {
        //create new take request
        TakeRequest newTakeRequest = new TakeRequest(text, currentUser.getId(), currentUser.getFullName(), selectedTags, null);
        // add this request to the user
        currentUser.addTakeRequest(newTakeRequest);
        //add the request to firebase
        firebaseManager.addTakeRequestToDB(newTakeRequest);
        //add selected tags to the tags collection
        firebaseManager.addTagsToDB(selectedTags);
        //find match
        findMatch(newTakeRequest);


    }


    public void addGiveRequest(String text, ArrayList<String> selectedTags) {
        //create new give request
        GiveRequest newGiveRequest = new GiveRequest(text, currentUser.getId(), currentUser.getFullName(), selectedTags, null);
        // add this request to the user
        currentUser.addGiveRequest(newGiveRequest);
        //add the request to firebase
        firebaseManager.addGiveRequestToDB(newGiveRequest);
        //add selected tags to the tags collection
        firebaseManager.addTagsToDB(selectedTags);
        //find match
        findMatch(newGiveRequest);


    }

    public ArrayList<String> findTags(String text) {
        //convert text to array
        ArrayList<String> tags = new ArrayList<String>(Arrays.asList(text.split(" ")));
        //remove all Conjunctions from the array
        for (int i = 0; i < tags.size(); i++) {
            for (Map.Entry<Integer, List<String>> e : conjunctions.entrySet()) {
                if (e.getValue().contains(tags.get(i))) {
                    tags.set(i, "_");
                    break;
                }
            }
        }
        while (tags.contains("_"))
            tags.remove("_");
        //return tag array
        return tags;
    }

    public void setSelectedService(Service selectedService) {
        this.selectedService = selectedService;
    }

    public Service getSelectedService() {
        return selectedService;
    }

    public void serviceEnd(Service service , int minuts)
    {
        //send notification to other user
        //todo
        //update service minuts
        service.setMinuts(minuts);
        //update service status
        service.setStatus(Service.Status.COMPLETED);
        //update service in db
        firebaseManager.updateServiceInDB(service);
        //update users info in db
        firebaseManager.updateUserServcieInDB(service.getTakeRequest().getUid(),service);
        firebaseManager.updateUserServcieInDB(service.getGiveRequest().getUid(),service);

    }
}
