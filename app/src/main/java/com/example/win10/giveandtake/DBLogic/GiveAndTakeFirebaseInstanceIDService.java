package com.example.win10.giveandtake.DBLogic;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by win10 on 7/22/2018.
 */

public class GiveAndTakeFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private String token = "";
    @Override
    public void onTokenRefresh() {
        token= FirebaseInstanceId.getInstance().getToken();
    }

    public String getToken() {
        return token;
    }
}
