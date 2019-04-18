package com.finalproject.giveandtake.Logic;


//when there is match between take request and give request
public class Session {

    public static enum Status {
        pending,
        accepted,
        active,
        paused,
        terminated,
        rejected
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
    private long millisPassed;
    private long millisSet;
    private String description;


    public Session() {

    }

    public Session(Status status, Request takeRequest, Request giveRequest, String id,String description, long millisSet,SessionInitiator initiator) {
        this.status = status;
        this.takeRequest = takeRequest;
        this.giveRequest = giveRequest;
        this.id = id;
        this.millisPassed = 0;
        this.millisSet = millisSet;
        this.initiator = initiator;
        this.description = description;
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

    public long getMillisPassed() {
        return millisPassed;
    }

    public long getMillisSet() {
        return millisSet;
    }

    public String getDescription() {
        return description;
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

    public void setMillisPassed(long millisPassed) {
        this.millisPassed = millisPassed;
    }

    public void setMillisSet(long millisSet) {
        this.millisSet = millisSet;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInitiator(SessionInitiator initiator) {
        this.initiator = initiator;
    }
}
