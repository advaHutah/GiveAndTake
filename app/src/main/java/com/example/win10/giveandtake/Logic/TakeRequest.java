package com.example.win10.giveandtake.Logic;


import java.util.ArrayList;

public class TakeRequest {

    protected String userInputText;
    protected String uid;
    protected String rid;
    protected String userName;
    protected ArrayList<String> tags;

    public TakeRequest(){

    }

    public TakeRequest(String userInputText, String uid, String userName) {
        this.userInputText = userInputText;
        this.tags = new ArrayList<String>();
        this.uid = uid;
        this.userName = userName;
    }

    public TakeRequest(String userInputText, String uid, String userName, ArrayList<String> tags ,String rid) {
        this(userInputText, uid, userName);
        this.tags = tags;
        this.rid = rid;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRid() {
        return rid;
    }
}
