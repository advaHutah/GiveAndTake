package com.example.win10.giveandtake.UI.handshakeSession;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.Logic.Session;
import com.example.win10.giveandtake.R;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class HandshakeSettingsFragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private HandshakeProcessFragment handshakeProcessFragment;
    private HandshakeActivity parentActivity ;
    private AppManager appManager;

    private Button btnStartProcess, btnSendSession;
    private TextView step1,step2,step3;
    private EditText descriptionText ,mintuesSet;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_handshake_settings, container, false);
        parentActivity = (HandshakeActivity) getActivity();
        appManager = AppManager.getInstance();

        fragmentManager = getFragmentManager();
        descriptionText = (EditText) view.findViewById(R.id.sessionDescription);
        mintuesSet = (EditText) view.findViewById(R.id.sessionTimeSet);

        btnSendSession = (Button) view.findViewById(R.id.btn_handshake_sendSession);
        btnStartProcess = (Button) view.findViewById(R.id.btn_handshake_start_process);
        step1 = (TextView) view.findViewById(R.id.handshakeActivity_step1);
        step2 = (TextView) view.findViewById(R.id.handshakeActivity_step2);
        step3 = (TextView) view.findViewById(R.id.handshakeActivity_step3);

        //buttonActions
        btnSendSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description =descriptionText.getText().toString();
                long timeSet = TimeUnit.MINUTES.toMillis(Integer.parseInt(mintuesSet.getText().toString()));
                //user gives to other user
                if(parentActivity.getType().equals(Request.RequestType.GIVE.toString())){
                    appManager.setSelectedSession(new Session(Session.Status.pending,appManager.getOtherUser().getMyTakeRequest(),
                            appManager.getCurrentUser().getMyGiveRequest(),UUID.randomUUID().toString(),description,timeSet,Session.SessionInitiator.GIVER));
                }
                //user takes from other user
                else {
                    appManager.setSelectedSession( new Session(Session.Status.pending,appManager.getCurrentUser().getMyTakeRequest()
                            ,appManager.getOtherUser().getMyGiveRequest(),UUID.randomUUID().toString(),description,timeSet,Session.SessionInitiator.TAKER));
                }
                //send start request to other user
                appManager.saveSession();
                changeTextColorToGrey(step1);

                //step2: wait for accept from other user
                appManager.sessionStatusChanged(new AppManager.AppManagerCallback<Session.Status>() {
                    @Override
                    public void onDataArrived(Session.Status value) {
                        if(value == Session.Status.accepted) {
                            changeTextColorToGrey(step2);
                            addToast("The session request was accepted", Toast.LENGTH_SHORT);
                            // step3: enable start service button
                            enableButton(btnStartProcess);
                        }
                        else if(value == Session.Status.rejected){
                            addToast("The session request was rejected", Toast.LENGTH_SHORT);
                        }
                    }
                });

            }
        });



        btnStartProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo probably will need an extra validation for starting Timer
                appManager.updateSessionStatus(Session.Status.active);
                handshakeProcessFragment = new HandshakeProcessFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.handshake_frame_container, handshakeProcessFragment)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private void changeTextColorToGrey(TextView step) {
        step.setTextColor(Color.GRAY);
    }

    public void enableButton(Button theButton)
    {
        theButton.setEnabled(true);
    }

    public void addToast(String text, int duration) {
        Context context = getActivity().getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
