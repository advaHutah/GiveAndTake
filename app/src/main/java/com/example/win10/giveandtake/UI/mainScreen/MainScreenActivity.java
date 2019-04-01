package com.example.win10.giveandtake.UI.mainScreen;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;

import com.example.win10.giveandtake.UI.tags.TagsMatchFragment;
import com.example.win10.giveandtake.UI.userHashtags.MyGiveOrTakeRequestActivity;
import com.example.win10.giveandtake.UI.userProfile.UserProfileActivity;
import com.example.win10.giveandtake.util.MyConstants;
import com.example.win10.giveandtake.util.TimeConvertUtil;

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

        if (appManager.getCurrentUser() != null) {
            userNameText.setText("שלום " + appManager.getCurrentUser().getFullName());
            setUserBalance(appManager.getCurrentUser().getBalance());
        }
        userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserProfileActivity();
            }
        });
        giveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGiveRequestActivity();
            }
        });
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTakeRequestActivity();
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
        adapter.addFragment( TagsMatchFragment.newInstance(false), "התאמות 'קח'");
        adapter.addFragment(TagsMatchFragment.newInstance(true), "התאמות 'תן'");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void createGiveRequestActivity() {
        createMyGiveOrTakeActivity(false);
    }

    private void createTakeRequestActivity() {
        createMyGiveOrTakeActivity(true);
    }

    private void createUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void createMyGiveOrTakeActivity(boolean isTakeRequest) {
        Intent myGiveOrTakeRequestActivity = new Intent(this, MyGiveOrTakeRequestActivity.class);
        myGiveOrTakeRequestActivity.putExtra(MyConstants.IS_TAKE_REQUEST,isTakeRequest);
        startActivity(myGiveOrTakeRequestActivity);
    }

    private void setUserBalance(long balance) {
        String time = TimeConvertUtil.convertTime(balance);
        userBalanceText.setText(time);
    }

    public TabAdapter getAdapter() {
        return adapter;
    }

//    private void signOut() {
//        final LoginActivity parentActivity= (LoginActivity) getActivity();
//        appManager.signOut();
//
//        appManager.getGoogleSignInClient().signOut()
//                .addOnCompleteListener(parentActivity, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        parentActivity.changeToLoginFragment();
//                    }
//                });
//    }

    //TODO delete if no needed
}
