package com.example.win10.giveandtake.UI.userProfile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.userHashtags.HashtagsManagementActivity;
import com.example.win10.giveandtake.UI.userMatch.MyMatchActivity;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class UserProfileActivity extends AppCompatActivity {


    private static final String TAG = "UserProfileActivity";
    private Handler imagesDownloaderHandler;
    private AppManager appManager;

    private Button btnMyHashtags;
    private Button btnMyMatch;
    private Button btnMyPhone;
    private TextView userNameText;
    private TextView userBalanceText;
    private TextView userEmailText;
    private ImageView userImage;

    private boolean bPhoneNumber = false;
    private String phoneNumber;
    final Activity UActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        HandlerThread handlerThread = new HandlerThread("images-downloader");
        handlerThread.start();
        imagesDownloaderHandler = new Handler(handlerThread.getLooper());

        appManager = AppManager.getInstance();

        userNameText = (TextView) findViewById(R.id.my_profile_name);
        userBalanceText = (TextView) findViewById(R.id.my_profile_balance);
        userEmailText = (TextView) findViewById(R.id.my_profile_email);
        userImage = (ImageView) this.findViewById(R.id.my_profile_image);
        btnMyHashtags = (Button) this.findViewById(R.id.btn_my_profile_hashtags);
        btnMyMatch = (Button) this.findViewById(R.id.btn_my_profile_match);
        btnMyPhone = (Button) this.findViewById(R.id.btn_my_profile_phone);

        if (appManager.getCurrentUser() != null) {
            userNameText.setText(appManager.getCurrentUser().getFullName());
            userEmailText.setText("(" + appManager.getCurrentUser().getEmail() + ")");
            setUserImage(appManager.getCurrentUser().getPhotoUrl());
            setUserPhone(appManager.getCurrentUser().getPhoneNumber());
            setUserBalance(appManager.getCurrentUser().getBalance());
        }


        btnMyHashtags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createHashtagsActivity();
            }
        });
        btnMyMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMyMatchActivity();
            }
        });
        btnMyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (bPhoneNumber) {
                    Intent intent =new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phoneNumber));
                    startActivity(intent);

                } else {
                    new InputSenderDialog(UActivity, new InputSenderDialog.InputSenderDialogListener() {
                        @Override
                        public void onOK(final String number) {
                            phoneNumber = number;
                            setUserPhone(phoneNumber);
                            appManager.updateUserPhoneNumer(phoneNumber);
                            Log.d(TAG, "The user tapped OK, number is "+number);
                        }

                        @Override
                        public void onCancel(String number) {
                            Log.d(TAG, "The user tapped Cancel, number is "+number);
                        }
                    }).setNumber(phoneNumber).show();
                }
            }
        });


    }

    private void setUserBalance(int balance) {
        String time = TimeConvert.secondToFullTime(balance);
        userBalanceText.setText(time);
    }



    private void setUserPhone(String phoneNumber) {
        if (!phoneNumber.equals("null")) {
            btnMyPhone.setText(phoneNumber);
            this.phoneNumber = phoneNumber;
            bPhoneNumber = true;
        } else {
            btnMyPhone.setText("Press to add phone number");
            bPhoneNumber = false;
        }
    }

    private void createHashtagsActivity() {
        Intent intent = new Intent(this, HashtagsManagementActivity.class);
        startActivity(intent);
    }

    private void createMyMatchActivity() {
        Intent intent = new Intent(this, MyMatchActivity.class);
        startActivity(intent);
    }

    public void setUserImage(String imageUrl) {
        if (imageUrl != null) {
            new DownloadImageTask((ImageView) findViewById(R.id.image)).execute(imageUrl);
        } else
            userImage.setImageResource(R.drawable.default_user);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
