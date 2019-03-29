package com.example.win10.giveandtake.UI.login;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.mainScreen.MainScreenActivity;
import com.github.loadingview.LoadingDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

// first app activity . includes fragments: login fragment , main screen ,splash screen
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private AppManager appManager;

    private static final int RC_SIGN_IN = 9001;
    public GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    // init for first time login to app
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;
    private SignInButton btnSignInWithGoogle;

    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appManager = AppManager.getInstance();
        sharedPreferences = getSharedPreferences("com.example.win10.giveandtake", MODE_PRIVATE);
        dialog = LoadingDialog.Companion.get(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.serverId))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        appManager.setGoogleSignInClient(mGoogleSignInClient);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnSignInWithGoogle = (com.google.android.gms.common.SignInButton) findViewById(R.id.btn_login_google);

        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //check if it is the firs run , if it is display splash screen
        if (sharedPreferences.getBoolean("firstRun", true)) {
            handleFirstRun();
        } else {
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }
    }

    private void handleFirstRun() {
        editor = sharedPreferences.edit();
        editor.putBoolean("firstRun", false);
        editor.apply();
        startSplashActivity();
    }

    public void startSplashActivity() {
        Intent splashScreenActivity = new Intent(this, SplashScreenActivity.class);
        startActivity(splashScreenActivity);
    }


    public void startMainScreenActivity() {
        Intent mainScreen = new Intent(this, MainScreenActivity.class);
        startActivity(mainScreen);
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            showProgressDialog();
            appManager.userLogedIn(user, new AppManager.AppManagerCallback<Boolean>() {
                @Override
                public void onDataArrived(Boolean value) {
                    hideProgressDialog();
                    //change to user fragment
                    Intent mainScreen = new Intent(getApplication(), MainScreenActivity.class);
                    startActivity(mainScreen);
                }
            });

            //todo delete
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    hideProgressDialog();
//                    Toast.makeText(getApplication(), "Couldn't connect, please try to login again.", Toast.LENGTH_LONG).show();
//                }
//            }, 15000);
        }
    }

    private void hideProgressDialog() {
        if (this.dialog != null)
            this.dialog.hide();
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.loginActivity), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        hideProgressDialog();
                    }
                });
    }

    private void showProgressDialog() {
        if (this.dialog != null)
            dialog.show();
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }
}
