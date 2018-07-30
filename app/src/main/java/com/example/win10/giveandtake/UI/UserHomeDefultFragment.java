package com.example.win10.giveandtake.UI;

import android.app.Fragment;
import android.app.FragmentManager;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserHomeDefultFragment extends Fragment {

    private View view;
    private TextView nameText, balanceText;
    private Button btnGive, btnTake;

    private AppManager appManager = AppManager.getInstance();
    private String uid;
    private User currentUser;

    private FragmentManager fragmentManager;
    private TakeRequestFragment takeRequestFragment;
    private GiveRequestFragment giveRequestFragment;

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_home_defult, container, false);

        nameText = (TextView) view.findViewById(R.id.user_name_text);
        balanceText = (TextView) view.findViewById(R.id.user_balance_text);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        //get current logged user and se info in activity
        //todo remove when fix loading user info from DB


        //initials buttons
        btnTake = (Button) view.findViewById(R.id.btn_take);
        btnGive = (Button) view.findViewById(R.id.btn_give);

//        nameText.setText(currentUser.getFullName());
  //         balanceText.setText(currentUser.getBalance() + "");
        fragmentManager = getFragmentManager();

        //buttonActions
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeRequestFragment = new TakeRequestFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, takeRequestFragment)
                        .commit();
            }
        });

        btnGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveRequestFragment = new GiveRequestFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, giveRequestFragment)
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
