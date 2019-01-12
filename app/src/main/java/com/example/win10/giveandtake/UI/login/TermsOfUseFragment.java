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


public class TermsOfUseFragment extends Fragment {

    private View view;
    private Button btnStartLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_terms_of_use, container, false);

        btnStartLogin = (Button) view.findViewById(R.id.btn_start_login_screen);

        //buttonActions
        btnStartLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoginActivity().onBackPressed();
            }
        });

        return view;
    }

    private LoginActivity getLoginActivity() {
        return (LoginActivity) getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
