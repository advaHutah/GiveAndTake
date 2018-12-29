package com.example.win10.giveandtake;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.UI.OtherUserActvity;
import com.example.win10.giveandtake.UI.login.LoginActivity;
import com.example.win10.giveandtake.UI.userProfile.UserProfileActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG ="MyFirebaseMessagingServ" ;
    private AppManager appManager;

    public MyFirebaseMessagingService() {
        appManager = AppManager.getInstance();
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        getNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    private void getNotification(String messageTitle,String messageBody) {
        Intent intent = new Intent(this, OtherUserActvity.class);
        // Set the Activity to start in a new, empty task
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT  | PendingIntent.FLAG_ONE_SHOT);
        Uri defultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        appManager.setOtherUser(messageTitle, new AppManager.AppManagerCallback<Boolean>() {
            @Override
            public void onDataArrived(Boolean value) {
            }
        });
        Notification  notification = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.temp_logo)
        .setContentTitle("Potential Match!")
        .setContentText("You Got A Match on the: "+messageBody)
        .setAutoCancel(true)
        .setSound(defultSoundUri)
        .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);
    }

}
