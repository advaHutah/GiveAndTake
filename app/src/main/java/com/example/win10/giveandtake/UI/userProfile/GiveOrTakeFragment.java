package com.example.win10.giveandtake.UI.userProfile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.userHashtags.MyHashtagsFragment;

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
                changeToTagsFragment(false);
            }
        });
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToTagsFragment(true);
            }
        });

        return view;
    }

    private void changeToTagsFragment(boolean isTakeRequst) {
        MyMatchTagsFragment myMatchTagsFragment = MyMatchTagsFragment.newInstance(isTakeRequst);
        fragmentManager.beginTransaction()
                .replace(R.id.myMatchActivity_frame_container, myMatchTagsFragment)
                .commit();
    }
}
