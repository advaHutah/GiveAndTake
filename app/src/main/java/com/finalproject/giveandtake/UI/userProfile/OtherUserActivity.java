package com.finalproject.giveandtake.UI.userProfile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.Logic.Request;
import com.finalproject.giveandtake.Logic.User;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.util.CreateActivityUtil;
import com.finalproject.giveandtake.util.GeneralUtil;
import com.finalproject.giveandtake.util.TimeConvertUtil;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import co.lujun.androidtagview.TagContainerLayout;

public class OtherUserActivity extends AppCompatActivity {

    private TextView nameText, balanceText, giveText, takeText;
    private TagContainerLayout giveTags, takeTags;
    private Button btnGiveSession, btnTakeSession;
    private AppManager appManager = AppManager.getInstance();
    private User otherUser;
    private ImageView userImage;
    private RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        nameText = (TextView) this.findViewById(R.id.otherUser_name);
        balanceText = (TextView) this.findViewById(R.id.otherUser_balance);
        giveText = (TextView) this.findViewById(R.id.giveText);
        takeText = (TextView) this.findViewById(R.id.takeText);
        giveTags = (TagContainerLayout) this.findViewById(R.id.otherUser_GiveTags);
        takeTags = (TagContainerLayout) this.findViewById(R.id.otherUser_TakeTags);
        userImage = (ImageView) this.findViewById(R.id.otherUser_image);
        ratingBar = (RatingBar) this.findViewById(R.id.otherUser_rating);

        btnGiveSession = (Button) this.findViewById(R.id.btn_give_session);
        btnTakeSession = (Button) this.findViewById(R.id.btn_take_session);

        otherUser = appManager.getOtherUser();
        if (otherUser != null) {
            GeneralUtil.setUserImage(otherUser.getPhotoUrl(), userImage);
            nameText.setText(otherUser.getFullName());
            ratingBar.setRating(otherUser.getRating());
            balanceText.setText(TimeConvertUtil.convertTime(otherUser.getBalance()));
            if(otherUser.getMyGiveRequest()!= null) {
                giveText.setText(otherUser.getMyGiveRequest().getUserInputText());
                ArrayList<String> aGiveStringTags = otherUser.getMyGiveRequest().getKeyWords();
                displayTags(aGiveStringTags, giveTags);
            }
            if(otherUser.getMyTakeRequest() != null) {
                takeText.setText(otherUser.getMyTakeRequest().getUserInputText());
                ArrayList<String> aTakeStringTags = otherUser.getMyTakeRequest().getKeyWords();
                displayTags(aTakeStringTags, takeTags);
            }


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
        if(otherUser.getMyGiveRequest()==null)
            btnTakeSession.setVisibility(View.INVISIBLE);
        if(otherUser.getMyTakeRequest() ==null)
            btnGiveSession.setVisibility(View.INVISIBLE);


        if(appManager.getCurrentUser() == null){
            btnGiveSession.setVisibility(View.INVISIBLE);
            btnTakeSession.setVisibility(View.INVISIBLE);

        }
    }


    private void displayTags(ArrayList<String> aStringTags, TagContainerLayout tagView) {
        if (aStringTags != null) {
            if(!aStringTags.isEmpty()) {
                for (String text : aStringTags) {
                    tagView.addTag(text);
                }
            }
            tagView.setGravity(Gravity.RIGHT);
        }
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

    private Activity getOtherUserActivity() {
        return this;
    }
}
