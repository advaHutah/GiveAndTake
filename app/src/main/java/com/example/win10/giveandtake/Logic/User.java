package com.example.win10.giveandtake.Logic;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {

    final static int INIT_BALANCE = 8 * 60;

    public enum Gender {
        MALE,
        FEMALE
    }

    private String id;
    private String email;
    private String fullName;
    private String phoneNumber;
//    private Gender gender;
    private int balance;
    private String photoUrl;

    private Request myTakeRequest;
    private Request myGiveRequest;
    private HashMap<String, Session> myServices;

    private FirebaseManager userService = FirebaseManager.getInstance();

    public User() {
    }

    public User(String id, String email, String fullName, String phoneNumber,String photoUrl) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber != null ? phoneNumber : "null";
        //this.gender = gender.equals("נקבה") ? Gender.FEMALE : Gender.MALE;
        this.photoUrl=photoUrl;
        this.balance = INIT_BALANCE;
        myTakeRequest = new Request();
        myGiveRequest = new Request();
        myServices = new HashMap<String, Session>();
    }

    public User(String id, String email, String fullName, String phoneNumber, int balance,String photoUrl) {
        this(id, email, fullName, phoneNumber,photoUrl);
        this.balance = balance;
    }

    public User(String id, String email, String fullName, String phoneNumber, int balance, Request myTakeRequest, Request myGiveRequest ,String photoUrl) {
        this(id, email, fullName, phoneNumber, balance,photoUrl);
        this.myTakeRequest = myTakeRequest;
        this.myGiveRequest = myGiveRequest;
        //this.mySessions = mySessions;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

//    public String getGender() {
//        return gender.name();
//    }

    public int getBalance() {
        return balance;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

//    public void setGender(String gender) {
//        this.gender = gender.equals("נקבה") ? Gender.FEMALE : Gender.MALE;
//    }

    @Override
    public String toString() {
        return "User details : email : " + email + ", full name : " + fullName + ", phone : " + phoneNumber;
     //   return "User details : email : " + email + ", full name : " + fullName + ", phone : " + phoneNumber + ", gender : " + gender;

    }

    public void addRequest(Request newRequest) {
        if (newRequest.requestType == Request.RequestType.TAKE) {
                myTakeRequest = newRequest;
        } else {
                myGiveRequest = newRequest;
        }
    }

    public void addService(Session session) {
        if (myServices == null) {
            myServices = new HashMap<String, Session>();
        }
      //  mySessions.put(session.getSid(), session);
    }

    public Request getMyGiveRequest() {
        return myGiveRequest;
    }

    public HashMap<String, Session> getMyServices() {
        return myServices;
    }

    public Request getMyTakeRequest() {
        return myTakeRequest;
    }

    public void setMyGiveRequests(Request myGiveRequest) {
        this.myGiveRequest = myGiveRequest;
    }

    public void setMyServices(HashMap<String, Session> myServices) {
        this.myServices = myServices;
    }

    public void setMyTakeRequest(Request myTakeRequest) {
        this.myTakeRequest = myTakeRequest;
    }
}
