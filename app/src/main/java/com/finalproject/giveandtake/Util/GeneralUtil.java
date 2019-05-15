package com.finalproject.giveandtake.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.finalproject.giveandtake.R;

import java.io.InputStream;

public class GeneralUtil {

    private static long timestampDiff = 0;

    public static void setTimestampDiff(long timestampDiff) {
        GeneralUtil.timestampDiff = timestampDiff;
    }

    public static void addToast(String text, int duration, Context context) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setUserImage(String imageUrl, ImageView imageView) {
        if (imageUrl != null) {
            new DownloadImageTask(imageView).execute(imageUrl);
        } else
            imageView.setImageResource(R.drawable.default_user);
    }

    public static long now() {
        return System.currentTimeMillis() + timestampDiff;
    }


    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}


