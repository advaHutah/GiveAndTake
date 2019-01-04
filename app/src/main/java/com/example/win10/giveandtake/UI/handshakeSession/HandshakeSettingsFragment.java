package com.example.win10.giveandtake.UI.handshakeSession;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.Logic.Session;
import com.example.win10.giveandtake.R;

import java.util.UUID;


public class HandshakeSettingsFragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private HandshakeProcessFragment handshakeProcessFragment;
    private HandshakeActivity parentActivity ;
    private AppManager appManager;

    private Button btnStartProcess;
    private TextView step1,step2,step3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_handshake_settings, container, false);
        parentActivity = (HandshakeActivity) getActivity();
        appManager = AppManager.getInstance();

        fragmentManager = getFragmentManager();
        btnStartProcess = (Button) view.findViewById(R.id.btn_handshake_start_process);
        step1 = (TextView) view.findViewById(R.id.handshakeActivity_step1);
        step2 = (TextView) view.findViewById(R.id.handshakeActivity_step2);
        step3 = (TextView) view.findViewById(R.id.handshakeActivity_step3);

        //buttonActions
        btnStartProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        //user gives to other user
        if(parentActivity.getType().equals(Request.RequestType.GIVE.toString())){
            appManager.setSelectedSession(new Session(Session.Status.pending,appManager.getOtherUser().getMyTakeRequest(),
                    appManager.getCurrentUser().getMyGiveRequest(),UUID.randomUUID().toString(),60,Session.SessionInitiator.GIVER));
        }
        //user takes from other user
        else {
            appManager.setSelectedSession( new Session(Session.Status.pending,appManager.getCurrentUser().getMyTakeRequest()
                    ,appManager.getOtherUser().getMyGiveRequest(),UUID.randomUUID().toString(),60,Session.SessionInitiator.TAKER));
        }
        FirebaseManager.getInstance().saveSession(appManager.getSelectedSession());
        //send start request to other user

        changeTextColorToGrey(step1);
        //todo step2: wait for accept from other user
        changeTextColorToGrey(step2);
        // step3: enable start service button
        this.enableButton(btnStartProcess);
    }

    private void changeTextColorToGrey(TextView step) {
        step.setTextColor(Color.GRAY);
    }

    public void enableButton(Button theButton)
    {
        theButton.setEnabled(true);
    }


}
