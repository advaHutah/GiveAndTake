package com.example.win10.giveandtake.UI;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.User;
import com.example.win10.giveandtake.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class UserHomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private AppManager appManager = AppManager.getInstance();
    private FirebaseManager firebaseManager = FirebaseManager.getInstance();


    private FragmentManager fragmentManager;
    private UserHomeDefultFragment userHomeDefultFragment;
    private FullUserInfoFragment fullUserInfoFragment;
    private TakeRequestFragment takeRequestFragment;
    private GiveRequestFragment giveRequestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);



        //open userHomeDefultFragment
        fragmentManager = getFragmentManager();
        userHomeDefultFragment = new UserHomeDefultFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, userHomeDefultFragment)
                .commit();

        //set user FSM token
        firebaseManager.updateToken(appManager.getCurrentUser().getId(),FirebaseInstanceId.getInstance().getToken());

        //register current user as listener to match

        //firebaseManager.matchListener(this);

        //define menu toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //define menu slide bar
        mDrawerLayout = findViewById(R.id.userProfileActivity);
        NavigationView navigationView = findViewById(R.id.menu);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(false);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_explore: {
                                startActivity(new Intent(UserHomeActivity.this, ExploreActivity.class));
                                break;
                            }
                            case R.id.nav_full_account_info: {
                                fullUserInfoFragment = new FullUserInfoFragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame, fullUserInfoFragment)
                                        .commit();
                                break;
                            }

                            case R.id.nav_take_request: {
                                takeRequestFragment = new TakeRequestFragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame, takeRequestFragment)
                                        .commit();
                                break;
                            }
                            case R.id.nav_give_request: {
                                giveRequestFragment = new GiveRequestFragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame, giveRequestFragment)
                                        .commit();
                                break;
                            }
                            case R.id.nav_history: {
                                //todo if there is time
                                break;
                            }
                            case R.id.nav_my_services: {
                                if(appManager.getCurrentUser().getMyServices()!=null) {
                                    startActivity(new Intent(UserHomeActivity.this, MyServicesActivity.class));
                                }else
                                    Toast.makeText(UserHomeActivity.this, "You dont have service",
                                            Toast.LENGTH_SHORT).show();
                                break;
                            }

                            case R.id.nav_edit_user_info: {
                                break;
                            }
                            case R.id.nav_connect: {
                                //todo if there is time
                                break;
                            }
                            case R.id.nav_logout: {
                                logout();
                                break;
                            }
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        //appManager.setUserLogout(auth.getCurrentUser().getUid());
        auth.signOut();
        finish();
        //todo close db connection
    }


}
