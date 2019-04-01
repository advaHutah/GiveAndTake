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

        User currentUser = AppManager.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getId() != null) {
            FirebaseManager.getInstance().updateToken(AppManager.getInstance().getCurrentUser().getId()
                    , refreshedToken);
        }
    }
}
