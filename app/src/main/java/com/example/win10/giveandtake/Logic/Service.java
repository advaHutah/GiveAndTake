package com.example.win10.giveandtake.Logic;

import java.io.Serializable;

//when there is match between take request and give request
public class Service implements Serializable {


    public enum Status {
        NOT_COMPLETE,
        COMPLETED
    }

    ;
    protected Status status;
    protected TakeRequest takeRequest;
    protected GiveRequest giveRequest;
    protected String sid;
    protected String description;
    protected int minuts;


    public Service() {

    }

    public Service(TakeRequest takeRequest, GiveRequest giveRequest, String description) {
        this.status = Status.NOT_COMPLETE;
        this.takeRequest = takeRequest;
        this.giveRequest = giveRequest;
        this.description = description;
        this.minuts=0;
    }
    public Service(TakeRequest takeRequest, GiveRequest giveRequest,String description,String sid , int minuts) {
        this(takeRequest,giveRequest,description);
        this.sid=sid;
        this.minuts=minuts;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public GiveRequest getGiveRequest() {
        return giveRequest;
    }

    public void setGiveRequest(GiveRequest giveRequest) {
        this.giveRequest = giveRequest;
    }

    public TakeRequest getTakeRequest() {
        return takeRequest;
    }

    public void setTakeRequest(TakeRequest takeRequest) {
        this.takeRequest = takeRequest;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getMinuts() {
        return minuts;
    }

    public void setMinuts(int minuts) {
        this.minuts += minuts;
    }
}
