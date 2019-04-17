package com.example.win10.giveandtake.UI.userProfile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.Logic.User;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.util.CreateActivityUtil;
import com.example.win10.giveandtake.util.GeneralUtil;
import com.example.win10.giveandtake.util.TimeConvertUtil;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class OtherUserActivity extends AppCompatActivity {

    private TextView nameText, balanceText, giveText, takeText, userEmail;
    private TagView giveTags, takeTags;
    private Button btnGiveSession, btnTakeSession;
    private AppManager appManager = AppManager.getInstance();
    private User otherUser;
    private ImageView userImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        nameText = (TextView) this.findViewById(R.id.otherUser_name);
        userEmail = (TextView) this.findViewById(R.id.otherUser_email);
        balanceText = (TextView) this.findViewById(R.id.otherUser_balance);
        giveText = (TextView) this.findViewById(R.id.giveText);
        takeText = (TextView) this.findViewById(R.id.takeText);
        giveTags = (TagView) this.findViewById(R.id.otherUser_giveTags);
        takeTags = (TagView) this.findViewById(R.id.otherUser_TakeTags);
        userImage = (ImageView) this.findViewById(R.id.otherUser_image);

        btnGiveSession = (Button) this.findViewById(R.id.btn_give_session);
        btnTakeSession = (Button) this.findViewById(R.id.btn_take_session);

        otherUser = appManager.getOtherUser();
        if (otherUser != null) {
            GeneralUtil generalUtil = new GeneralUtil();
            generalUtil.setUserImage(otherUser.getPhotoUrl(), userImage);
            nameText.setText(otherUser.getFullName());
            userEmail.setText(otherUser.getEmail());
            balanceText.setText(TimeConvertUtil.convertTime(otherUser.getBalance()));
            giveText.setText(otherUser.getMyGiveRequest().getUserInputText());
            takeText.setText(otherUser.getMyTakeRequest().getUserInputText());

            ArrayList<String> aGiveStringTags = otherUser.getMyGiveRequest().getKeyWords();
            ArrayList<String> aTakeStringTags = otherUser.getMyGiveRequest().getStopWords();
            displayTags(aGiveStringTags, giveTags);
            displayTags(aTakeStringTags, takeTags);
        }

        btnGiveSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create handshake activity
                CreateActivityUtil.createHandshakeSettingsActivity(getOtherUserActivity(), Request.RequestType.GIVE.toString());
            }
        });
        btnTakeSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create handshake activity
                CreateActivityUtil.createHandshakeSettingsActivity(getOtherUserActivity(), Request.RequestType.TAKE.toString());
            }
        });
    }


    private void displayTags(ArrayList<String> aStringTags, TagView tagView) {
        if (aStringTags != null) {
            if(!aStringTags.isEmpty()) {
                for (String text : aStringTags) {
                    Tag newTag = new Tag(text);
                    setTagDesign(newTag);
                    tagView.addTag(newTag);
                }
            }
        }
    }


    private void callOtherUser(String phoneNumber) {
        if (phoneNumber != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }

    private void setTagDesign(Tag tag) {
        //todo check why R.color.tsgColor is not working
        tag.layoutColor=Color.parseColor("#66ccff");
        tag.tagTextSize=15;
        tag.radius=30f;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private Activity getOtherUserActivity() {
        return this;
    }
}
