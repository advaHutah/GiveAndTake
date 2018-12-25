package com.example.win10.giveandtake.UI.handshakeSession;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.win10.giveandtake.R;


public class HandshakeSettingsFragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private HandshakeProcessFragmentGiver handshakeProcessFragment;


    private Button btnStartProcess;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_handshake_settings, container, false);


        fragmentManager = getFragmentManager();
        btnStartProcess = (Button) view.findViewById(R.id.btn_handshake_start_process);

        this.anableButton(btnStartProcess);

        //buttonActions
        btnStartProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handshakeProcessFragment = new HandshakeProcessFragmentGiver();
                fragmentManager.beginTransaction()
                        .replace(R.id.handshake_frame_container, handshakeProcessFragment)
                        .commit();
            }
        });


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void anableButton(Button theButton)
    {
        theButton.setEnabled(true);
    }


}
