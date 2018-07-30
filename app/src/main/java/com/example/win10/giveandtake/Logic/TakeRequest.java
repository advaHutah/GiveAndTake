package com.example.win10.giveandtake.Logic;


import java.util.ArrayList;

public class TakeRequest {

     protected String userInputText;
    protected  String uid;
    protected ArrayList<String> tags;

    public TakeRequest(String userInputText,String uid) {
        this.userInputText = userInputText;
        this.tags = new ArrayList<String>();
        this.uid = uid;
        findTags(userInputText);
    }

    public void findTags(String userInputText) {
        //TODO
    }

}
