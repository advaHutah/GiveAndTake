package com.example.win10.giveandtake.UI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.win10.giveandtake.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {


    private EditText inputEmail, inputPassword, inputFirstName, inputLastName, inputPhoneNumber;
    RadioGroup inputGenderGroup;
    RadioButton inputGenderButton;
    private String gender;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    // private RestaurantManager restaurantManager = RestaurantManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //define input buttons
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputFirstName = (EditText) findViewById(R.id.first_name_text);
        inputLastName = (EditText) findViewById(R.id.last_name_text);
        inputPhoneNumber = (EditText) findViewById(R.id.phone_text);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        //Todo add birthdate
        //TODO add user image

        //buttons actions
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, SetMeetingActivity.class));
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String firsttName = inputFirstName.getText().toString().trim();
                String lastName = inputLastName.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(firsttName)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_firstName, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lastName)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_lastName, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), R.string.enter_more_then_6_characters, Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO check first and last name validation

                //define input buttons - radio button-gender
                inputGenderGroup = (RadioGroup) findViewById(R.id.select_gender_radio);
                int radioButtonID = inputGenderGroup.getCheckedRadioButtonId();
                View radioButton = inputGenderGroup.findViewById(radioButtonID);
                int idx = inputGenderGroup.indexOfChild(radioButton);
                inputGenderButton = (RadioButton) inputGenderGroup.getChildAt(idx);
                gender = inputGenderButton.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                //create user

                //TODO create User


            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


}
