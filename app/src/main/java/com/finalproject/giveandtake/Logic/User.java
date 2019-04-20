package com.finalproject.giveandtake.Logic;

import com.finalproject.giveandtake.Logic.Request;
import com.finalproject.giveandtake.DBLogic.FirebaseManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class User implements Serializable {

    final static long INIT_BALANCE = TimeUnit.HOURS.toMillis(2);

    public enum Gender {
        MALE,
        FEMALE
    }

    private String id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private float rating;
    private Map<String,Float> usersRatings;
    private long balance;// milisec
    private String photoUrl;

    private Request myTakeRequest;
    private Request myGiveRequest;

    private FirebaseManager userService = FirebaseManager.getInstance();

    public User() {
    }

    public User(String id, String email, String fullName, String phoneNumber,String photoUrl) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber != null ? phoneNumber : "null";
        this.photoUrl=photoUrl;
        this.balance = INIT_BALANCE;
        this.rating=0;
        this.usersRatings = new HashMap<>();
        myTakeRequest = new Request();
        myGiveRequest = new Request();
    }

    public User(String id, String email, String fullName, String phoneNumber, long balance,String photoUrl,float rating,Map<String,Float> usersRatings) {
        this(id, email, fullName, phoneNumber,photoUrl);
        this.balance = balance;
        this.rating = rating;
        this.usersRatings = usersRatings;

    }

    public User(String id, String email, String fullName, String phoneNumber, long balance, Request myTakeRequest, Request myGiveRequest ,String photoUrl,float rating,Map<String,Float> usersRatings) {
        this(id, email, fullName, phoneNumber,balance,photoUrl,rating,usersRatings);
        this.myTakeRequest = myTakeRequest;
        this.myGiveRequest = myGiveRequest;
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

    public long getBalance() {
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

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Map<String, Float> getUsersRatings() {
        return usersRatings;
    }

    public void setUsersRatings(Map<String, Float> usersRatings) {
        this.usersRatings = usersRatings;
    }

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


    public Request getMyGiveRequest() {
        return myGiveRequest;
    }

    public Request getMyTakeRequest() {
        return myTakeRequest;
    }

    public void setMyGiveRequests(Request myGiveRequest) {
        this.myGiveRequest = myGiveRequest;
    }

    public void setMyTakeRequest(Request myTakeRequest) {
        this.myTakeRequest = myTakeRequest;
    }
}
