package com.example.win10.giveandtake.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.User;
import com.example.win10.giveandtake.R;

public class FullUserInfoFragment extends Fragment {

    private View view;
    private TextView nameText, balanceText, emailText, phoneText, genderText;

    private AppManager appManager = AppManager.getInstance();
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_full_user_info, container, false);

        nameText = (TextView) view.findViewById(R.id.user_name_text);
        balanceText = (TextView) view.findViewById(R.id.user_balance_text);
        emailText = (TextView) view.findViewById(R.id.user_email_text);
        phoneText = (TextView) view.findViewById(R.id.user_phone_text);
        genderText = (TextView) view.findViewById(R.id.user_gender_text);

        //todo remove when fix loading user info from DB
        //get current logged user and update info in activity
        currentUser = appManager.getCurrentUser();

        nameText.setText(currentUser.getFullName());
        balanceText.setText(currentUser.getBalance()+"");
        emailText.setText(currentUser.getEmail());
        phoneText.setText(currentUser.getPhoneNumber());
        genderText.setText(currentUser.getGender().toString());
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

}
