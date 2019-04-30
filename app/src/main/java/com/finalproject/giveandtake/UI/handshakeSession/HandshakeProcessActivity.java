package com.finalproject.giveandtake.UI.handshakeSession;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class HandshakeProcessActivity extends AppCompatActivity {

    private AppManager appManager;
    private TextView timerValue, actionText, balanceValue;
    private Button stopBtn;
    private Timer timer;
    private TimerTask timerTask;
    private Handler timeHandler = new Handler();
    private long millisPassed;
    private boolean sessionRestored;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handshake_process);

        timerValue = (TextView)findViewById(R.id.timerValue);
        balanceValue = (TextView) findViewById(R.id.balanceValue);
        actionText = (TextView)findViewById(R.id.handshakeProcessActivity_action_description);
        stopBtn = (Button) findViewById(R.id.handshakeProcessActivity_stopBtn);
        appManager = AppManager.getInstance();
        sessionRestored = getIntent().getBooleanExtra(MyConstants.SESSION_RESTORED,false);
        checkIfSessionIsRestored();

        setActionText();

        listenToSessionStatusChanges();

        handleStopButtonClick();

    }


    private void checkIfSessionIsRestored() {
        if(sessionRestored) {
             appManager.refreshTimestampDelta();
             millisPassed = GeneralUtil.now() - appManager.getSelectedSession().getStartTimeStamp();

            //im the giver
            if (appManager.getSelectedSession().getGiveRequest().getUid().equals(appManager.getCurrentUser().getId())) {
                appManager.getCurrentUser().setBalance(appManager.getCurrentUser().getBalance()+millisPassed);
            }
            else {
                appManager.getCurrentUser().setBalance(appManager.getCurrentUser().getBalance()- millisPassed);

            }

            startTimer();
        }
        else{
            millisPassed = 0;
        }
    }

    private void listenToSessionStatusChanges() {
        appManager.sessionStatusChanged(new AppManager.AppManagerCallback<Session.Status>() {
            @Override
            public void onDataArrived(Session.Status value) {
                if (value == Session.Status.active && !sessionRestored) {
                    addToast("ההחלפה התתחילה", Toast.LENGTH_SHORT);
                    appManager.refreshTimestampDelta();
                    appManager.getSelectedSession().setStartTimeStamp(GeneralUtil.now());
                    startTimer();
                }
                else if(value == Session.Status.terminated) {
                    timer.cancel();
                    appManager.updateMyBalance();
                    appManager.refreshTimestampDelta();
                    appManager.getSelectedSession().setEndTimeStamp(GeneralUtil.now());
                    addToast("ההחלפה הסתיימה", Toast.LENGTH_SHORT);
                    CreateActivityUtil.createHandshakeSummaryActivity(getHandshakeProcessActivity());
                }
            }
        });
    }



    private void setActionText() {
        actionText.setText(appManager.getSelectedSession().getGiveRequest().getUserName() +
                " מעביר\\ה זמן ל " + appManager.getSelectedSession().getTakeRequest().getUserName());
    }

    private void handleStopButtonClick() {
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                appManager.finishSession();
                addToast("The session  was terminated", Toast.LENGTH_SHORT);
                CreateActivityUtil.createHandshakeSummaryActivity(getHandshakeProcessActivity());

            }
        });
    }


    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            timer.purge();
        }
    }


    private void startTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                timeHandler.post(new Runnable() {
                    public void run(){
                        millisPassed +=1000;
                        //im the giver
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
                                appManager.finishSession();
                                stopTimer();
                                CreateActivityUtil.createHandshakeSummaryActivity(getHandshakeProcessActivity());
                            }
                        }
                        updateTimerValueView();
                        updateBalanceValueView();
                    }
                });
            }
        };

            timer.schedule(timerTask, 0, 1000);

    }


    private void updateTimerValueView(){
        timerValue.setText(TimeConvertUtil.convertTime(millisPassed));
    }


    private void updateBalanceValueView(){
        balanceValue.setText(TimeConvertUtil.convertTime(appManager.getCurrentUser().getBalance()));

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
