package com.example.win10.giveandtake.Logic;

import com.example.win10.giveandtake.DBLogic.UserService;

import java.util.ArrayList;

public class GiveRequest {

    protected String userInputText;
    protected String uid;
    protected ArrayList<String> tags;

    public GiveRequest(String userInputText, String uid) {
        this.userInputText = userInputText;
        this.uid = uid;
        this.tags = new ArrayList<>();
        findTags(userInputText);
    }

    public void findTags(String userInputText) {
        //TODO
    }


}
