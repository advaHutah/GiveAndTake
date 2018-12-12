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
    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private Button btnBackToLogin;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_terms_of_use, container, false);

        btnBackToLogin = (Button) view.findViewById(R.id.btn_back_to_login_screen);

        fragmentManager = getFragmentManager();
        //buttonActions
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFragment = new LoginFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.loginActivity_frame_container, loginFragment)
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
