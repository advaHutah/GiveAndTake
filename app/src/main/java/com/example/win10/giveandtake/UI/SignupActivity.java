package com.example.win10.giveandtake.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {


    private EditText inputEmail, inputPassword, inputFirstName, inputLastName, inputPhoneNumber;
    RadioGroup inputGenderGroup;
    RadioButton inputGenderButton;
    private String gender;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private AppManager appManager = AppManager.getInstance();


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


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //buttons actions
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
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
                final String firstName = inputFirstName.getText().toString().trim();
                final String phone = inputPhoneNumber.getText().toString().trim();
                final String lastName = inputLastName.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(firstName)) {
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

                if (!isValidPhone(phone)) {
                    Toast.makeText(getApplicationContext(), R.string.invalid_phone, Toast.LENGTH_SHORT).show();
                    return;
                }

                //define input buttons - radio button-gender
                inputGenderGroup = (RadioGroup) findViewById(R.id.select_gender_radio);
                int radioButtonID = inputGenderGroup.getCheckedRadioButtonId();
                View radioButton = inputGenderGroup.findViewById(radioButtonID);
                int idx = inputGenderGroup.indexOfChild(radioButton);
                inputGenderButton = (RadioButton) inputGenderGroup.getChildAt(idx);
                gender = inputGenderButton.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, getString(R.string.user_created) + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, getString(R.string.authentication_faild) + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    appManager.createNewUser(auth.getCurrentUser().getUid(), email, firstName, phone, gender);
                                    //TODO open current user profile
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }


    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


}
