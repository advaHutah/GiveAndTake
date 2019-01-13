package com.example.win10.giveandtake.UI.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.DBLogic.GiveAndTakeInstanceIdService;
import com.example.win10.giveandtake.MyApplication;
import com.example.win10.giveandtake.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private static final long FETCH_USER_DATA_TIMEOUT = 12000;

    private View view;
    private TermsOfUseFragment termsOfUseFragment;
    private MainScreenFragment mainScreenFragment;
    private FragmentManager fragmentManager;
    private Button btnTermsOfUse;
    private Button btnSignInWithGoogle;
    private ProgressBar spinner;
    private GoogleSignInClient mGoogleSignInClient;

    @Nullable
    public LoginActivity getLoginActivity() {
        return (LoginActivity) getActivity();
    }

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private AppManager appManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        fragmentManager = getFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        appManager = AppManager.getInstance();
        createGoogleClient();

        spinner = (ProgressBar) view.findViewById(R.id.progressBar1);

        btnTermsOfUse = (Button) view.findViewById(R.id.btn_fragment_terms);
        btnSignInWithGoogle = (Button) view.findViewById(R.id.btn_fragment_login_google);

        //buttonActions
        btnTermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoginActivity().changeToTermsOfUseFragment();
            }
        });
        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

        updateUI();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            spinner.setVisibility(View.VISIBLE);
            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            switch (e.getStatusCode()) {
                case 12500:
                    // Google Play Services version is too low!
                    addToast("Please update your Google Play Services");
                    break;
                case 7:
                    // Network error!
                    addToast("Network error, please try again after your network is fixed.");
                    break;
            }

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI();
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                            GiveAndTakeInstanceIdService service = new GiveAndTakeInstanceIdService();
                            service.onTokenRefresh();
                        } else {
                            GiveAndTakeInstanceIdService service = new GiveAndTakeInstanceIdService();
                            service.onTokenRefresh();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(getActivity().findViewById(R.id.fragment_login), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                });
    }

    private void updateUI() {
        final Fragment that = this;

        if (appManager.isUserLoggedIn()) {
            // get user info from DB
            spinner.setVisibility(View.VISIBLE);
            final Handler timeoutHandler = new Handler();
            final Runnable timeoutHandlerRunnable = new Runnable() {
                public void run() {
                    if (spinner.getVisibility() == View.VISIBLE) {
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(MyApplication.getContext(), "Couldn't connect, please try to login again.", Toast.LENGTH_LONG).show();
                    }
                }
            };
            timeoutHandler.postDelayed(timeoutHandlerRunnable, FETCH_USER_DATA_TIMEOUT);

            appManager.userLoggedIn(mAuth.getCurrentUser(), new AppManager.AppManagerCallback<Boolean>() {
                @Override
                public void onDataArrived(Boolean value) {
                    //change to user fragment
                    timeoutHandler.removeCallbacks(timeoutHandlerRunnable);
                    spinner.setVisibility(View.GONE);
                    getLoginActivity().changeToMainScreenFragment();
                }
            });
        }
    }

    private void createGoogleClient() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getLoginActivity(), googleSignInOptions);
        appManager.setGoogleSignInClient(mGoogleSignInClient);
    }

    public void addToast(String text) {
        addToast(text, Toast.LENGTH_SHORT);
    }

    public void addToast(String text, int duration) {
        Toast toast = Toast.makeText(MyApplication.getContext(), text, duration);
        toast.show();
    }

}
