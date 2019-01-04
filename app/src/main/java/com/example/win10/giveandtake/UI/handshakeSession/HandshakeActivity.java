package com.example.win10.giveandtake.UI.handshakeSession;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.win10.giveandtake.Logic.Session;
import com.example.win10.giveandtake.R;

public class HandshakeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private HandshakeSettingsFragment handshakeSettingsFragment;
    private HandshakeProcessFragment handshakeProcessFragment;

    String type;
    String sessionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //user started the session
         type = getIntent().getStringExtra("type");

         //open handshake settings fragment
         fragmentManager = getFragmentManager();
         handshakeSettingsFragment = new HandshakeSettingsFragment();
         fragmentManager.beginTransaction()
                 .replace(R.id.handshake_frame_container, handshakeSettingsFragment)
                  .commit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handshake);
    }

    public String getType() {
        return type;
    }

    public String getSessionId() {
        return sessionId;
    }
}
