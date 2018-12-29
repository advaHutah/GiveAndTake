package com.example.win10.giveandtake.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.User;
import com.example.win10.giveandtake.R;

public class OtherUserActivity extends AppCompatActivity {

    private TextView nameText, balanceText, giveText, takeText;
    private GridView giveTags,takeTags;
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

        otherUser = appManager.getOtherUser();

        nameText.setText(otherUser.getFullName());
        balanceText.setText(otherUser.getBalance()+"");

        giveText.setText(otherUser.getMyGiveRequest().getUserInputText());
        takeText.setText(otherUser.getMyTakeRequest().getUserInputText());

        giveTags.setAdapter(new ArrayAdapter<>(this,R.layout.item,otherUser.getMyGiveRequest().getTags()));
        takeTags.setAdapter(new ArrayAdapter<String>(this,R.layout.item,otherUser.getMyTakeRequest().getTags()));

    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
