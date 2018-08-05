package com.example.win10.giveandtake.Logic;


import java.util.ArrayList;

public class TakeRequest {

     protected String userInputText;
    protected  String uid;
    protected ArrayList<String> tags;

    public TakeRequest(String userInputText,String uid) {
        this.userInputText = userInputText;
        this.tags = new ArrayList<String>();
        this.uid = uid;
    }
    public TakeRequest(String userInputText,String uid,ArrayList<String> tags) {
        this(userInputText,uid);
        this.tags = tags;
    }
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getUid() {
        return uid;
    }

    public String getUserInputText() {
        return userInputText;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserInputText(String userInputText) {
        this.userInputText = userInputText;
    }

}
