package com.example.win10.giveandtake.UI.handshakeSession;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.Logic.Session;
import com.example.win10.giveandtake.Logic.User;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.handshakeSession.HandshakeActivity;
import com.example.win10.giveandtake.UI.handshakeSession.HandshakeProcessFragment;
import com.example.win10.giveandtake.util.CreateActivityUtil;
import com.example.win10.giveandtake.util.TimeConvertUtil;

public class OtherUserSessionRequestActivity extends AppCompatActivity {

    private TextView nameText, balanceText, giveText, takeText, descriptionText, timeSet;
    private GridView giveTags, takeTags;
    private Button btnPhoneNumber, btnAcceptSession, btnRejectSession;
    private AppManager appManager = AppManager.getInstance();
    private User otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_session_request);
        //user got a session request
        timeSet = (TextView) this.findViewById(R.id.sessionTimeSet);
        descriptionText = (TextView) this.findViewById(R.id.sessionDescription);
        nameText = (TextView) this.findViewById(R.id.user_name_text);
        balanceText = (TextView) this.findViewById(R.id.user_balance_text);
        giveText = (TextView) this.findViewById(R.id.giveText);
        takeText = (TextView) this.findViewById(R.id.takeText);
        giveTags = (GridView) this.findViewById(R.id.giveTags);
        takeTags = (GridView) this.findViewById(R.id.takeTags);
//        btnPhoneNumber = (Button)this.findViewById(R.id.btn_otherUserPhone);
        btnAcceptSession = (Button) this.findViewById(R.id.btn_accept_session);
        btnRejectSession = (Button) this.findViewById(R.id.btn_reject_session);
        otherUser = appManager.getOtherUser();
        if (otherUser != null) {
            nameText.setText(otherUser.getFullName());
            balanceText.setText(TimeConvertUtil.convertTime(otherUser.getBalance()));

            giveText.setText(otherUser.getMyGiveRequest().getUserInputText());
            takeText.setText(otherUser.getMyTakeRequest().getUserInputText());

            giveTags.setAdapter(new ArrayAdapter<>(this, R.layout.item, otherUser.getMyGiveRequest().getTags()));
            takeTags.setAdapter(new ArrayAdapter<String>(this, R.layout.item, otherUser.getMyTakeRequest().getTags()));
            descriptionText.setText("Session Description: " + appManager.getSelectedSession().getDescription());
            timeSet.setText("Session Time: " + TimeConvertUtil.convertTime(appManager.getSelectedSession().getMillisSet()));

        }
        btnAcceptSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appManager.updateSessionStatus(Session.Status.accepted);

                CreateActivityUtil.createHandshakeProcessActivity(getOtherUserSessionRequestActivity(),true);
            }
        });
        btnRejectSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appManager.updateSessionStatus(Session.Status.rejected);

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
