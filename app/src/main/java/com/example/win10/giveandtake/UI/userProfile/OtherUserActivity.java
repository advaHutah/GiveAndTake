package com.example.win10.giveandtake.UI.userProfile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.Logic.User;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.handshakeSession.HandshakeActivity;
import com.example.win10.giveandtake.util.TimeConvertUtil;

public class OtherUserActivity extends AppCompatActivity {

    private TextView nameText, balanceText, giveText, takeText;
    private GridView giveTags, takeTags;
    private Button btnPhoneNumber, btnGiveSession, btnTakeSession;
    private AppManager appManager = AppManager.getInstance();
    private User otherUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        nameText = (TextView) this.findViewById(R.id.user_name_text);
        balanceText = (TextView) this.findViewById(R.id.user_balance_text);
        giveText = (TextView) this.findViewById(R.id.giveText);
        takeText = (TextView) this.findViewById(R.id.takeText);
        giveTags = (GridView) this.findViewById(R.id.giveTags);
        takeTags = (GridView) this.findViewById(R.id.takeTags);
        btnPhoneNumber = (Button) this.findViewById(R.id.btn_phone);
        btnGiveSession = (Button) this.findViewById(R.id.btn_give_session);
        btnTakeSession = (Button) this.findViewById(R.id.btn_take_session);
        otherUser = appManager.getOtherUser();
        if (otherUser != null) {
            nameText.setText(otherUser.getFullName());
            balanceText.setText(TimeConvertUtil.convertTime(otherUser.getBalance()));
            setPhoneNumberText(otherUser.getPhoneNumber());
            giveText.setText(otherUser.getMyGiveRequest().getUserInputText());
            takeText.setText(otherUser.getMyTakeRequest().getUserInputText());

            giveTags.setAdapter(new ArrayAdapter<>(this, R.layout.item, otherUser.getMyGiveRequest().getTags()));
            takeTags.setAdapter(new ArrayAdapter<String>(this, R.layout.item, otherUser.getMyTakeRequest().getTags()));

            btnPhoneNumber.setEnabled(true);
            btnPhoneNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callOtherUser(appManager.getOtherUser().getPhoneNumber());
                }
            });
        }
        btnGiveSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create handshake activity
                createHandshakeActivity(Request.RequestType.GIVE);
            }
        });
        btnTakeSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create handshake activity
                createHandshakeActivity(Request.RequestType.TAKE);
            }
        });
    }

    private void setPhoneNumberText(String phoneNumberText) {
        if (phoneNumberText != null || !phoneNumberText.equals(""))
            btnPhoneNumber.setText(otherUser.getPhoneNumber());
        else
            btnPhoneNumber.setText("no phone number");

    }

    private void createHandshakeActivity(Request.RequestType type) {
        Intent handShake = new Intent(this, HandshakeActivity.class);
        handShake.putExtra("type", type.toString());
        startActivity(handShake);
    }

    private void callOtherUser(String phoneNumber) {
        if (phoneNumber != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
