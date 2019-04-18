package com.finalproject.giveandtake.Logic;


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
    protected RequestType requestType;
    protected ArrayList<String> suggestedTags;
    protected ArrayList<String> keyWords;
    protected ArrayList<String> stopWords;

    private Map<String,ArrayList<com.finalproject.giveandtake.Logic.TagUserInfo>> match ;

    public Request(){
    }
    public Request(String userInputText, String uid, String userName ,RequestType requestType) {
        this.userInputText = userInputText;
        this.uid = uid;
        this.userName = userName;
        this.requestType = requestType;
        this.suggestedTags = new ArrayList<>();
        this.match = new HashMap<>();
        this.keyWords = new ArrayList<>();
        this.stopWords = new ArrayList<>();
    }

    public Request(String userInputText, String uid, String userName, ArrayList<String> suggestedTags,ArrayList<String> keyWords,ArrayList<String> stopWords,RequestType requestType) {
        this(userInputText, uid,userName,requestType);
        this.suggestedTags = suggestedTags;
        this.keyWords = keyWords;
        this.stopWords = stopWords;
    }

    public void setSuggestedTags(ArrayList<String> suggestedTags) {
        this.suggestedTags = suggestedTags;
    }

    public ArrayList<String> getSuggestedTags() {
        return suggestedTags;
    }

    public ArrayList<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(ArrayList<String> keyWords) {
        this.keyWords = keyWords;
    }

    public ArrayList<String> getStopWords() {
        return stopWords;
    }

    public void setStopWords(ArrayList<String> stopWords) {
        this.stopWords = stopWords;
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

    public Map<String, ArrayList<com.finalproject.giveandtake.Logic.TagUserInfo>> getMatch() {
        return match;
    }
    public void setMatch(Map<String,ArrayList<com.finalproject.giveandtake.Logic.TagUserInfo>> match) {
        this.match = match;
    }


}
