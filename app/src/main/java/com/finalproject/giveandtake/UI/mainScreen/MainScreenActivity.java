package com.finalproject.giveandtake.UI.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.Logic.Session;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.UI.tags.TagsMatchFragment;
import com.finalproject.giveandtake.UI.userHashtags.MyGiveOrTakeRequestActivity;
import com.finalproject.giveandtake.util.CreateActivityUtil;
import com.finalproject.giveandtake.util.MyConstants;
import com.finalproject.giveandtake.util.TimeConvertUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class MainScreenActivity extends FragmentActivity {

    private AppManager appManager;

    private TextView userNameText, userBalanceText;
    private Button giveBtn, takeBtn, exploreBtn;
    private ImageButton userProfileBtn;

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        appManager = AppManager.getInstance();

        userNameText = (TextView) findViewById(R.id.main_screen_activity_user_name);
        userBalanceText = (TextView) findViewById(R.id.main_screen_Activity_balance);
        userProfileBtn = (ImageButton) findViewById(R.id.mainScreenActivity_userProfileBtn);
        giveBtn = (Button) findViewById(R.id.mainScreenActivity_giveBtn);
        takeBtn = (Button) findViewById(R.id.mainScreenActivity_takeBtn);
        exploreBtn = (Button) findViewById(R.id.mainScreenActivity_exploreBtn);


        userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appManager.getCurrentUser() != null) {
                    CreateActivityUtil.createUserProfileActivity(getMainScreenActivity(), appManager.getCurrentUser());
                } else {
                    CreateActivityUtil.createLoginActivity(getMainScreenActivity());
                }
            }
        });
        giveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appManager.getCurrentUser() != null) {
                    createGiveRequestActivity();
                }
            }
        });
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appManager.getCurrentUser() != null) {
                    createTakeRequestActivity();
                }
            }
        });
        exploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(TagsMatchFragment.newInstance(false), "התאמות 'קח'");
        adapter.addFragment(TagsMatchFragment.newInstance(true), "התאמות 'תן'");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (appManager.getCurrentUser() != null) {
            userNameText.setText("שלום " + appManager.getCurrentUser().getFullName());
            setUserBalance(appManager.getCurrentUser().getBalance());
            appManager.getMySessionHistory(new AppManager.AppManagerCallback<ArrayList<Session>>() {
                @Override
                public void onDataArrived(ArrayList<Session> value) {

                }
            });
        } else {
            userNameText.setText("שלום אורח");
            setUserBalance(0);
        }
    }

    private void createGiveRequestActivity() {
        createMyGiveOrTakeActivity(false);
    }

    private void createTakeRequestActivity() {
        createMyGiveOrTakeActivity(true);
    }

    private void createMyGiveOrTakeActivity(boolean isTakeRequest) {
        Intent myGiveOrTakeRequestActivity = new Intent(this, MyGiveOrTakeRequestActivity.class);
        myGiveOrTakeRequestActivity.putExtra(MyConstants.IS_TAKE_REQUEST, isTakeRequest);
        startActivity(myGiveOrTakeRequestActivity);
    }

    private void setUserBalance(long balance) {
        String time = TimeConvertUtil.convertTime(balance);
        userBalanceText.setText(time);
    }

    private MainScreenActivity getMainScreenActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
