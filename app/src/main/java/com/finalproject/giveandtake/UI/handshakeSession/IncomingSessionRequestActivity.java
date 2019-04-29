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

    private TextView nameText,emailText, balanceText, giveText, takeText, descriptionText, timeSet;
    private TagContainerLayout giveTags, takeTags;
    private Button btnPhoneNumber, btnAcceptSession, btnRejectSession;
    private AppManager appManager = AppManager.getInstance();
    private User otherUser;
    private ImageView userImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_session_request);
        //user got a session request
        timeSet = (TextView) this.findViewById(R.id.incoming_session_time);
        descriptionText = (TextView) this.findViewById(R.id.incoming_session_request_description);
        userImage = (ImageView) this.findViewById(R.id.incoming_session_request_image);
        nameText = (TextView) this.findViewById(R.id.incoming_session_request_name);
        emailText = (TextView) this.findViewById(R.id.incoming_session_request_email);
        balanceText = (TextView) this.findViewById(R.id.incoming_session_request_balance);
        giveText = (TextView) this.findViewById(R.id.giveText);
        takeText = (TextView) this.findViewById(R.id.takeText);
        giveTags = (TagContainerLayout) this.findViewById(R.id.incoming_session_request_GiveTags);
        takeTags = (TagContainerLayout) this.findViewById(R.id.incoming_session_request_TakeTags);
//        btnPhoneNumber = (Button)this.findViewById(R.id.btn_otherUserPhone);
        btnAcceptSession = (Button) this.findViewById(R.id.btn_accept_session);
        btnRejectSession = (Button) this.findViewById(R.id.btn_reject_session);
        otherUser = appManager.getOtherUser();
        if (otherUser != null) {
            GeneralUtil generalUtil = new GeneralUtil();
            generalUtil.setUserImage(otherUser.getPhotoUrl(), userImage);
            nameText.setText(otherUser.getFullName());
            emailText.setText(otherUser.getEmail());
            balanceText.setText(TimeConvertUtil.convertTime(otherUser.getBalance()));
            if(otherUser.getMyGiveRequest()!=null) {
                giveText.setText(otherUser.getMyGiveRequest().getUserInputText());
                ArrayList<String> aGiveStringTags = otherUser.getMyGiveRequest().getKeyWords();
                displayTags(aGiveStringTags, giveTags);
            }
            if(otherUser.getMyTakeRequest()!=null) {
                takeText.setText(otherUser.getMyTakeRequest().getUserInputText());
                ArrayList<String> aTakeStringTags = otherUser.getMyTakeRequest().getKeyWords();
                displayTags(aTakeStringTags, takeTags);
            }


            descriptionText.setText(appManager.getSelectedSession().getDescription());
            timeSet.setText(TimeConvertUtil.convertTime(appManager.getSelectedSession().getMillisSet()));


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

            }
        });

    }

    private void displayTags(ArrayList<String> aStringTags, TagContainerLayout tagView) {
        if (aStringTags != null) {
            if(!aStringTags.isEmpty()) {
                for (String text : aStringTags) {
                    tagView.addTag(text);
                }
            }
        }
    }

    private Activity getOtherUserSessionRequestActivity() {
        return this;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
