package com.example.win10.giveandtake.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by win10 on 3/23/2019.
 */

public class GeneralUtil {

    public static void addToast(String text, int duration, Context context) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
