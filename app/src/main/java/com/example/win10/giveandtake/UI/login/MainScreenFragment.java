package com.example.win10.giveandtake.UI.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.win10.giveandtake.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

//fragment that display user profile info and function like give and take request

public class MainScreenFragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;

    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_screen, container, false);


        fragmentManager = getFragmentManager();
        btnLogout = (Button) view.findViewById(R.id.btn_fragment_logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });



        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void signOut() {

        ((LoginActivity)getActivity()).getmGoogleSignInClient().signOut()
                .addOnCompleteListener( getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loginFragment = new LoginFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.loginActivity_frame_container, loginFragment)
                                .commit();
                    }
                });
    }
}
