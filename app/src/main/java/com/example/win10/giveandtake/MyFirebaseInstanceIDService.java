package com.example.win10.giveandtake;

import android.app.Service;
import android.util.Log;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.example.win10.giveandtake.Logic.AppManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG ="MyFirebaseInstanceIDSer" ;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String instanceId = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + instanceId);
        if(AppManager.getInstance().getCurrentUser().getId()!=null)
            FirebaseManager.getInstance().updateToken(AppManager.getInstance().getCurrentUser().getId()
                    ,instanceId);
    }



}
