package com.example.win10.giveandtake.UI.userMatch;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.Logic.TagUserInfo;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.userProfile.OtherUserActivity;

import java.util.ArrayList;

import androidx.annotation.Nullable;

//TODO delete after updating
//fragment that display user profile info and function like give and take request

public class MyMatchUsersFragment extends Fragment {
    private static final String TAG = "MyMatchTagsFragment";

    private View view;
    private FragmentManager fragmentManager;
    private ArrayList<TagUserInfo> usersInfoList ;
    private GridView matchingUsersGrid;
    private String selectedUser;



    private AppManager appManager;


    public static MyMatchUsersFragment newInstance(String tag,boolean isTakeRequest,boolean fromNotification) {
        MyMatchUsersFragment myMatchUsersFragment = new MyMatchUsersFragment();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        args.putBoolean("isTakeRequest",isTakeRequest);
        args.putBoolean("fromNotification",fromNotification);
        myMatchUsersFragment.setArguments(args);
        return myMatchUsersFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_match_users, container, false);

        appManager = AppManager.getInstance();
        matchingUsersGrid = (GridView) view.findViewById(R.id.my_match_users_grid);

        String selectedTag = this.getArguments().getString("tag");
        boolean isTakeRequest= this.getArguments().getBoolean("isTakeRequest");
        boolean fromNotification= this.getArguments().getBoolean("fromNotification");

        Request.RequestType otherRequestType = isTakeRequest? Request.RequestType.GIVE: Request.RequestType.TAKE;
        if(fromNotification) {
            usersInfoList = appManager.getNotificationUsers();
            if(usersInfoList!=null)
                displayMatchingUsers();
        } else {
            // get all user that match the tag except current user
            appManager.getMatchUsers(selectedTag, otherRequestType, new AppManager.AppManagerCallback<ArrayList<TagUserInfo>>() {
                @Override
                public void onDataArrived(ArrayList<TagUserInfo> value) {
                    usersInfoList = value;
                    if(usersInfoList!=null)
                    displayMatchingUsers();
                }
            });
        }

        return view;
    }

    private void displayMatchingUsers() {
            ArrayAdapter arrayadapter = new ArrayAdapter<TagUserInfo>(view.getContext(), R.layout.item, usersInfoList);
            matchingUsersGrid.setAdapter(arrayadapter);
            matchingUsersGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    selectedUser = usersInfoList.get(position).getUid();
                    appManager.setOtherUser(selectedUser, new AppManager.AppManagerCallback<Boolean>() {
                        @Override
                        public void onDataArrived(Boolean value) {
                            if(appManager.getOtherUser()!=null) {
                                Intent otherUser = new Intent(view.getContext(), OtherUserActivity.class);
                                startActivity(otherUser);
                            }
                        }
                    });

                }
            });

    }




}
