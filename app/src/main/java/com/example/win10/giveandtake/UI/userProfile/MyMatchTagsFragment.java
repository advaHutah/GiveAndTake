package com.example.win10.giveandtake.UI.userProfile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//fragment that display user profile info and function like give and take request

public class MyMatchTagsFragment extends Fragment {
    private static final String TAG = "MyMatchTagsFragment";

    private View view;
    private FragmentManager fragmentManager;



    private AppManager appManager;


    public static MyMatchTagsFragment newInstance(boolean isTakeRequst) {
        MyMatchTagsFragment myMatchTagsFragment = new MyMatchTagsFragment();
        Bundle args = new Bundle();
        args.putBoolean("isTakeRequst", isTakeRequst);
        myMatchTagsFragment.setArguments(args);
        return myMatchTagsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_match_tags, container, false);

        appManager = AppManager.getInstance();


        if (this.getArguments().getBoolean("isTakeRequst")) {
            //do something

        } else {

        }
        return view;
    }

}
