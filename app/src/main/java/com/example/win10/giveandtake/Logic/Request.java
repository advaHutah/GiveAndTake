package com.example.win10.giveandtake.Logic;


import java.util.ArrayList;

public class Request {

    public enum RequestType {
        GIVE,
        TAKE
    }

    protected String userInputText;
    protected String uid;
    protected String rid;
    protected String userName;
    protected RequestType requestType;
    protected ArrayList<String> tags;

    public Request(){

    }
    public Request(String userInputText, String uid, String userName ,RequestType requestType) {
        this.userInputText = userInputText;
        this.uid = uid;
        this.userName = userName;
        this.requestType = requestType;
        this.tags = new ArrayList<>();
    }

    public Request(String userInputText, String uid, String userName, ArrayList<String> tags, String rid,RequestType requestType) {
        this(userInputText, uid,userName,requestType);
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRid() {
        return rid;
    }

    public RequestType getRequestType() {
        return requestType;
    }
    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
}
