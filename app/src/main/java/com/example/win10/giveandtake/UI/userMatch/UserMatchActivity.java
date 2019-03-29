package com.example.win10.giveandtake.UI.userMatch;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.Logic.TagUserInfo;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.mainScreen.AdapterClickListener;
import com.example.win10.giveandtake.UI.userProfile.OtherUserActivity;

import java.util.ArrayList;

public class UserMatchActivity extends AppCompatActivity implements AdapterClickListener {

    private FragmentManager fragmentManager;
    private ArrayList<TagUserInfo> usersInfoList;
    private GridView matchingUsersGrid;
    private AppManager appManager;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private UserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_match_acivity);
        appManager = AppManager.getInstance();

        initList();

        Intent intent = getIntent();
        boolean isTakeRequest = intent.getBooleanExtra("isTakeRequst", true);
        boolean fromNotification = intent.getBooleanExtra("isfromNotification", false);
        String selectedTag = intent.getStringExtra("tag");


        Request.RequestType otherRequestType = isTakeRequest ? Request.RequestType.GIVE : Request.RequestType.TAKE;
        if (fromNotification) {
            usersInfoList = appManager.getNotificationUsers();
            if (usersInfoList != null)
                displayMatchingUsers();
        } else {
            // get all user that match the tag except current user
            appManager.getMatchUsers(selectedTag, otherRequestType, new AppManager.AppManagerCallback<ArrayList<TagUserInfo>>() {
                @Override
                public void onDataArrived(ArrayList<TagUserInfo> value) {
                    usersInfoList = value;
                    TagUserInfo u = new TagUserInfo("id", "adva", Request.RequestType.TAKE);
                    usersInfoList.add(u);
                    if (usersInfoList != null)
                        displayMatchingUsers();
                }
            });
        }


    }

    private void initList() {
        adapter = new UserListAdapter();
        adapter.setClickListener(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.uv_event_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void itemClicked(View view, int position) {
        String selectedUserId = usersInfoList.get(position).getUid();
        appManager.setOtherUser(selectedUserId, new AppManager.AppManagerCallback<Boolean>() {
            @Override
            public void onDataArrived(Boolean value) {
                if (appManager.getOtherUser() != null) {
                    Intent otherUser = new Intent(getApplication(), OtherUserActivity.class);
                    startActivity(otherUser);
                }
            }
        });
    }

    private void displayMatchingUsers() {
        adapter.addUserItems(usersInfoList);

    }
}
