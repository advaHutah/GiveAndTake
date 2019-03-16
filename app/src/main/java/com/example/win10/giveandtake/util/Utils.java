package com.example.win10.giveandtake.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Utils {
    public static void showDialog(final String dialogTitle, final String dialogMessage, Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                /*
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                */
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
