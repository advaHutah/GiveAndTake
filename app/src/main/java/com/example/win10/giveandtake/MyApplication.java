package com.example.win10.giveandtake;

import android.app.Application;
import android.content.Context;

/**
 * Created by win10 on 8/9/2018.
 */

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }
}
