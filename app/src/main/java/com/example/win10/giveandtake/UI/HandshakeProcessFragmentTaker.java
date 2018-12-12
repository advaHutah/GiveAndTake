package com.example.win10.giveandtake.UI;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.win10.giveandtake.R;


public class HandshakeProcessFragmentTaker extends Fragment {

    private View view;
    private FragmentManager fragmentManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_handshake_process_taker, container, false);


        fragmentManager = getFragmentManager();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
