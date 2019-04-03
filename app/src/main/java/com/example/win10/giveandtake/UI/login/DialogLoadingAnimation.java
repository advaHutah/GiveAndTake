package com.example.win10.giveandtake.UI.login;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.MyApplication;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.GTActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by roee on 22/02/2017.
 */
public class DialogLoadingAnimation extends DialogFragment {


    public GoogleSignInClient mGoogleSignInClient;
    boolean withClearBackground = false;
    private boolean shouldRepeat = true;
    private String loadingText = null;
    private SignInButton btnSignInWithGoogle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.serverId))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(MyApplication.getContext(), gso);
        AppManager.getInstance().setGoogleSignInClient(mGoogleSignInClient);

        if (loadingText == null) {
            loadingText = "loading";
        }
        //setStyle(DialogFragment.STYLE_NO_TITLE, R.style.TransparentBackgroundDialogFullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v =  inflater.inflate(R.layout.login_button_fragment, container, false);

//        loadingAnimation = v.findViewById(R.id.dialog_loading_animation);
//        loadingAnimation.setNewSentence(loadingText);
//        LinearLayout linearLayout = v.findViewById(R.id.loading_container);

        setCancelable(false);
//        if (withClearBackground) {
//            loadingProgressBar = v.findViewById(R.id.loading_animation_progress_bar);
//            loadingProgressBar.setVisibility(View.GONE);
//            linearLayout.setBackgroundResource(R.drawable.bg_scenes_alert_dialog);
//        } else {
//            loadingProgressBar = v.findViewById(R.id.loading_animation_progress_bar);
//            loadingProgressBar.setVisibility(View.VISIBLE);
//            linearLayout.setBackgroundResource(R.drawable.bg_loading);
//        }

        btnSignInWithGoogle = (com.google.android.gms.common.SignInButton) v.findViewById(R.id.btn_login_google);

        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GTActivity.RC_SIGN_IN);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // loadingAnimation.startAnimation(shouldRepeat);
    }

    public DialogLoadingAnimation show(FragmentManager fragmentManager) {
        show(fragmentManager, getClass().getSimpleName());
        return this;
    }

    public DialogLoadingAnimation withClearBackground(boolean withClearBackground) {
        this.withClearBackground = withClearBackground;
        return this;
    }

    public DialogLoadingAnimation shouldRepeat(boolean shouldRepeat) {
        this.shouldRepeat = shouldRepeat;
        return this;
    }

    @Override
    public void dismiss() {
        super.dismiss();
      //  if (loadingAnimation != null) loadingAnimation.stopAnimating();
    }

    public DialogLoadingAnimation withText(String loadingText) {
        this.loadingText = loadingText;
   //     if (loadingAnimation != null) loadingAnimation.setNewSentence(loadingText);
        return this;
    }
}
