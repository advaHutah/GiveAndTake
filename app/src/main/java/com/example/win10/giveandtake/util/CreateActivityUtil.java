package com.example.win10.giveandtake.util;

import android.content.Context;
import android.content.Intent;

import com.example.win10.giveandtake.Logic.User;
import com.example.win10.giveandtake.UI.handshakeSession.HandshakeProcessActivity;
import com.example.win10.giveandtake.UI.handshakeSession.HandshakeSettingsActivity;
import com.example.win10.giveandtake.UI.handshakeSession.HandshakeSummaryActivity;
import com.example.win10.giveandtake.UI.login.LoginActivity;
import com.example.win10.giveandtake.UI.userProfile.UserProfileActivity;

public class CreateActivityUtil {

    public static void createLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void createUserProfileActivity(Context context, User currentUser) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(MyConstants.UID, currentUser != null ? currentUser.getId() : null);
        context.startActivity(intent);
    }

    public static void createHandshakeProcessActivity(Context context, boolean startSession) {
        Intent intent = new Intent(context, HandshakeProcessActivity.class);
        intent.putExtra(MyConstants.START_SESSION, startSession);
        context.startActivity(intent);
    }

    public static void createHandshakeSettingsActivity(Context context, String type) {
        Intent intent = new Intent(context, HandshakeSettingsActivity.class);
        intent.putExtra(MyConstants.REQUEST_TYPE, type);
        context.startActivity(intent);
    }

    public static void createHandshakeSummaryActivity(Context context) {
        Intent intent = new Intent(context, HandshakeSummaryActivity.class);
        context.startActivity(intent);
    }
}
