package com.example.win10.giveandtake.UI.userProfile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;

//fragment that display user profile info and function like give and take request

public class MyMatchUsersFragment extends Fragment {
    private static final String TAG = "MyMatchTagsFragment";

    private View view;
    private FragmentManager fragmentManager;


    private AppManager appManager;


    public static MyMatchUsersFragment newInstance(String tag) {
        MyMatchUsersFragment myMatchUsersFragment = new MyMatchUsersFragment();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        myMatchUsersFragment.setArguments(args);
        return myMatchUsersFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_match_users, container, false);

        appManager = AppManager.getInstance();

        String tag = this.getArguments().getString("tag");
        //do something
        return view;
    }

}
