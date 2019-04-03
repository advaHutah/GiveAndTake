package com.example.win10.giveandtake.UI.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.win10.giveandtake.R;

public class SplashScreenActivity extends AppCompatActivity {

    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        btnContinue = (Button) findViewById(R.id.btn_continue_terms);

        //buttonActions
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent termOfUse = new Intent(getApplication(),TermsOfUseActivity.class);
                startActivity(termOfUse);
            }
        });
    }
}
