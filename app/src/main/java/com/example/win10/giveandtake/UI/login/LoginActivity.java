package com.example.win10.giveandtake.UI.login;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;

// first app activity . includes fragments: login fragment , main screen ,splash screen
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String FIRST_RUN_KEY = "firstRun";
    private AppManager appManager;

    private FragmentManager fragmentManager;

    // init for first time login to app
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        appManager = AppManager.getInstance();

        //set view content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("com.example.win10.giveandtake", MODE_PRIVATE);
        loginFragment = new LoginFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //check if it is the firs run , if it is display splash screen
        if (sharedPreferences.getBoolean(FIRST_RUN_KEY, true)) {
            sharedPreferences.edit().putBoolean(FIRST_RUN_KEY, false).apply();
            changeToSplashFragment();
        } else {
            // Check if user is signed in (non-null) and update UI accordingly.
            changeToLoginFragment();
        }
    }

    public void changeToSplashFragment() {
        SplashScreenFragment splashScreenFragment = new SplashScreenFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.loginActivity_frame_container, splashScreenFragment)
                .commit();
    }

    public void changeToMainScreenFragment() {
        MainScreenFragment mainScreenFragment = new MainScreenFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.loginActivity_frame_container, mainScreenFragment)
                .commit();
    }

    public void changeToLoginFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.loginActivity_frame_container, loginFragment)
                .commit();
    }

    public void changeToTermsOfUseFragment() {
        TermsOfUseFragment termsOfUseFragment = new TermsOfUseFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.loginActivity_frame_container, termsOfUseFragment)
                .commit();
    }

}
