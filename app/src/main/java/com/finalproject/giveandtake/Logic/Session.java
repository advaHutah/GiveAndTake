package com.finalproject.giveandtake.Logic;


import com.finalproject.giveandtake.util.GeneralUtil;

//when there is match between take request and give request
public class Session {



    public static enum Status {
        pending,
        accepted,
        active,
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
    private Long endTimeStamp;
    private String description;
    private long startTimeStamp;


    public Session() {

    }

    public Session(Status status, Request takeRequest, Request giveRequest, String id,String description,SessionInitiator initiator) {
        this.status = status;
        this.takeRequest = takeRequest;
        this.giveRequest = giveRequest;
        this.id = id;
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


    public void setDescription(String description) {
        this.description = description;
    }

    public void setInitiator(SessionInitiator initiator) {
        this.initiator = initiator;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public boolean isAlive() {
      return endTimeStamp == null && status!= Status.rejected;
    }

    public void setEndTimeStamp(Long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public Long getEndTimeStamp() {
        return endTimeStamp;
    }

    public long getSessionTime() {
        if(endTimeStamp!= null) {
            return endTimeStamp.longValue() - startTimeStamp;
        }
        else return 0;
    }
}
