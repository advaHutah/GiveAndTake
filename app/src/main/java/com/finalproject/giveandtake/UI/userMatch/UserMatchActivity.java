package com.finalproject.giveandtake.UI.userMatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.Logic.Request;
import com.finalproject.giveandtake.Logic.TagUserInfo;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.UI.mainScreen.AdapterClickListener;
import com.finalproject.giveandtake.UI.userProfile.OtherUserActivity;
import com.finalproject.giveandtake.util.MyConstants;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserMatchActivity extends AppCompatActivity implements AdapterClickListener {

    private ArrayList<TagUserInfo> usersInfoList;
    private AppManager appManager;
    private RecyclerView recyclerView;
    private UserListAdapter adapter;
    private TextView tagDiscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_match_acivity);
        appManager = AppManager.getInstance();

        initList();

        Intent intent = getIntent();
        boolean isTakeRequest = intent.getBooleanExtra(MyConstants.IS_TAKE_REQUEST, true);
        boolean fromNotification = intent.getBooleanExtra(MyConstants.IS_FROM_NOTIFICATION, false);
        String selectedTag = intent.getStringExtra(MyConstants.SELECTED_TAG);

        tagDiscription=(TextView)findViewById(R.id.userMatchActivity_tag);
        tagDiscription.setText("עבור "+"'"+selectedTag+"'");

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
                    if (usersInfoList != null)
                        displayMatchingUsers();
                }
            });
        }


    }

    private void initList() {
        adapter = new UserListAdapter();
        adapter.setClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.uv_event_items);
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
