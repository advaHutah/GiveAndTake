package com.example.win10.giveandtake.DBLogic;

import android.app.Service;
import android.util.Log;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.example.win10.giveandtake.Logic.AppManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class GiveAndTakeInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG ="MyFirebaseInstanceIDSer" ;


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        if(AppManager.getInstance().getCurrentUser().getId()!=null)
//            FirebaseManager.getInstance().updateToken(AppManager.getInstance().getCurrentUser().getId()
//                    ,refreshedToken);
 }
}
