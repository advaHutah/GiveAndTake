package com.finalproject.giveandtake.UI.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


//todo delete after full test of UI

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private View view;
    private FragmentManager fragmentManager;
    private Button btnTermsOfUse;
    private com.google.android.gms.common.SignInButton btnSignInWithGoogle;
    private ProgressBar spinner;
    private GoogleSignInClient mGoogleSignInClient;
    private LoginActivity parentActivity;

    boolean dataArrived;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private AppManager appManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        fragmentManager = getFragmentManager();

        parentActivity = (LoginActivity) getActivity();
        createGoogleClient();

        spinner = (ProgressBar) view.findViewById(R.id.progressBar1);

        btnTermsOfUse = (Button) view.findViewById(R.id.btn_fragment_terms);
        btnSignInWithGoogle = (com.google.android.gms.common.SignInButton) view.findViewById(R.id.btn_fragment_login_google);

        //buttonActions
        btnTermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //parentActivity.changeToTermsOfUseFragment();
                Intent termOfUse = new Intent(parentActivity,TermsOfUseActivity.class);
                startActivity(termOfUse);
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
        dataArrived=false;
        updateUI(appManager.isUserLoggedIn());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("signIn", "signInResult:failed code=" + e.getStatusCode());
            updateUI(false);
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
                            updateUI(user != null);

                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(getActivity().findViewById(R.id.fragment_login), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(false);
                        }
                    }
                });
    }

    private void updateUI(boolean isLoggedIn) {
        final Fragment that = this;
        if (isLoggedIn) {
            // get user info from DB
            spinner.setVisibility(View.VISIBLE);
            appManager.userLogedIn(mAuth.getCurrentUser(), new AppManager.AppManagerCallback<Boolean>() {
                @Override
                public void onDataArrived(Boolean value) {
                    //change to user fragment
                    dataArrived = true;
                    spinner.setVisibility(View.GONE);
                    parentActivity.startMainScreenActivity();
                }
            });
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (!dataArrived) {
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(parentActivity, "Couldn't connect, please try to login again.", Toast.LENGTH_LONG).show();
                    }
                }
            }, 15000);
        }
    }

    private void createGoogleClient() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.serverId))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(parentActivity, googleSignInOptions);
        appManager.setGoogleSignInClient(mGoogleSignInClient);
    }

    public void addToast(String text, int duration) {
        Context context = parentActivity.getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
