package com.example.win10.giveandtake.DBLogic;

import android.app.Service;
import android.util.Log;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.User;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class GiveAndTakeInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseInstanceIDSer";


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        User currentUser = AppManager.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getId() != null) {
            FirebaseManager.getInstance().updateToken(AppManager.getInstance().getCurrentUser().getId()
                    , refreshedToken);
            // Perry: "Please notice!"
            // java.lang.NullPointerException
            //        at com.example.win10.giveandtake.DBLogic.GiveAndTakeInstanceIdService.onTokenRefresh(GiveAndTakeInstanceIdService.java:26)
            //        at com.google.firebase.iid.FirebaseInstanceIdService.handleIntent(Unknown Source)
            //        at com.google.firebase.iid.zzc.run(Unknown Source)
            //        ...
        }
    }
}