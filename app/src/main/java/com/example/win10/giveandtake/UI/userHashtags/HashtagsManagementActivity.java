package com.example.win10.giveandtake.UI.userHashtags;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.login.SplashScreenFragment;

public class HashtagsManagementActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtags_management);

        changeToGiveOrTakeFragment();
    }

    public void changeToGiveOrTakeFragment() {
       FragmentManager fragmentManager = getFragmentManager();
        GiveOrTakeFragment giveOrTakeFragment = new GiveOrTakeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.hashtagsManagementActivity_frame_container, giveOrTakeFragment)
                .commit();


    }
}
