package com.finalproject.giveandtake.UI.login;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.finalproject.giveandtake.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnContinue = (Button) findViewById(R.id.btn_continue_terms);

        //buttonActions
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent termOfUse = new Intent(getApplication(), com.finalproject.giveandtake.UI.login.TermsOfUseActivity.class);
                startActivity(termOfUse);
            }
        });
    }
}
