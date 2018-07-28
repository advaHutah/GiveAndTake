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


public class UserHomeDefultFragment extends Fragment{

    private View view;
    private TextView nameText, balanceText;
    private Button btnGive, btnTake;

    private AppManager appManager = AppManager.getInstance();
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_home_defult, container, false);

        currentUser = appManager.getCurrentUser();
        nameText = (TextView)view.findViewById(R.id.user_name_text);
        balanceText = (TextView) view.findViewById(R.id.user_balance_text);

        //get current logged user and se info in activity

        //todo remove when fix loading user info from DB
        // nameText.setText(currentUser.getFullName());
        // balanceText.setText(currentUser.getBalance());

        //initials buttons
        btnTake = (Button) view.findViewById(R.id.btn_take);
        btnGive = (Button) view.findViewById(R.id.btn_give);

        //buttonActions
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo create request
            }
        });

        btnGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo create request
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
