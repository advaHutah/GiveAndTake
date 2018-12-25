package com.example.win10.giveandtake.Logic;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Request {

    public enum RequestType {
        GIVE,
        TAKE
    }

    protected String userInputText;
    protected String uid;
    protected String userName;
    protected int isFinal;
    protected RequestType requestType;
    protected ArrayList<String> tags;
    private Map<String,ArrayList<TagUserInfo>> match ;

    public Request(){
    }
    public Request(String userInputText, String uid, String userName ,RequestType requestType) {
        this.userInputText = userInputText;
        this.uid = uid;
        this.userName = userName;
        this.requestType = requestType;
        this.tags = new ArrayList<>();
        this.match = new HashMap<>();
        this.isFinal=0;
    }

    public Request(String userInputText, String uid, String userName, ArrayList<String> tags,RequestType requestType) {
        this(userInputText, uid,userName,requestType);
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public RequestType getRequestType() {
        return requestType;
    }
    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Map<String, ArrayList<TagUserInfo>> getMatch() {
        return match;
    }
    public void setMatch(Map<String,ArrayList<TagUserInfo>> match) {
        this.match = match;
    }

    public int getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(int isFinal) {
        this.isFinal = isFinal;
    }
}
