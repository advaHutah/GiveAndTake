package com.example.win10.giveandtake.UI.handshakeSession;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.win10.giveandtake.UI.login.MainScreenFragment;
import com.example.win10.giveandtake.UI.userProfile.UserProfileActivity;
import com.example.win10.giveandtake.util.TimeConvertUtil;

import java.util.concurrent.TimeUnit;


public class HandshakeSummaryFragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private HandshakeActivity parentActivity;
    private AppManager appManager;
    private TextView timerValue, actionText, balanceValue;
    private Button btnOk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_handshake_summary, container, false);
        parentActivity = (HandshakeActivity) getActivity();
        appManager = AppManager.getInstance();
        fragmentManager = getFragmentManager();

        timerValue = (TextView) view.findViewById(R.id.handshakeSummaryFragment_timerValue);
        balanceValue = (TextView) view.findViewById(R.id.handshakeSummaryFragment_balanceValue);
        actionText = (TextView) view.findViewById(R.id.handshakeSummaryFragment_action_description);
        btnOk = (Button) view.findViewById(R.id.btn_handshakeSummaryFragment_ok);

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

        return view;
    }

    private void showThanksDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle("Thanks!!");
        builder.setMessage("You donated to a brighter future");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent main = new Intent(parentActivity,UserProfileActivity.class);
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
        Context context = getActivity().getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


}
