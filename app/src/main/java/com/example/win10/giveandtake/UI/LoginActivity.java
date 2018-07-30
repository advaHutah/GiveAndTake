package com.example.win10.giveandtake.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.win10.giveandtake.DBLogic.GiveAndTakeFirebaseInstanceIDService;
import com.example.win10.giveandtake.DBLogic.GiveAndTakeFirebaseMessagingService;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.User;
import com.example.win10.giveandtake.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private AppManager appManager;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset, btnExplore;
    private String token;
    //private User currentUser;
    //private RestaurantManager restaurantManager = RestaurantManager.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db = database.getReference();

        //define input /buttons
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnExplore = (Button) findViewById(R.id.btn_explore);

        appManager = AppManager.getInstance();
        auth = FirebaseAuth.getInstance();


        //buttons actions
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ExploreActivity.class));

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO check is the user exist
                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = auth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });


            }
        });

    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        //todo remove mark when logout is working
        updateUI(currentUser);
    }
       private void updateUI(FirebaseUser currentUser) {
           if (currentUser != null) {
//            //todo fix loading user info from DB
               appManager.setUserLogin(currentUser.getUid());
               startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
           }
       }


//    private void updateUI(FirebaseUser mycurrentUser) {
//        // Check if user is signed in (non-null) and update UI accordingly.
//        if (mycurrentUser != null) {
//            //todo remove when fix loading user info from DB
//            mAuth = FirebaseAuth.getInstance();
//            mFirebaseDatabase = FirebaseDatabase.getInstance();
//            myRef = mFirebaseDatabase.getReference();
//            userID = mycurrentUser.getUid();
//            myRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//                    showData(dataSnapshot);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//            appManager.setUserLogin(mycurrentUser.getUid());
//            startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
//        }
//    }
//
//    private void showData(DataSnapshot dataSnapshot) {
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//            currentUser = new User();
//            currentUser.setFirstName(ds.child(userID).getValue(User.class).getFirstName());
//            currentUser.setLastName(ds.child(userID).getValue(User.class).getLastName());
//            currentUser.setPhoneNumber(ds.child(userID).getValue(User.class).getPhoneNumber());
//            currentUser.setEmail(ds.child(userID).getValue(User.class).getEmail());
//            currentUser.setBalance(ds.child(userID).getValue(User.class).getBalance());
//            currentUser.setGender(ds.child(userID).getValue(User.class).getGender());
//            appManager.setCurrentUser(currentUser);
//        }
//    }
}
