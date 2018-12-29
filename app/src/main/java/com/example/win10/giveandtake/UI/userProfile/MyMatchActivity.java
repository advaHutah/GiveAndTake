package com.example.win10.giveandtake.UI.userProfile;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.userHashtags.*;

import java.util.ArrayList;

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
        changeToGiveOrTakeFragment();

    }


    public void addToast(String text, int duration) {
        Context context = this.getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void changeToGiveOrTakeFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        com.example.win10.giveandtake.UI.userProfile.GiveOrTakeFragment giveOrTakeFragment = new com.example.win10.giveandtake.UI.userProfile.GiveOrTakeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.myMatchActivity_frame_container, giveOrTakeFragment)
                .commit();
    }

    public void changeToUsersFragment(String tag,boolean isTakeRequst) {
        FragmentManager fragmentManager = getFragmentManager();
        MyMatchUsersFragment myMatchUsersFragment = MyMatchUsersFragment.newInstance(tag,isTakeRequst);
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
