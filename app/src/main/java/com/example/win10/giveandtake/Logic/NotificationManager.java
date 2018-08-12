package com.example.win10.giveandtake.Logic;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.example.win10.giveandtake.MyApplication;
import com.example.win10.giveandtake.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NotificationManager {
    private static NotificationManager singletonNotificationManager = null;


    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final String TAG = "Notification";
    private static int noti_id=0;
    private FirebaseManager firebaseManager = FirebaseManager.getInstance();

    private NotificationManager() {
    }

    public static NotificationManager getInstance() {
        if (singletonNotificationManager == null)
            singletonNotificationManager = new NotificationManager();
        return singletonNotificationManager;
    }

    public void sendNotificationToTheUser(String uid, String title, final String body) {

        firebaseManager.getToken(uid, new FirebaseManager.FirebaseCallback<String>() {
            @Override
            public void onDataArrived(String value) {
                String fcmToken = value;
                HashMap<String, String> data = new HashMap<>();
                data.put("more data", "some ID");
                sendFcmNotificationUsingUrlRequest(generateNotificationPayload("Test", "From Android"), data, new String[]{fcmToken}, new FirebaseManager.FirebaseCallback<Response>() {
                    @Override
                    public void onDataArrived(Response value) {
                        Log.d(TAG, "sendFcmNotificationUsingUrlRequest: " + value);
                      //  sendNotification("Match Notification","We find match for you ! ");
                    }

                });
            }
        });

    }

    private HashMap<String, String> generateNotificationPayload(String title, String body) {
        HashMap<String, String> notificationDictionary = new HashMap<>();
        notificationDictionary.put("alert", title);
        notificationDictionary.put("title", title);
        notificationDictionary.put("body", body);
        notificationDictionary.put("icon", "app-icon");
        notificationDictionary.put("sound", "default.aiff");

        return notificationDictionary;
    }

    private void showNowNotification(Notification notification, int notificationId) {
        NotificationManagerCompat.from(MyApplication.getContext()).notify(notificationId, notification);
    }

//    private void scheduleNotification(Notification notification, int notificationId, int delay) {
//        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
//        PendingIntent broadcastPendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, broadcastPendingIntent);
//    }

    //notificationDictionary: [String:String], dataDictionary: [String:String], toRegistrationIds registrationIds: [String], completion: @escaping (Bool) -> ()
    private void sendFcmNotificationUsingUrlRequest(HashMap<String, String> notificationDictionary, HashMap<String, String> dataDictionary, String[] registrationIds, final FirebaseManager.FirebaseCallback<Response> callbacksHandler) {
        HashMap<String, Object> jsonDictionary = new HashMap<>();
        jsonDictionary.put("registration_ids", registrationIds); // or use 'to' for ony one registration ID (without using an array)
        jsonDictionary.put("notification", notificationDictionary);
        jsonDictionary.put("data", dataDictionary);

        HashMap<String, String> httpHeaders = new HashMap<>();
        String secretKey = "AIzaSyChmQwpD8juXnZ0nGmuWqz7aBSOsFh-aFI";
        httpHeaders.put("Authorization", "key= " + secretKey);

        makePostRequest(new Gson().toJson(jsonDictionary), "https://fcm.googleapis.com/fcm/send", httpHeaders, callbacksHandler);
    }


    /**
     * Performs a network request to the specified URL with the given parameters.
     *
     * @param urlString The URL of the URL request.
     * @return A response object if the request succeeded, otherwise it returns null.
     */
    void makePostRequest(@Nullable String jsonString, final String urlString, @Nullable HashMap<String, String> httpHeaders, final FirebaseManager.FirebaseCallback callbacksHandler) {
        try {
            Request.Builder builder = new Request.Builder()
                    .url(urlString);

            if (httpHeaders != null) {
                for (Map.Entry<String, String> httpHeaderKey : httpHeaders.entrySet()) {
                    builder.header(httpHeaderKey.getKey(), httpHeaderKey.getValue());
                }
            }

            builder.post(RequestBody.create(MediaType.parse("json"), jsonString));

            final Request request = builder.build();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Response response = null;
                    try {
                        response = httpClient.newCall(request).execute(); // OkHttpClient - An open source project, downloaded from gradle
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "performRequest: Failed to perform url request with string '" + urlString + "', exception: " + e.toString());
                        if (callbacksHandler != null)
                            callbacksHandler.onDataArrived(null);
                    }

                    if (callbacksHandler != null)
                        callbacksHandler.onDataArrived(response);
                }
            }).start();

        } catch (IllegalArgumentException e) {
            Log.e(TAG, "performRequest: Failed to create url request with string '" + urlString + "'");
            if (callbacksHandler != null)
                callbacksHandler.onDataArrived(null);
        }
    }

    public static void sendNotification(String title,String body) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MyApplication.getContext(), "notify_001");
        Intent ii = new Intent(MyApplication.getContext(), MyApplication.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getContext(), 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(body);
        bigText.setBigContentTitle(title);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.temp_logo);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        android.app.NotificationManager mNotificationManager =
                (android.app.NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(noti_id, mBuilder.build());
        noti_id++;
    }

}

