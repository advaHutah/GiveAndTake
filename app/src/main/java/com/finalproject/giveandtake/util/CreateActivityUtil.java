package com.finalproject.giveandtake.util;

import android.content.Context;
import android.content.Intent;

import com.finalproject.giveandtake.Logic.User;
import com.finalproject.giveandtake.UI.handshakeSession.HandshakeProcessActivity;
import com.finalproject.giveandtake.UI.handshakeSession.HandshakeSettingsActivity;
import com.finalproject.giveandtake.UI.handshakeSession.HandshakeSummaryActivity;
import com.finalproject.giveandtake.UI.login.LoginActivity;
import com.finalproject.giveandtake.UI.userProfile.UserProfileActivity;

public class CreateActivityUtil {

    public static void createLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void createUserProfileActivity(Context context, User currentUser) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(com.finalproject.giveandtake.util.MyConstants.UID, currentUser != null ? currentUser.getId() : null);
        context.startActivity(intent);
    }

    public static void createHandshakeProcessActivity(Context context, boolean startSession) {
        Intent intent = new Intent(context, HandshakeProcessActivity.class);
        intent.putExtra(com.finalproject.giveandtake.util.MyConstants.START_SESSION, startSession);
        context.startActivity(intent);
    }

    public static void createHandshakeSettingsActivity(Context context, String type) {
        Intent intent = new Intent(context, HandshakeSettingsActivity.class);
        intent.putExtra(com.finalproject.giveandtake.util.MyConstants.REQUEST_TYPE, type);
        context.startActivity(intent);
    }

    public static void createHandshakeSummaryActivity(Context context) {
        Intent intent = new Intent(context, HandshakeSummaryActivity.class);
        context.startActivity(intent);
    }
}
