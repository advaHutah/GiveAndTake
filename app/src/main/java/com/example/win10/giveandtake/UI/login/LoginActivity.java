package com.example.win10.giveandtake.UI.login;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.win10.giveandtake.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

// first app activity . includes fregmants: login fragment , main screen ,splash screen
public class LoginActivity extends AppCompatActivity {

//    private EditText inputEmail, inputPassword;
//    private FirebaseAuth auth;
//    private AppManager appManager;
//    private ProgressBar progressBar;
//    private Button btnSignup, btnLogin, btnReset, btnExplore;


    private FragmentManager fragmentManager;
    private MainScreenFragment mainScreenFragment;
    private SplashScreenFragment splashScreenFragment;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //set view content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    ;

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        updateUI(account);

    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            fragmentManager = getFragmentManager();
            mainScreenFragment = new MainScreenFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.loginActivity_frame_container, mainScreenFragment)
                    .commit();
        } else {
            //if it's a new user open splash screen fragment
            fragmentManager = getFragmentManager();
            splashScreenFragment = new SplashScreenFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.loginActivity_frame_container, splashScreenFragment)
                    .commit();
        }
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        //define input buttons
//        inputEmail = (EditText) findViewById(R.id.email);
//        inputPassword = (EditText) findViewById(R.id.password);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        btnSignup = (Button) findViewById(R.id.btn_signup);
//        btnLogin = (Button) findViewById(R.id.btn_login);
//        btnReset = (Button) findViewById(R.id.btn_reset_password);
//        btnExplore = (Button) findViewById(R.id.btn_explore);
//
//        appManager = AppManager.getInstance();
//        auth = FirebaseAuth.getInstance();
//
//
//        //buttons actions
//        btnSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
//
//            }
//        });
//
//        btnReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
//            }
//        });
//
//        btnExplore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, ExploreActivity.class));
//
//            }
//        });
//
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String email = inputEmail.getText().toString().trim();
//                String password = inputPassword.getText().toString().trim();
//                if(!email.equals("")&& !password.equals("")) {
//                    auth.signInWithEmailAndPassword(email, password)
//                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        // Sign in success, update UI with the signed-in user's information
//                                        FirebaseUser user = auth.getCurrentUser();
//                                        updateUI(user);
//                                    } else {
//                                        // If sign in fails, display a message to the user.
//                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                                Toast.LENGTH_SHORT).show();
//                                        updateUI(null);
//                                    }
//                                }
//                            });
//                }
//
//            }
//        });
//
//    }

//    public void onStart() {
//        super.onStart();
//        updateUI(auth.getCurrentUser());
//
//    }
//
//    private void updateUI(FirebaseUser user) {
//        if (user !=  null) {
//     //     ProgressDialog.show(this, "login", "loading profile");
//            FirebaseManager.getInstance().getUserDetailFromDB(auth.getCurrentUser().getUid(), new FirebaseManager.FirebaseCallback<User>() {
//                @Override
//                public void onDataArrived(User value) {
//                    appManager.setCurrentUser(value);
//                    startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
//                }
//            });
//        }
//    }
}
