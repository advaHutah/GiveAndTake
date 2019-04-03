package com.example.win10.giveandtake.util;

import android.content.Context;
import android.content.Intent;

import com.example.win10.giveandtake.UI.login.LoginActivity;

public class CreateActivityUtil {

    public static void createLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
