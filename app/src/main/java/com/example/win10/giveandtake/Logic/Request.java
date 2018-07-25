package com.example.win10.giveandtake.Logic;

import com.example.win10.giveandtake.DBLogic.UserService;

public class Request {
    public enum Type {
        GIVE,
        TAKE
    }

    ;
    private String userInputText;
    private String[] tags;
    private Type type;

    public Request(String userInputText, Type type) {
        //TODO
        this.userInputText = userInputText;
        this.type = type;
        findTags(userInputText);
    }

    public void findTags(String userInputText) {
        //TODO
    }


}
