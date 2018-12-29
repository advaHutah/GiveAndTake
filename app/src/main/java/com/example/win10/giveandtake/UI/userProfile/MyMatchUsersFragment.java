package com.example.win10.giveandtake.UI.userProfile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.Logic.TagUserInfo;
import com.example.win10.giveandtake.R;

import java.util.ArrayList;

//fragment that display user profile info and function like give and take request

public class MyMatchUsersFragment extends Fragment {
    private static final String TAG = "MyMatchTagsFragment";

    private View view;
    private FragmentManager fragmentManager;
    private ArrayList<TagUserInfo> usersInfoList ;


    private AppManager appManager;


    public static MyMatchUsersFragment newInstance(String tag,boolean isTakeRequest) {
        MyMatchUsersFragment myMatchUsersFragment = new MyMatchUsersFragment();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        args.putBoolean("isTakeRequest",isTakeRequest);
        myMatchUsersFragment.setArguments(args);
        return myMatchUsersFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_match_users, container, false);

        appManager = AppManager.getInstance();

        String selectedTag = this.getArguments().getString("tag");
        boolean isTakeRequest= this.getArguments().getBoolean("isTakeRequest");
        Request.RequestType otherRequestType = isTakeRequest? Request.RequestType.GIVE: Request.RequestType.TAKE;
        // get all user that match the tag except current user
        appManager.getMatchUsers(selectedTag, otherRequestType, new AppManager.AppManagerCallback<ArrayList<TagUserInfo>>() {
            @Override
            public void onDataArrived(ArrayList<TagUserInfo> value) {
                usersInfoList=value;
            //todo display users match
            }
        });

        // todo : if the user choose name, change to otherUserActivity
        return view;
    }

}
