package com.finalproject.giveandtake.UI.userProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.util.CreateActivityUtil;
import com.finalproject.giveandtake.util.MyConstants;

public class PhoneRequestActivity extends AppCompatActivity {

    Button btnViewUserProfile, btnOk, btnCancel;
    TextView description;
    String otherUserId;
    AppManager appManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_request);
        appManager = AppManager.getInstance();
        btnViewUserProfile = (Button) findViewById(R.id.btn_phone_request_view_profile);
        description = (TextView) findViewById(R.id.phone_request_description);
        btnOk = (Button) findViewById(R.id.btn_accept_phone);
        btnCancel = (Button) findViewById(R.id.btn_reject_phone);

        btnViewUserProfile.setEnabled(false);
        Intent intent = getIntent();
        otherUserId = intent.getStringExtra(MyConstants.PHONE_OTHER_USER);
        appManager.setOtherUser(otherUserId, new AppManager.AppManagerCallback<Boolean>() {
            @Override
            public void onDataArrived(Boolean value) {
                btnViewUserProfile.setEnabled(true);
            }
        });

        btnViewUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateActivityUtil.createOtherUserActivity(getApplicationContext());
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appManager.updatePhonePermissionStatus(appManager.getCurrentUser().getId(), otherUserId,MyConstants.PhonePermissionStatus.ACCEPT);
                getPhoneRequestActivity().finish();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appManager.updatePhonePermissionStatus(appManager.getCurrentUser().getId(), otherUserId,MyConstants.PhonePermissionStatus.REJECT);
                getPhoneRequestActivity().finish();
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    private Activity getPhoneRequestActivity(){
        return this;
    }
}
