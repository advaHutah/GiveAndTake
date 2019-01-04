package com.example.win10.giveandtake.UI.handshakeSession;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Session;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.userMatch.MyMatchActivity;


public class HandshakeProcessFragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private HandshakeActivity parentActivity ;
    private AppManager appManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_handshake_process, container, false);
        parentActivity = (HandshakeActivity) getActivity();
        appManager = AppManager.getInstance();
        //got session request
        if(parentActivity.getSessionId() !=null){
        }
        fragmentManager = getFragmentManager();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
