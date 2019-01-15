package com.example.win10.giveandtake;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.win10.giveandtake.util.Utils;

/**
 * Created by win10 on 8/9/2018.
 */

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void showAlert(String alertTitle, String message, Activity activity) {
        Utils.showDialog(alertTitle, message, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }
}
