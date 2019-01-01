package com.example.win10.giveandtake.UI.userMatch;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;

//fragment that display user profile info and function like give and take request

public class GiveOrTakeFragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;

    private AppManager appManager;

    private Button btnGive;
    private Button btnTake;
    private MyMatchActivity parentActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_give_or_take, container, false);

        appManager = AppManager.getInstance();
        fragmentManager = getFragmentManager();
        parentActivity = (MyMatchActivity) getActivity();

        btnGive = (Button) view.findViewById(R.id.give_or_take_fragment_give_btn);
        btnTake = (Button) view.findViewById(R.id.give_or_take_fragment_take_btn);
        btnGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentActivity.changeToTagsFragment(false);
            }
        });
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentActivity.changeToTagsFragment(true);
            }
        });

        return view;
    }

}
