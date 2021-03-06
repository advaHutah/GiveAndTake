package com.example.win10.giveandtake.UI.userHashtags;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.login.LoginActivity;
import com.example.win10.giveandtake.UI.login.LoginFragment;
import com.example.win10.giveandtake.UI.userProfile.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

//fragment that display user profile info and function like give and take request

public class GiveOrTakeFragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;

    private AppManager appManager;

    private Button btnGive;
    private Button btnTake;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_give_or_take, container, false);

        appManager = AppManager.getInstance();
        fragmentManager = getFragmentManager();

        btnGive = (Button) view.findViewById(R.id.give_or_take_fragment_give_btn);
        btnTake = (Button) view.findViewById(R.id.give_or_take_fragment_take_btn);
        btnGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToHashtagsFragment(false);
            }
        });
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToHashtagsFragment(true);
            }
        });

        return view;
    }

    private void changeToHashtagsFragment(boolean isTakeRequst) {
        MyHashtagsFragment myHashtagsFragment = MyHashtagsFragment.newInstance(isTakeRequst);
        fragmentManager.beginTransaction()
                .replace(R.id.hashtagsManagementActivity_frame_container, myHashtagsFragment)
                .commit();
    }


}
