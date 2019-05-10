package com.finalproject.giveandtake.UI.userProfile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.giveandtake.UI.userProfile.InputSenderDialog;
import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.util.CreateActivityUtil;
import com.finalproject.giveandtake.util.GeneralUtil;
import com.finalproject.giveandtake.util.TimeConvertUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {


    private static final String TAG = "UserProfileActivity";
    private AppManager appManager;


    private Button btnMyPhone;
    private Button btnLogout;
    private TextView userNameText;
    private TextView userBalanceText;
    private TextView userEmailText;
    private ImageView userImage;
    private RatingBar ratingBar;
    private String phoneNumber = "";

    public static final String PHONE_RGX = "^[0-9]{10}$";

    final Activity UActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        appManager = AppManager.getInstance();

        userNameText = (TextView) findViewById(R.id.my_profile_name);
        userBalanceText = (TextView) findViewById(R.id.my_profile_balance);
        userEmailText = (TextView) findViewById(R.id.my_profile_email);
        userImage = (ImageView) findViewById(R.id.my_profile_image);
        ratingBar = (RatingBar) findViewById(R.id.my_profile_rating);

        btnMyPhone = (Button) this.findViewById(R.id.btn_my_profile_phone);
        btnLogout = (Button) this.findViewById(R.id.btn_logout);

        btnMyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                createPhoneNumberDialog();
            }

        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                signOut();
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        GeneralUtil generalUtil = new GeneralUtil();
        generalUtil.setUserImage(appManager.getCurrentUser().getPhotoUrl(), userImage);

        userNameText.setText(appManager.getCurrentUser().getFullName());
        userEmailText.setText("(" + appManager.getCurrentUser().getEmail() + ")");
        setUserPhone(appManager.getCurrentUser().getPhoneNumber());
        setUserBalance(appManager.getCurrentUser().getBalance());
        phoneNumber = appManager.getCurrentUser().getPhoneNumber();
        ratingBar.setRating(appManager.getCurrentUser().getRating());
        if (phoneNumber == null || phoneNumber.equals("Null") || phoneNumber.isEmpty()) {
            btnMyPhone.setText(R.string.btnPhoneNum_text);
        } else {
            btnMyPhone.setText(phoneNumber);
        }
    }

    private void signOut() {
        // Firebase sign out
        appManager.signOut();

        // Google sign out
        appManager.getGoogleSignInClient().signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        CreateActivityUtil.createLoginActivity(getUserProfileActivity());
                    }
                });
    }

    private void setUserBalance(long balance) {
        String time = TimeConvertUtil.convertTime(balance);
        userBalanceText.setText(time);
    }


    private void setUserPhone(String phoneNumber) {
        if (phoneNumber != null) {
            btnMyPhone.setText(phoneNumber);
        }
    }


    private void createPhoneNumberDialog() {
        new InputSenderDialog(UActivity, new InputSenderDialog.InputSenderDialogListener() {
            @Override
            public void onOK(final String number) {
                Pattern pattern = Pattern.compile(PHONE_RGX);
                if (pattern.matcher(number).matches()) {
                    phoneNumber = number;
                    btnMyPhone.setText(phoneNumber);
                    appManager.updateUserPhoneNumber(phoneNumber);
                } else {
                    GeneralUtil.addToast(getApplication().getString(R.string.phoneNum_InvalidInputErrMsg), Toast.LENGTH_LONG, getApplication());
                    phoneNumber = "";
                    btnMyPhone.setText(getApplication().getString(R.string.btnPhoneNum_text));
                    appManager.updateUserPhoneNumber(phoneNumber);
                }
            }

            @Override
            public void onCancel(String number) {
                Log.d(TAG, "The user tapped Cancel, number is " + number);
            }
        }).setNumber(phoneNumber).show();
    }

    private UserProfileActivity getUserProfileActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
