package com.example.win10.giveandtake.UI.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.win10.giveandtake.R;


public class SplashScreenFragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private TermsOfUseFragment termsOfUseFragment;


    private Button btnTermsOfUse;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_splash_screen, container, false);


        fragmentManager = getFragmentManager();
        btnTermsOfUse = (Button) view.findViewById(R.id.btn_fragment_terms);

        //buttonActions
        btnTermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                termsOfUseFragment = new TermsOfUseFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.loginActivity_frame_container, termsOfUseFragment)
                        .commit();
            }
        });


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }



}
