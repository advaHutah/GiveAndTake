package com.finalproject.giveandtake.UI.handshakeSession;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.UI.mainScreen.MainScreenActivity;
import com.finalproject.giveandtake.util.CreateActivityUtil;
import com.finalproject.giveandtake.util.GeneralUtil;
import com.finalproject.giveandtake.util.TimeConvertUtil;

import androidx.appcompat.app.AppCompatActivity;

public class HandshakeSummaryActivity extends AppCompatActivity {
    private AppManager appManager;
    private TextView timerValue, actionText, balanceValue;
    private Button btnRating;
    private Button btnOk;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handshake_summary);

        appManager = AppManager.getInstance();

        timerValue = (TextView) findViewById(R.id.handshakeSummaryFragment_timerValue);
        balanceValue = (TextView) findViewById(R.id.handshakeSummaryFragment_balanceValue);
        actionText = (TextView) findViewById(R.id.handshakeSummaryFragment_action_description);
        btnRating = (Button) findViewById(R.id.btn_handshakeSummaryFragment_rating);
        btnOk = (Button) findViewById(R.id.btn_handshakeSummaryFragment_ok);
        if(appManager.getSelectedSession()!=null){
            timerValue.setText(TimeConvertUtil.convertTime(appManager.getSelectedSession().getSessionTime()));
            setActionText();
        }
        balanceValue.setText(TimeConvertUtil.convertTime(appManager.getCurrentUser().getBalance()));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appManager.resetOtherUserAndSession();
                showThanksDialog();
            }
        });

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingDialog();
            }
        });
    }

    private void showThanksDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("תודה!!");
        builder.setMessage("תרמת לעתיד טוב יותר");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        CreateActivityUtil.createMainScreenActivity(getHandshakeSummaryActivity());
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showRatingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("דירוג משתמש");
        builder.setMessage("דרג את "+ appManager.getOtherUser().getFullName());
        builder.setCancelable(true);
        builder.setView(getRatingBarLayout());
        builder.setPositiveButton(
                "דרג",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        float rating = ratingBar.getRating();
                        if(rating > 0f){
                            appManager.rateOtherUser(appManager.getCurrentUser().getId(),appManager.getOtherUser().getId(),rating);
                        }
                        else {
                            GeneralUtil.addToast("הדירוג חייב להיות בטווח הערכים 1-5", Toast.LENGTH_SHORT,getHandshakeSummaryActivity());
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private LinearLayout getRatingBarLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(Gravity.CENTER);
        ratingBar = new RatingBar(this);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(0.5f);
        linearLayout.addView(ratingBar);
        return linearLayout;
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    private void setActionText() {
        actionText.setText(appManager.getSelectedSession().getGiveRequest().getUserName() +
                " העניק\\ה זמן ל " + appManager.getSelectedSession().getTakeRequest().getUserName());
        btnRating.setText("דרג את "+ appManager.getOtherUser().getFullName());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CreateActivityUtil.createMainScreenActivity(getHandshakeSummaryActivity());
    }

    private Activity getHandshakeSummaryActivity(){
        return this;
    }
}
