package com.example.win10.giveandtake.Logic;

import com.example.win10.giveandtake.DBLogic.UserService;
import com.example.win10.giveandtake.R;

public class User {

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

    private Service[] myService;

    private UserService userService = UserService.getInstance();

    public User() {
    }

    public User(String id , String email, String firstName, String lastName, String phoneNumber, String gender) {
        this.id =id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender.equals("נקבה") ? Gender.FEMALE : Gender.MALE;
        this.balance = INIT_BALANCE;
    }

    public User(String id,String email, String firstName, String lastName, String phoneNumber, String gender, int balance) {
        this(id,email, firstName, lastName, phoneNumber, gender);
        this.balance = balance;
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

    public Gender getGender() {
        return gender;
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

    @Override
    public String toString() {
        return "User details : email : " + email + ", full name : " + firstName + " " + lastName + ", phone : " + phoneNumber + ", gender : " + gender;
    }


    public void createNewRequest() {
        userService.addRequestToDB();
    }

    public void createTakeRequest(String userInputText, Request.Type type) {
        //TODO
        Request newTakeRequest = new Request(userInputText, type);
    }

    public void createGiveRequest(String userInputText, Request.Type type) {
        //TODO
        Request newGiveRequest = new Request(userInputText, type);
    }

}
