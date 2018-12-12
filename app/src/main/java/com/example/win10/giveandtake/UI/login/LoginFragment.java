package com.example.win10.giveandtake.UI.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.win10.giveandtake.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class LoginFragment extends Fragment {

    private View view;
    private TermsOfUseFragment termsOfUseFragment;
    private MainScreenFragment mainScreenFragment;
    private FragmentManager fragmentManager;


    private Button btnTermsOfUse;
    private Button btnSignInWithGoogle;
    private static final int RC_SIGN_IN = 9001;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        fragmentManager = getFragmentManager();
        btnTermsOfUse = (Button) view.findViewById(R.id.btn_fragment_terms);
        btnSignInWithGoogle = (Button) view.findViewById(R.id.btn_fragment_login_google);


        //buttonActions
        btnTermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                termsOfUseFragment = new TermsOfUseFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.loginActivity_frame_container, termsOfUseFragment)
                        .commit();
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

    private void signIn() {
        Intent signInIntent = ((LoginActivity) getActivity()).getmGoogleSignInClient().getSignInIntent();
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
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("signIn", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            //save user info in DB

            //change to user fragment
            fragmentManager = getFragmentManager();
            mainScreenFragment = new MainScreenFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.loginActivity_frame_container, mainScreenFragment)
                    .commit();
        } else {
            //TODO display error message that signIn faild
        }
    }
}
