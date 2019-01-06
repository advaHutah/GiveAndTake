package com.example.win10.giveandtake.UI.handshakeSession;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Session;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.util.TimeConvertUtil;

import java.util.concurrent.TimeUnit;


public class HandshakeProcessFragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private HandshakeActivity parentActivity;
    private HandshakeSummaryFragment handshakeSummaryFragment;
    private AppManager appManager;
    private TextView timerValue, actionText, balanceValue;
    private Button stopBtn;
    private CountDownTimer timer;
    private long millisPassed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_handshake_process, container, false);
        timerValue = (TextView) view.findViewById(R.id.timerValue);
        balanceValue = (TextView) view.findViewById(R.id.balanceValue);
        actionText = (TextView) view.findViewById(R.id.handshakeProcessFragment_action_description);
        stopBtn = (Button) view.findViewById(R.id.handshakeProcessFragment_stopBtn);
        millisPassed = 0;
        parentActivity = (HandshakeActivity) getActivity();
        appManager = AppManager.getInstance();
        fragmentManager = getFragmentManager();
        setActionText();
        appManager.sessionStatusChanged(new AppManager.AppManagerCallback<Session.Status>() {
            @Override
            public void onDataArrived(Session.Status value) {
                if (value == Session.Status.active) {
                    addToast("The session Started", Toast.LENGTH_SHORT);
                    startTimer();
                }
                else if(value == Session.Status.terminated) {
                    timer.cancel();
                    appManager.getSelectedSession().setMillisPassed(millisPassed);
                    addToast("The session  was terminated", Toast.LENGTH_SHORT);
                    moveToSummaryFragment();
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                appManager.finishSession(millisPassed + 1000);
                addToast("The session  was terminated", Toast.LENGTH_SHORT);

            }
        });


        return view;
    }

    private void setActionText() {
        actionText.setText(appManager.getSelectedSession().getGiveRequest().getUserName() +
                " Is Giving To " + appManager.getSelectedSession().getTakeRequest().getUserName());
    }

    private void startTimer() {
        timer = new CountDownTimer(appManager.getSelectedSession().getMillisSet(), 1000) {
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
                        appManager.finishSession(millisPassed);
                        timer.cancel();
                        moveToSummaryFragment();
                    }
                }
                balanceValue.setText(TimeConvertUtil.convertTime(appManager.getCurrentUser().getBalance()));
                timerValue.setText(TimeConvertUtil.convertTime(millisUntilFinished));

            }

            @Override
            public void onFinish() {
                appManager.finishSession(millisPassed + 1000);
                moveToSummaryFragment();
            }
        }.start();

    }

    public void moveToSummaryFragment(){
        handshakeSummaryFragment = new HandshakeSummaryFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.handshake_frame_container, handshakeSummaryFragment)
                .commit();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void addToast(String text, int duration) {
        Context context = parentActivity.getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


}
