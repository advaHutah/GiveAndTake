package com.example.win10.giveandtake.UI.login;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.User;
import com.example.win10.giveandtake.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// first app activity . includes fragments: login fragment , main screen ,splash screen
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private AppManager appManager;
    private GoogleSignInClient mGoogleSignInClient;

    private FragmentManager fragmentManager;

    // init for first time login to app
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        fragmentManager = getFragmentManager();
        appManager = AppManager.getInstance();
        createGoogleClient();

        //set view content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("com.example.win10.giveandtake", MODE_PRIVATE);
    }

    private void createGoogleClient() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //check if it is the firs run , if it is display splash screen
        if (sharedPreferences.getBoolean("firstRun", true)) {
            editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();
            changeToSplashFragment();
        } else {
            // Check if user is signed in (non-null) and update UI accordingly.
            changeToLoginFragment();
        }
    }

    public void changeToSplashFragment() {
        fragmentManager = getFragmentManager();
        SplashScreenFragment splashScreenFragment = new SplashScreenFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.loginActivity_frame_container, splashScreenFragment)
                .commit();
    }

    public void changeToMainScreenFragment() {
        fragmentManager = getFragmentManager();
        MainScreenFragment mainScreenFragment = new MainScreenFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.loginActivity_frame_container, mainScreenFragment)
                .commit();
    }

    public void changeToLoginFragment() {
        fragmentManager = getFragmentManager();
        LoginFragment loginFragment = new LoginFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.loginActivity_frame_container, loginFragment)
                .commit();
    }

    public void changeToTermsOfUseFragment() {
        fragmentManager = getFragmentManager();
        TermsOfUseFragment termsOfUseFragment = new TermsOfUseFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.loginActivity_frame_container, termsOfUseFragment)
                .commit();
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

}
