package com.finalproject.giveandtake.UI.handshakeSession;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.Logic.Request;
import com.finalproject.giveandtake.Logic.Session;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.util.CreateActivityUtil;
import com.finalproject.giveandtake.util.MyConstants;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

public class HandshakeSettingsActivity extends AppCompatActivity {
    private AppManager appManager;

    private Button btnStartProcess, btnSendSession;
    private TextView step1, step2, step3;
    private EditText descriptionText, mintuesSet;

    String type;
    String startSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handshake_settings);

        appManager = AppManager.getInstance();

        type = getIntent().getStringExtra(MyConstants.REQUEST_TYPE);
        startSession = getIntent().getStringExtra(MyConstants.START_SESSION);

        descriptionText = (EditText) findViewById(R.id.sessionDescription);
        mintuesSet = (EditText) findViewById(R.id.sessionTimeSet);

        btnSendSession = (Button) findViewById(R.id.btn_handshake_sendSession);
        btnStartProcess = (Button) findViewById(R.id.btn_handshake_start_process);
        step1 = (TextView) findViewById(R.id.handshakeActivity_step1);
        step2 = (TextView) findViewById(R.id.handshakeActivity_step2);
        step3 = (TextView) findViewById(R.id.handshakeActivity_step3);

        //buttonActions
        btnSendSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!descriptionText.getText().toString().isEmpty() && !mintuesSet.getText().toString().isEmpty()) {
                String description = descriptionText.getText().toString();
                long timeSet = TimeUnit.MINUTES.toMillis(Integer.parseInt(mintuesSet.getText().toString()));

                    //user gives to other user
                    if (getType().equals(Request.RequestType.GIVE.toString())) {
                        appManager.setSelectedSession(new Session(Session.Status.pending, appManager.getOtherUser().getMyTakeRequest(),
                                appManager.getCurrentUser().getMyGiveRequest(), UUID.randomUUID().toString(), description, timeSet, Session.SessionInitiator.GIVER));
                    }
                    //user takes from other user
                    else {
                        appManager.setSelectedSession(new Session(Session.Status.pending, appManager.getCurrentUser().getMyTakeRequest()
                                , appManager.getOtherUser().getMyGiveRequest(), UUID.randomUUID().toString(), description, timeSet, Session.SessionInitiator.TAKER));
                    }
                    //send start request to other user
                    appManager.saveSession();
                    changeTextColorToGrey(step1);

                    //step2: wait for accept from other user
                    appManager.sessionStatusChanged(new AppManager.AppManagerCallback<Session.Status>() {
                        @Override
                        public void onDataArrived(Session.Status value) {
                            if (value == Session.Status.accepted) {
                                changeTextColorToGrey(step2);
                                addToast("The session request was accepted", Toast.LENGTH_SHORT, getHandshakeSettingActivity());
                                // step3: enable start service button
                                enableButton(btnStartProcess);
                            } else if (value == Session.Status.rejected) {
                                addToast("The session request was rejected", Toast.LENGTH_SHORT, getHandshakeSettingActivity());
                            }
                        }
                    });
                }else
                {
                    addToast("יש להכניס תיאור וזמן", Toast.LENGTH_SHORT, getHandshakeSettingActivity());

                }

            }
        });


        btnStartProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo probably will need an extra validation for starting Timer
                CreateActivityUtil.createHandshakeProcessActivity(getHandshakeSettingActivity(), true);
            }
        });

    }

    private void changeTextColorToGrey(TextView step) {
        step.setTextColor(Color.GRAY);
    }

    public void enableButton(Button theButton) {
        theButton.setEnabled(true);
    }

    public void addToast(String text, int duration, Activity activity) {
        Context context = activity.getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public Activity getHandshakeSettingActivity() {
        return this;
    }

    public String getType() {
        return type;
    }

}
