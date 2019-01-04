package com.example.win10.giveandtake.Logic;


//when there is match between take request and give request
public class Session {

    public static enum Status {
        pending,
        active,
        paused,
        terminated
    }

    public static enum SessionInitiator {
        GIVER,
        TAKER,
    }
   
    private SessionInitiator initiator;
    private Status status;
    private Request takeRequest;
    private Request giveRequest;
    private String id;
    private int minutes;

    public Session() {

    }

    public Session(Status status, Request takeRequest, Request giveRequest, String id, int minutes,SessionInitiator initiator) {
        this.status = status;
        this.takeRequest = takeRequest;
        this.giveRequest = giveRequest;
        this.id = id;
        this.minutes = minutes;
        this.initiator = initiator;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Request getTakeRequest() {
        return takeRequest;
    }

    public Request getGiveRequest() {
        return giveRequest;
    }

    public String getId() {
        return id;
    }

    public int getMinutes() {
        return minutes;
    }

    public SessionInitiator getInitiator() {
        return initiator;
    }

    public void setTakeRequest(Request takeRequest) {
        this.takeRequest = takeRequest;
    }

    public void setGiveRequest(Request giveRequest) {
        this.giveRequest = giveRequest;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setInitiator(SessionInitiator initiator) {
        this.initiator = initiator;
    }
}
