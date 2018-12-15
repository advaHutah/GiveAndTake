package com.example.win10.giveandtake.UI.userProfile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.userHashtags.HashtagsManagementActivity;

public class UserProfileActivity extends AppCompatActivity {


    private AppManager appManager;

    private Button btnMyHashtags;
    private Button btnMyMatch;
    private Button btnMyPhone;
    private TextView userNameText;
    private TextView userBalanceText;
    private TextView userEmailText;
    private ImageView userImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        //set user FCM token
        //  firebaseManager.updateToken(appManager.getCurrentUser().getId(),FirebaseInstanceId.getInstance().getToken());

        //register current user as listener to match

        //firebaseManager.matchListener(this);

        appManager = AppManager.getInstance();

        userNameText = (TextView) findViewById(R.id.my_profile_name);
        userBalanceText = (TextView) findViewById(R.id.my_profile_balance);
        userEmailText = (TextView) findViewById(R.id.my_profile_email);

        if (appManager.getCurrentUser() != null) {
            userNameText.setText(appManager.getCurrentUser().getFullName());
            userBalanceText.setText(appManager.getCurrentUser().getBalance() + "");
            userEmailText.setText("(" + appManager.getCurrentUser().getEmail() + ")");
            //todo change balace from int to hours and minuts
        }

        btnMyHashtags = (Button) this.findViewById(R.id.btn_my_profile_hashtags);
        btnMyMatch = (Button) this.findViewById(R.id.btn_my_profile_match);
        btnMyPhone = (Button) this.findViewById(R.id.btn_my_profile_phone);
        btnMyHashtags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createHashtagsActivity();
                //TODO create new activity of hashtags
            }
        });
        btnMyMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO create new activity of match
            }
        });
        btnMyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO create call option
            }
        });


        userImage = (ImageView) this.findViewById(R.id.my_profile_image);
       // userImage.setImageResource(appManager.getCurrentUser().getImage());



    }

    private void createHashtagsActivity() {
        Intent intent = new Intent(this, HashtagsManagementActivity.class);
        startActivity(intent);
    }

}
