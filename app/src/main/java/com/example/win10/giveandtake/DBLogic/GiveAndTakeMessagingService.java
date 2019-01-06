package com.example.win10.giveandtake.DBLogic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.Logic.TagUserInfo;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.userMatch.MyMatchActivity;
import com.example.win10.giveandtake.UI.handshakeSession.OtherUserSessionRequestActivity;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;


public class GiveAndTakeMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG ="MyFirebaseMessagingServ" ;

    public GiveAndTakeMessagingService() {
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

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

        if(remoteMessage.getNotification().getTitle().contains("Session")){
            getSessionNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
        else
            getMatchNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    private void getMatchNotification(String title,String messageBody) {
        Intent intent = new Intent(this, MyMatchActivity.class);
        intent.putExtra("type",getTypeFromMessage(title));
        intent.putExtra("tag",getTagFromMessage(title));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AppManager.getInstance().setNotificationUsers(getUsersFromMessage(messageBody,getTypeFromMessage(title)));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
        notiBuilder.setSmallIcon(R.drawable.temp_logo);
        notiBuilder.setContentTitle("You Got a Potetntial Match Users!");
        notiBuilder.setContentText("Tag:" + getTagFromMessage(title) + " Type:"+ getTypeFromMessage(title));
        notiBuilder.setAutoCancel(true);
        notiBuilder.setSound(defultSoundUri);
        notiBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notiBuilder.build());

    }

    private ArrayList<TagUserInfo> getUsersFromMessage(String message,String type){
        ArrayList<TagUserInfo>  users = new ArrayList();
        Request.RequestType requestType;
        if(type.equalsIgnoreCase("give"))
            //notification was sent to giver about potential takers
            requestType = Request.RequestType.TAKE;
        else
            //notification was sent to t taker bout potential givers
            requestType = Request.RequestType.GIVE;

        String[] info = message.split("#");
        for(int i =0 ; i < info.length ; i= i+2){
            String uid = info[i].substring("uid:".length());
            String uname = info[i+1].substring("uname:".length());
            users.add(new TagUserInfo(uid,uname,requestType));
        }
        return users;
    }
    private String getTagFromMessage(String title){
        return title.substring(0,title.indexOf("|"));
    }

    private String getTypeFromMessage(String title){
        return title.substring(title.indexOf("|")+1);
    }

    private void getSessionNotification(String title, String sessionId) {

        Intent intent = new Intent(this, OtherUserSessionRequestActivity.class);
        AppManager.getInstance().setSelectedSessionByID(sessionId);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
        notiBuilder.setSmallIcon(R.drawable.temp_logo);
        notiBuilder.setContentTitle("New Session Request!");
        notiBuilder.setContentText("Press To Enter Session");
        notiBuilder.setAutoCancel(true);
        notiBuilder.setSound(defultSoundUri);
        notiBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notiBuilder.build());
    }
}