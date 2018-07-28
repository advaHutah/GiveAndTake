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

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.User;
import com.example.win10.giveandtake.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserHomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private AppManager appManager = AppManager.getInstance();
    private User currentUser;

    private FragmentManager fragmentManager ;
    private UserHomeDefultFragment userHomeDefultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        fragmentManager = getFragmentManager();
        userHomeDefultFragment = new UserHomeDefultFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, userHomeDefultFragment)
                .commit();

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
                        // TODO update the UI based on the item selected
                        switch (menuItem.getItemId()) {
                            case R.id.nav_explore: {
                            }
                            case R.id.nav_full_account_info: {
                                FullUserInfoFragment fullUserInfoFragment = new FullUserInfoFragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame, fullUserInfoFragment)
                                        .commit();
                                break;
                            }

                            case R.id.nav_give_request: {
                            }
                            case R.id.nav_history: {
                            }
                            case R.id.nav_edit_user_info: {
                            }
                            case R.id.nav_connect: {
                            }
                            case R.id.nav_logout: {
                                auth.signOut();
                                startActivity(new Intent(UserHomeActivity.this, LoginActivity.class));
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
}
