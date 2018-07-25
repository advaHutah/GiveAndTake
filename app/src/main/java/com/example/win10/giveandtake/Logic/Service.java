package com.example.win10.giveandtake.Logic;

//when there is match between take request and give request
public class Service {

    public enum Status {
        NO_DATE_SPECIFIDE,
        DATE_SPECIFIDE_NOT_COMPLETE,
        COMPLETED
    };
    private User userGive;
    private User userTake;
    private Status status;


    public Service(User userGive, User userTake) {
        this.userGive = userGive;
        this.userTake = userTake;
        this.status = Status.NO_DATE_SPECIFIDE;
    }

    public User getUserGive() {
        return userGive;
    }

    public User getUserTake() {
        return userTake;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
