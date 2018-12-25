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
    private Gender gender;
    private int balance;
    private int image;
    //TODO add image resource

    private Request myTakeRequest;
    private Request myGiveRequest;
    private HashMap<String, Service> myServices;

    private FirebaseManager userService = FirebaseManager.getInstance();

    public User() {
    }

    public User(String id, String email, String fullName, String phoneNumber, String gender) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber != null ? phoneNumber : "null";
        this.gender = gender.equals("נקבה") ? Gender.FEMALE : Gender.MALE;
        this.balance = INIT_BALANCE;
        myTakeRequest = new Request();
        myGiveRequest = new Request();
        myServices = new HashMap<String, Service>();
    }

    public User(String id, String email, String fullName, String phoneNumber, String gender, int balance) {
        this(id, email, fullName, phoneNumber, gender);
        this.balance = balance;
    }

    public User(String id, String email, String fullName, String phoneNumber, String gender, int balance, Request myTakeRequest, Request myGiveRequest, HashMap<String, Service> myServices) {
        this(id, email, fullName, phoneNumber, gender, balance);
        this.myTakeRequest = myTakeRequest;
        this.myGiveRequest = myGiveRequest;
        this.myServices = myServices;
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

    public String getGender() {
        return gender.name();
    }

    public int getBalance() {
        return balance;
    }

    public String getFullName() {
        return fullName;
    }

    public int getImage() {
        return image;
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

    public void setGender(String gender) {
        this.gender = gender.equals("נקבה") ? Gender.FEMALE : Gender.MALE;
    }

    @Override
    public String toString() {
        return "User details : email : " + email + ", full name : " + fullName + ", phone : " + phoneNumber + ", gender : " + gender;
    }

    public void addRequest(Request newRequest) {
        if (newRequest.requestType == Request.RequestType.TAKE) {
                myTakeRequest = newRequest;
        } else {
                myGiveRequest = newRequest;
        }
    }

    public void addService(Service service) {
        if (myServices == null) {
            myServices = new HashMap<String, Service>();
        }
      //  myServices.put(service.getSid(), service);
    }

    public Request getMyGiveRequest() {
        return myGiveRequest;
    }

    public HashMap<String, Service> getMyServices() {
        return myServices;
    }

    public Request getMyTakeRequest() {
        return myTakeRequest;
    }

    public void setMyGiveRequests(Request myGiveRequest) {
        this.myGiveRequest = myGiveRequest;
    }

    public void setMyServices(HashMap<String, Service> myServices) {
        this.myServices = myServices;
    }

    public void setMyTakeRequest(Request myTakeRequest) {
        this.myTakeRequest = myTakeRequest;
    }
}
