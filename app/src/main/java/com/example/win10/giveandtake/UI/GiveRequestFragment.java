package com.example.win10.giveandtake.UI;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.win10.giveandtake.R;

public class GiveRequestFragment extends Request {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_give_request, container, false);


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}


