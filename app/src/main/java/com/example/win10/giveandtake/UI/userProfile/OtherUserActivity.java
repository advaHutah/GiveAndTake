package com.example.win10.giveandtake.UI.userProfile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class OtherUserActivity extends AppCompatActivity {

    private TextView nameText, balanceText, giveText, takeText;
    private GridView giveTags,takeTags;
    private Button btnPhoneNumber, btnGiveSession,btnTakeSession;
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
//        btnPhoneNumber = (Button)this.findViewById(R.id.btn_otherUserPhone);
        btnGiveSession = (Button)this.findViewById(R.id.btn_give_session);
        btnTakeSession = (Button)this.findViewById(R.id.btn_take_session);
        otherUser = appManager.getOtherUser();

        nameText.setText(otherUser.getFullName());
        balanceText.setText(otherUser.getBalance()+"");

        giveText.setText(otherUser.getMyGiveRequest().getUserInputText());
        takeText.setText(otherUser.getMyTakeRequest().getUserInputText());

        giveTags.setAdapter(new ArrayAdapter<>(this,R.layout.item,otherUser.getMyGiveRequest().getTags()));
        takeTags.setAdapter(new ArrayAdapter<String>(this,R.layout.item,otherUser.getMyTakeRequest().getTags()));


//        btnPhoneNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //todo get phone number
//            }
//        });
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

    private void createHandshakeActivity(Request.RequestType type) {
        Intent handShake = new Intent(this, HandshakeActivity.class);
        handShake.putExtra("type",type.toString());
        startActivity(handShake);
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
