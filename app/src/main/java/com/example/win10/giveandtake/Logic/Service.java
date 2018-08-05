package com.example.win10.giveandtake.Logic;

//when there is match between take request and give request
public class Service {

    public enum Status {
        NO_DATE_SPECIFIDE,
        DATE_SPECIFIDE_NOT_COMPLETE,
        COMPLETED
    };
    protected Status status;
    protected TakeRequest takeRequest;
    protected GiveRequest giveRequest;


    public Service(TakeRequest takeRequest,GiveRequest giveRequest) {
        this.status = Status.NO_DATE_SPECIFIDE;
        this.takeRequest= takeRequest;
        this.giveRequest = giveRequest;
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
}
