package com.finalproject.giveandtake.DBLogic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.Logic.Request;
import com.finalproject.giveandtake.Logic.TagUserInfo;
import com.finalproject.giveandtake.Logic.User;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.UI.handshakeSession.IncomingSessionRequestActivity;
import com.finalproject.giveandtake.UI.userMatch.UserMatchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


public class GiveAndTakeMessagingService extends FirebaseMessagingService {
    private static final String TAG ="MyFirebaseMessagingServ" ;
    private static GiveAndTakeMessagingService singletonGiveAndTakeMessagingService = null;



    public static GiveAndTakeMessagingService getInstance() {
        if (singletonGiveAndTakeMessagingService == null) {
            singletonGiveAndTakeMessagingService = new GiveAndTakeMessagingService();
        }
        return singletonGiveAndTakeMessagingService;
    }


    public GiveAndTakeMessagingService() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        User currentUser = AppManager.getInstance().getCurrentUser();
                        if (currentUser != null && currentUser.getId() != null) {
                            com.finalproject.giveandtake.DBLogic.FirebaseManager.getInstance().updateToken(AppManager.getInstance().getCurrentUser().getId()
                                    , token);
                        }
                    }
                });
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        User currentUser = AppManager.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getId() != null) {
            com.finalproject.giveandtake.DBLogic.FirebaseManager.getInstance().updateToken(AppManager.getInstance().getCurrentUser().getId()
                    , token);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getNotification().getTitle().contains("Session")){
            getSessionNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
        else
            getMatchNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    private void getMatchNotification(String title,String messageBody) {
        if(!messageBody.isEmpty()) {
            Intent intent = new Intent(this, UserMatchActivity.class);
            intent.putExtra("type", getTypeFromMessage(title));
            intent.putExtra("tag", getTagFromMessage(title));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            AppManager.getInstance().setNotificationUsers(getUsersFromMessage(messageBody, getTypeFromMessage(title)));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
            notiBuilder.setSmallIcon(R.drawable.temp_logo);
            notiBuilder.setContentTitle("קיימת עבורך התאמה פוטנציאלית!");
            notiBuilder.setContentText("תגית:" + getTagFromMessage(title) + " לתת\\לקחת:" + getTypeFromMessage(title));
            notiBuilder.setAutoCancel(true);
            notiBuilder.setSound(defultSoundUri);
            notiBuilder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notiBuilder.build());
        }

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

        Intent intent = new Intent(this, IncomingSessionRequestActivity.class);
        AppManager.getInstance().setSelectedSessionByID(sessionId);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
        notiBuilder.setSmallIcon(R.drawable.temp_logo);
        notiBuilder.setContentTitle("בקשה החלפת זמן חדשה");
        notiBuilder.setContentText("לחץ על מנת להכנס לבקשה");
        notiBuilder.setAutoCancel(true);
        notiBuilder.setSound(defultSoundUri);
        notiBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notiBuilder.build());
    }
}
