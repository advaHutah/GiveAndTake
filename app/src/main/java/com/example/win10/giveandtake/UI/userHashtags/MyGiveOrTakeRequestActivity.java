package com.example.win10.giveandtake.UI.userHashtags;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.util.MyConstants;

import java.util.ArrayList;

public class MyGiveOrTakeRequestActivity extends AppCompatActivity {


    private TextView requestTitle;
    private EditText inputText;
    private String text = "";
    private Button requestBtn;
    private Button findTextBtn;
    private TagView tagGroup;


    private AppManager appManager;
    private ArrayList<String> myTagsString;
    private Request.RequestType requestType;
    private ArrayList<String> existingTags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_give_or_take_request);

        Intent intent = getIntent();
        boolean isTakeRequest = intent.getBooleanExtra(MyConstants.IS_TAKE_REQUEST, true);

        requestTitle = (TextView) findViewById(R.id.myHashtagActivity_title_request_type);
        appManager = AppManager.getInstance();

        inputText = (EditText) findViewById(R.id.request_input_text);
        requestBtn = (Button) findViewById(R.id.make_request_btn);
        findTextBtn = (Button) findViewById(R.id.request_find_text_btn);
        tagGroup = (TagView) findViewById(R.id.GiveOrTakeReq_tag_group);

        setOnDeleteEvent(tagGroup);

        if (isTakeRequest) {
            requestType = Request.RequestType.TAKE;
            requestTitle.setText(R.string.GiveOrTakeReq_discriptionTake);
            text = appManager.getCurrentUser().getMyTakeRequest().getUserInputText();
            existingTags = appManager.getCurrentUser().getMyTakeRequest().getTags();
            if(existingTags!=null && existingTags.isEmpty()){
                showTags(existingTags);
            }

        } else {
            requestTitle.setText(R.string.GiveOrTakeReq_discriptionGive);
            requestType = Request.RequestType.GIVE;
            text = appManager.getCurrentUser().getMyGiveRequest().getUserInputText();
            existingTags = appManager.getCurrentUser().getMyTakeRequest().getTags();
            if(existingTags!=null && existingTags.isEmpty()){
                showTags(existingTags);
            }


        }
        //in case request is already exist
        if (text != null && !text.isEmpty()) {
            inputText.setText(text);
            getTags();
        }

        findTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = inputText.getText().toString().trim();
                if (!text.isEmpty()) {
                    appManager.addRequestNotFinal(text, requestType);
                    getTags();
                }
            }
        });
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text.isEmpty() && !myTagsString.isEmpty()) {
                    appManager.addRequestFinal(myTagsString, requestType);
                    //notify the user that the request has been submitted or change the view
                    addToast(getString(R.string.GiveOrTakeReq_successMsg), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public void addToast(String text, int duration) {
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    private void getTags() {

        appManager.getMyRequestTags(requestType, new AppManager.AppManagerCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                myTagsString = value;
                if (myTagsString != null && !myTagsString.isEmpty()) {
                    showTags(myTagsString);
                    setButtonVisibility(requestBtn, View.VISIBLE);
                } else {
                    addToast(getString(R.string.GiveOrTakeReq_noTagsMsg), Toast.LENGTH_SHORT);
                    setButtonVisibility(requestBtn, View.GONE);
                }

            }
        });
    }

    private void setButtonVisibility(Button button, int state) {
        button.setVisibility(state);
    }


    public void showTags(final ArrayList<String> myTagsString) {
        ArrayList<Tag> myTags = new ArrayList<>();
        for (String text : myTagsString) {
            Tag newTag = new Tag(text);
            setTagDesign(newTag);
            myTags.add(newTag);
        }
        tagGroup.addTags(myTags);
    }


    private void setOnDeleteEvent(TagView tagGroup) {
        //set delete listener
        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {

            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyGiveOrTakeRequestActivity.this);
                builder.setMessage("\"" + tag.getText() + "\" ימחק ");
                builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myTagsString.remove(tag.getText());
                        view.remove(position);
                        Toast.makeText(MyGiveOrTakeRequestActivity.this, "\"" + tag.getText() + "\" deleted", Toast.LENGTH_SHORT).show();
                        if (myTagsString.isEmpty()) {
                            setButtonVisibility(requestBtn, View.GONE);
                        }
                    }
                });
                builder.setNegativeButton("לא", null);
                builder.show();

            }
        });

    }


    private void setTagDesign(Tag tag) {
        //todo check why R.color.tsgColor is not working
        tag.setLayoutColor(R.color.tagsColor);
        tag.setTagTextSize(30);
        tag.setRadius(30f);
        tag.setDeletable(true);
    }
}
