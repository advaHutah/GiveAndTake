package com.example.win10.giveandtake.Logic;

/**
 * Created by win10 on 7/16/2018.
 */

public class User {

    public enum Gender {
        MALE,
        FEMALE
    };
    private String email;
    //private String password; //TODO checke if o need to save the password
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Gender gender ;
    //TODO add birthdate

    public User( String email,String firstName, String lastName,String phoneNumber,String gender)
    {
        this.email=email;
        this.firstName = firstName;
        this.lastName=lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender.equals(Gender.FEMALE)? Gender.FEMALE :Gender.MALE;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User details : email : "+email+", full name : "+firstName+" " +lastName +", phone : "+phoneNumber+", gender : "+gender;
    }
}
