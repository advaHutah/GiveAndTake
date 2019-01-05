package com.example.win10.giveandtake.UI.login;

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
import com.example.win10.giveandtake.util.TimeConvertUtil;
import com.example.win10.giveandtake.UI.userProfile.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

//fragment that display user profile info and function like give and take request

public class MainScreenFragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;

    private AppManager appManager;

    private Button btnLogout, btnGiveTake;
    private TextView userNameText, userBalanceText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        appManager = AppManager.getInstance();
        fragmentManager = getFragmentManager();


        userNameText = (TextView) view.findViewById(R.id.main_screen_fragment_user_name);
        userBalanceText = (TextView) view.findViewById(R.id.main_screen_fragment_balance);

        if (appManager.getCurrentUser() != null) {
            userNameText.setText("Hello " + appManager.getCurrentUser().getFullName());
            setUserBalance(appManager.getCurrentUser().getBalance());
        }

        btnLogout = (Button) view.findViewById(R.id.btn_fragment_logout);
        btnGiveTake = (Button) view.findViewById(R.id.main_screen_fragment_give_btn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        btnGiveTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserProfileActivity();
            }
        });

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    private void createUserProfileActivity() {
        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        startActivity(intent);
    }
    private void setUserBalance(long balance) {
        String time = TimeConvertUtil.convertTime(balance);
        userBalanceText.setText(time);
    }

    private void signOut() {
        final LoginActivity parentActivity= (LoginActivity) getActivity();
        appManager.signOut();

        appManager.getGoogleSignInClient().signOut()
                .addOnCompleteListener(parentActivity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        parentActivity.changeToLoginFragment();
                    }
                });
    }
}
