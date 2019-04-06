package com.example.win10.giveandtake.UI.handshakeSession;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.userProfile.UserProfileActivity;
import com.example.win10.giveandtake.util.TimeConvertUtil;

public class HandshakeSummaryActivity extends AppCompatActivity {
    private AppManager appManager;
    private TextView timerValue, actionText, balanceValue;
    private Button btnOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handshake_summary);

        appManager = AppManager.getInstance();

        timerValue = (TextView) findViewById(R.id.handshakeSummaryFragment_timerValue);
        balanceValue = (TextView) findViewById(R.id.handshakeSummaryFragment_balanceValue);
        actionText = (TextView) findViewById(R.id.handshakeSummaryFragment_action_description);
        btnOk = (Button) findViewById(R.id.btn_handshakeSummaryFragment_ok);

        timerValue.setText(TimeConvertUtil.convertTime(appManager.getSelectedSession().getMillisPassed()));
        setActionText();
        balanceValue.setText(TimeConvertUtil.convertTime(appManager.getCurrentUser().getBalance()));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appManager.resetOtherUserAndSession();
                showThanksDialog();
            }
        });
    }

    private void showThanksDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thanks!!");
        builder.setMessage("You donated to a brighter future");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent main = new Intent(getHandshakeSummaryActivity(),UserProfileActivity.class);
                        startActivity(main);
                    }
                });



        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    private void setActionText() {
        actionText.setText(appManager.getSelectedSession().getGiveRequest().getUserName() +
                " Gived " + appManager.getSelectedSession().getTakeRequest().getUserName());
    }

    public void addToast(String text, int duration) {
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    private Activity getHandshakeSummaryActivity(){
        return this;
    }
}
