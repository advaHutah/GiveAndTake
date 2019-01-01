package com.example.win10.giveandtake.UI.userMatch;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.R;

public class MyMatchActivity extends AppCompatActivity {

    private AppManager appManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_match);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //todo not working from notification
        String type = getIntent().getStringExtra("type");
        String tag = getIntent().getStringExtra("tag");
        boolean isTakeRequest;
        if (type != null) {
            //got here from notification
            if (type.equalsIgnoreCase("give"))
                isTakeRequest = false;
            else
                isTakeRequest = true;
            changeToUsersFragment(tag, isTakeRequest, true);
        }
        else{
            // got here from 'my match' button
            changeToGiveOrTakeFragment();
        }
    }

    public void addToast(String text, int duration) {
        Context context = this.getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void changeToGiveOrTakeFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        GiveOrTakeFragment giveOrTakeFragment = new GiveOrTakeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.myMatchActivity_frame_container, giveOrTakeFragment)
                .commit();
    }

    public void changeToUsersFragment(String tag, boolean isTakeRequest, boolean fromNotification) {
        FragmentManager fragmentManager = getFragmentManager();
        MyMatchUsersFragment myMatchUsersFragment = MyMatchUsersFragment.newInstance(tag, isTakeRequest, fromNotification);
        fragmentManager.beginTransaction()
                .replace(R.id.myMatchActivity_frame_container, myMatchUsersFragment)
                .commit();
    }

    public void changeToTagsFragment(boolean isTakeRequst) {
        FragmentManager fragmentManager = getFragmentManager();
        MyMatchTagsFragment myMatchTagsFragment = MyMatchTagsFragment.newInstance(isTakeRequst);
        fragmentManager.beginTransaction()
                .replace(R.id.myMatchActivity_frame_container, myMatchTagsFragment)
                .commit();
    }
}
