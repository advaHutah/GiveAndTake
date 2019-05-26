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
import com.finalproject.giveandtake.UI.userProfile.PhoneRequestActivity;
import com.finalproject.giveandtake.Util.MyConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


public class GiveAndTakeMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    private static final String PHONE_NOTE_TITLE = "בקשה לקבלת מספר טלפון";
    private static final String PHONE_NOTE_REQUEST_BODY = "לחץ על מנת להכנס לבקשה";
    private static final String PHONE_NOTE_ANSWER_BODY = "בקשתך";
    private static final String SESSION_NOTE_TITLE = "בקשה החלפת זמן חדשה";
    private static final String SESSION_NOTE_BODY = "לחץ על מנת להכנס לבקשה";
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

        if (remoteMessage.getNotification().getTitle().contains(SESSION_NOTE_TITLE)) {
            getSessionNotification(remoteMessage.getNotification().getTag());
        } else if (remoteMessage.getNotification().getTitle().contains(PHONE_NOTE_TITLE) &&
                remoteMessage.getNotification().getBody().contains(PHONE_NOTE_REQUEST_BODY)) {
            getPhoneRequestNotification(remoteMessage.getNotification().getTag());
        }
        else if (remoteMessage.getNotification().getTitle().contains(PHONE_NOTE_TITLE) &&
                remoteMessage.getNotification().getBody().contains(PHONE_NOTE_ANSWER_BODY)) {
            getPhoneRequestNotificationAnswer(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTag());
        }
        else
            getMatchNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTag());
    }

    private void getPhoneRequestNotificationAnswer(String title, String body, String otherUserID) {
        //create dialog of phone request
        if (!otherUserID.isEmpty()) {

            Uri defultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
            notiBuilder.setSmallIcon(R.drawable.temp_logo);
            notiBuilder.setAutoCancel(true);
            notiBuilder.setSound(defultSoundUri);
            notiBuilder.setContentTitle(title);
            notiBuilder.setContentText(body);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(new Random().nextInt(), notiBuilder.build());
        }

    }


    private void getPhoneRequestNotification(String otherUserID) {
        //create dialog of phone request
        if (!otherUserID.isEmpty()) {
            Intent intent = new Intent(this, PhoneRequestActivity.class);
            intent.putExtra(MyConstants.PHONE_OTHER_USER, otherUserID);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, new Random().nextInt(), intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
            notiBuilder.setSmallIcon(R.drawable.temp_logo);
            notiBuilder.setContentTitle(PHONE_NOTE_TITLE);
            notiBuilder.setContentText(PHONE_NOTE_REQUEST_BODY);
            notiBuilder.setAutoCancel(true);
            notiBuilder.setSound(defultSoundUri);
            notiBuilder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(new Random().nextInt(), notiBuilder.build());
        }

    }

    private void getMatchNotification(String title, String messageBody, String tag) {
        if (!messageBody.isEmpty() && !tag.isEmpty()) {
            Intent intent = new Intent(this, UserMatchActivity.class);
            intent.putExtra("type", getTypeFromMessage(title));
            intent.putExtra("tag", getTagFromMessage(messageBody));
            intent.putExtra(MyConstants.IS_FROM_NOTIFICATION,true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ArrayList usersFromMessage =getUsersFromMessage(tag, getTypeFromMessage(title));
            AppManager.getInstance().setNotificationUsers(usersFromMessage);
            if(!usersFromMessage.isEmpty()) {
                PendingIntent pendingIntent = PendingIntent.getActivity(this, new Random().nextInt(), intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
                notiBuilder.setSmallIcon(R.drawable.temp_logo);
                notiBuilder.setAutoCancel(true);
                notiBuilder.setContentTitle(title);
                notiBuilder.setContentText(messageBody);
                notiBuilder.setSound(defultSoundUri);
                notiBuilder.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(new Random().nextInt(), notiBuilder.build());
            }
        }

    }

    private ArrayList<TagUserInfo> getUsersFromMessage(String message, String type) {
        ArrayList<TagUserInfo> users = new ArrayList();
        Request.RequestType requestType;
        if (type.equalsIgnoreCase("give"))
            //notification was sent to giver about potential takers
            requestType = Request.RequestType.TAKE;
        else
            //notification was sent to t taker bout potential givers
            requestType = Request.RequestType.GIVE;

        String[] info = message.split("#");
        for (int i = 0; i < info.length; i = i + 2) {
            String uid = info[i].substring("uid:".length());
            String uname = info[i + 1].substring("uname:".length());
            users.add(new TagUserInfo(uid, uname, requestType));
        }
        return users;
    }

    private String getTagFromMessage(String body) {
        String[] arr = body.split(" ");
        return arr[arr.length-1];
    }

    private String getTypeFromMessage(String title) {
        return title.equals("תן") ? "Give" : "Take";
    }

    private void getSessionNotification(String sessionId) {
        if (!sessionId.isEmpty()) {
            Intent intent = new Intent(this, IncomingSessionRequestActivity.class);
            AppManager.getInstance().setSelectedSessionByID(sessionId);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, new Random().nextInt(), intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
            notiBuilder.setSmallIcon(R.drawable.temp_logo);
            notiBuilder.setContentTitle(SESSION_NOTE_TITLE);
            notiBuilder.setContentText(SESSION_NOTE_BODY);
            notiBuilder.setAutoCancel(true);
            notiBuilder.setSound(defultSoundUri);
            notiBuilder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(new Random().nextInt(), notiBuilder.build());
        }
    }
}
