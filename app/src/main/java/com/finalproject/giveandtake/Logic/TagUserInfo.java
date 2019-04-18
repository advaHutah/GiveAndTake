package com.finalproject.giveandtake.Logic;

import com.finalproject.giveandtake.Logic.Request;

/**
 * Created by win10 on 12/19/2018.
 */

public class TagUserInfo {

    private String uid;
    private String userName;
    private Request.RequestType requestType;

    public TagUserInfo() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Request.RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(Request.RequestType requestType) {
        this.requestType = requestType;
    }

    public TagUserInfo(String uid, String userName, Request.RequestType requestType) {
        this.uid = uid;
        this.userName = userName;
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return userName;
    }
}

