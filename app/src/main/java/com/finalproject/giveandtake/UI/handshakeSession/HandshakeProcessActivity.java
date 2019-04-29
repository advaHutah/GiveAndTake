package com.finalproject.giveandtake.UI.handshakeSession;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.Logic.Session;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.util.CreateActivityUtil;
import com.finalproject.giveandtake.util.GeneralUtil;
import com.finalproject.giveandtake.util.MyConstants;
import com.finalproject.giveandtake.util.TimeConvertUtil;

import androidx.appcompat.app.AppCompatActivity;

public class HandshakeProcessActivity extends AppCompatActivity {

    private AppManager appManager;
    private TextView timerValue, actionText, balanceValue;
    private Button stopBtn;
    private CountDownTimer timer;
    private long millisPassed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handshake_process);

        timerValue = (TextView)findViewById(R.id.timerValue);
        balanceValue = (TextView) findViewById(R.id.balanceValue);
        actionText = (TextView)findViewById(R.id.handshakeProcessActivity_action_description);
        stopBtn = (Button) findViewById(R.id.handshakeProcessActivity_stopBtn);
        appManager = AppManager.getInstance();
        boolean sessionRestored = getIntent().getBooleanExtra(MyConstants.SESSION_RESTORED,false);

        if(sessionRestored) {
            appManager.refreshTimestampDelta();
            long timePassedAlready = GeneralUtil.now() - appManager.getSelectedSession().getStartTimeStamp();

            //im the giver
            if (appManager.getSelectedSession().getGiveRequest().getUid().equals(appManager.getCurrentUser().getId())) {
                appManager.getCurrentUser().setBalance(appManager.getCurrentUser().getBalance()+timePassedAlready);
            }
            else {
                appManager.getCurrentUser().setBalance(appManager.getCurrentUser().getBalance()- timePassedAlready);

            }
            millisPassed=appManager.getSelectedSession().getMillisSet() - timePassedAlready;
            startTimer(millisPassed);
        }
        else{
            millisPassed = 0;
        }


        appManager = AppManager.getInstance();
        setActionText();
        appManager.sessionStatusChanged(new AppManager.AppManagerCallback<Session.Status>() {
            @Override
            public void onDataArrived(Session.Status value) {
                if (value == Session.Status.active) {
                    addToast("ההחלפה התתחילה", Toast.LENGTH_SHORT);
                    appManager.refreshTimestampDelta();
                    startTimer(appManager.getSelectedSession().getMillisSet());
                }
                else if(value == Session.Status.terminated) {
                    timer.cancel();
                    appManager.updateMyBalance();
                    appManager.getSelectedSession().setMillisPassed(millisPassed);
                    addToast("ההחלפה הסתיימה", Toast.LENGTH_SHORT);
                    CreateActivityUtil.createHandshakeSummaryActivity(getHandshakeProcessActivity());
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                appManager.finishSession(millisPassed + 1000);
                addToast("The session  was terminated", Toast.LENGTH_SHORT);
                CreateActivityUtil.createHandshakeSummaryActivity(getHandshakeProcessActivity());

            }
        });

    }

    private void setActionText() {
        actionText.setText(appManager.getSelectedSession().getGiveRequest().getUserName() +
                " מעביר\\ה זמן ל " + appManager.getSelectedSession().getTakeRequest().getUserName());
    }

    private void startTimer(long timerStartValue) {
        timer = new CountDownTimer(timerStartValue, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //im the giver
                millisPassed += 1000;
                if (appManager.getSelectedSession().getGiveRequest().getUid().equals(appManager.getCurrentUser().getId())) {
                    long newTimeGiver = appManager.getCurrentUser().getBalance() + 1000;
                    appManager.getCurrentUser().setBalance(newTimeGiver);
                }
                //im the taker
                else {
                    if(appManager.getCurrentUser().getBalance()- 1000 >= 0) {
                        long newTimeTaker = appManager.getCurrentUser().getBalance() - 1000;
                        appManager.getCurrentUser().setBalance(newTimeTaker);
                    }
                    else {
                        appManager.finishSession(millisPassed +1000);
                        timer.cancel();
                        CreateActivityUtil.createHandshakeSummaryActivity(getHandshakeProcessActivity());
                    }
                }
                balanceValue.setText(TimeConvertUtil.convertTime(appManager.getCurrentUser().getBalance()));
                timerValue.setText(TimeConvertUtil.convertTime(millisUntilFinished));

            }

            @Override
            public void onFinish() {
                if (appManager.getSelectedSession().getGiveRequest().getUid().equals(appManager.getCurrentUser().getId())) {
                    long newTimeGiver = appManager.getCurrentUser().getBalance() + 1000;
                    appManager.getCurrentUser().setBalance(newTimeGiver);
                }

                else {
                        long newTimeTaker = appManager.getCurrentUser().getBalance() - 1000;
                        appManager.getCurrentUser().setBalance(newTimeTaker);
                    }
                appManager.finishSession(millisPassed + 1000);
                CreateActivityUtil.createHandshakeSummaryActivity(getHandshakeProcessActivity());
            }
        }.start();

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    public void addToast(String text, int duration) {
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        addToast("לא ניתן לחזור אחורה בעת החלפה",Toast.LENGTH_SHORT);
    }

    private Activity getHandshakeProcessActivity(){
        return this;
    }
}
