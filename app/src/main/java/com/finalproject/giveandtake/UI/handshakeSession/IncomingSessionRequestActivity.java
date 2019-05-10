package com.finalproject.giveandtake.UI.handshakeSession;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.Logic.Session;
import com.finalproject.giveandtake.Logic.User;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.util.CreateActivityUtil;
import com.finalproject.giveandtake.util.GeneralUtil;
import com.finalproject.giveandtake.util.TimeConvertUtil;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import co.lujun.androidtagview.TagContainerLayout;

public class IncomingSessionRequestActivity extends AppCompatActivity {

    private TextView nameText, balanceText,descriptionText;
    private Button btnAcceptSession, btnRejectSession;
    private AppManager appManager = AppManager.getInstance();
    private User otherUser;
    private ImageView userImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_session_request);
        //user got a session request
        descriptionText = (TextView) this.findViewById(R.id.incoming_session_request_description);
        userImage = (ImageView) this.findViewById(R.id.incoming_session_request_image);
        nameText = (TextView) this.findViewById(R.id.incoming_session_request_name);
        balanceText = (TextView) this.findViewById(R.id.incoming_session_request_balance);

        btnAcceptSession = (Button) this.findViewById(R.id.btn_accept_session);
        btnRejectSession = (Button) this.findViewById(R.id.btn_reject_session);
        otherUser = appManager.getOtherUser();
        if (otherUser != null) {
            GeneralUtil generalUtil = new GeneralUtil();
            generalUtil.setUserImage(otherUser.getPhotoUrl(), userImage);
            nameText.setText(otherUser.getFullName());
            balanceText.setText(TimeConvertUtil.convertTime(otherUser.getBalance()));
            descriptionText.setText(appManager.getSelectedSession().getDescription());

        }
        btnAcceptSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appManager.updateSessionStatus(Session.Status.accepted);
                CreateActivityUtil.createHandshakeProcessActivity(getOtherUserSessionRequestActivity(),true,false);
            }
        });
        btnRejectSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appManager.updateSessionStatus(Session.Status.rejected);
                CreateActivityUtil.createMainScreenActivity(getOtherUserSessionRequestActivity());

            }
        });

    }

    private Activity getOtherUserSessionRequestActivity() {
        return this;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
