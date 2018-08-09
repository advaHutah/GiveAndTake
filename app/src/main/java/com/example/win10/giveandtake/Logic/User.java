package com.example.win10.giveandtake.Logic;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.GiveRequestFragment;
import com.example.win10.giveandtake.UI.TakeRequestFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class User implements Serializable {

    final static int INIT_BALANCE = 0;

    public enum Gender {
        MALE,
        FEMALE
    }

    ;
    private String id;
    private String email;
    //private String password; //TODO checke if o need to save the password
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Gender gender;
    private int balance;
    //TODO add birthdate
    //TODO add image resource

    private HashMap<String, TakeRequest> myTakeRequest;
    private HashMap<String, GiveRequest> myGiveRequests;
    private HashMap<String, Service> myServices;

    private FirebaseManager userService = FirebaseManager.getInstance();

    public User() {
    }

    public User(String id, String email, String firstName, String lastName, String phoneNumber, String gender) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender.equals("נקבה") ? Gender.FEMALE : Gender.MALE;
        this.balance = INIT_BALANCE;
        myTakeRequest = new HashMap<String, TakeRequest>();
        myGiveRequests = new HashMap<String, GiveRequest>();
        myServices = new HashMap<String, Service>();
    }

    public User(String id, String email, String firstName, String lastName, String phoneNumber, String gender, int balance) {
        this(id, email, firstName, lastName, phoneNumber, gender);
        this.balance = balance;
    }

    public User(String id, String email, String firstName, String lastName, String phoneNumber, String gender, int balance, HashMap<String, TakeRequest> myTakeRequest, HashMap<String, GiveRequest> myGiveRequests, HashMap<String, Service> myServices) {
        this(id, email, firstName, lastName, phoneNumber, gender, balance);
        this.myTakeRequest = myTakeRequest;
        this.myGiveRequests = myGiveRequests;
        this.myServices = myServices;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender.name();
    }

    public int getBalance() {
        return balance;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender.equals("נקבה") ? Gender.FEMALE : Gender.MALE;
    }


    @Override
    public String toString() {
        return "User details : email : " + email + ", full name : " + firstName + " " + lastName + ", phone : " + phoneNumber + ", gender : " + gender;
    }


    public void addTakeRequest(TakeRequest newTakeRequest) {
        if (myTakeRequest == null)
            myTakeRequest = new HashMap<String, TakeRequest>();
        myTakeRequest.put(newTakeRequest.getRid(), newTakeRequest);
    }

    public void addGiveRequest(GiveRequest newGiveRequest) {
        if (myGiveRequests == null)
            myGiveRequests = new HashMap<String, GiveRequest>();
        myGiveRequests.put(newGiveRequest.getRid(), newGiveRequest);
    }

    public void addService(Service service) {
        if (myServices == null) {
            myServices = new HashMap<String, Service>();
        }
        myServices.put(service.getSid(), service);
    }

    public HashMap<String, GiveRequest> getMyGiveRequests() {
        return myGiveRequests;
    }

    public HashMap<String, Service> getMyServices() {
        return myServices;
    }

    public HashMap<String, TakeRequest> getMyTakeRequest() {
        return myTakeRequest;
    }

    public void setMyGiveRequests(HashMap<String, GiveRequest> myGiveRequests) {
        this.myGiveRequests = myGiveRequests;
    }

    public void setMyServices(HashMap<String, Service> myServices) {
        this.myServices = myServices;
    }

    public void setMyTakeRequest(HashMap<String, TakeRequest> myTakeRequest) {
        this.myTakeRequest = myTakeRequest;
    }
}
