package com.example.win10.giveandtake.UI.userHashtags;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
    private Button addTagsBtn;

    private TagView tagGroup;


    private AppManager appManager;
    private ArrayList<String> myTagsString;
    private Request.RequestType requestType;
    private ArrayList<String> existingTags;
    private ArrayList<String> keyWords = new ArrayList<>();
    private ArrayList<String> stopWords =new ArrayList<>();


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
        addTagsBtn = (Button) findViewById(R.id.add_tag_btn);

        tagGroup = (TagView) findViewById(R.id.GiveOrTakeReq_tag_group);

        setOnDeleteEvent(tagGroup);

        if (isTakeRequest) {
            requestType = Request.RequestType.TAKE;
            requestTitle.setText(R.string.GiveOrTakeReq_discriptionTake);
            text = appManager.getCurrentUser().getMyTakeRequest().getUserInputText();
            existingTags = appManager.getCurrentUser().getMyTakeRequest().getSuggestedTags();
            if(existingTags!=null && existingTags.isEmpty()){
                showTags(existingTags);
            }

        } else {
            requestTitle.setText(R.string.GiveOrTakeReq_discriptionGive);
            requestType = Request.RequestType.GIVE;
            text = appManager.getCurrentUser().getMyGiveRequest().getUserInputText();
            existingTags = appManager.getCurrentUser().getMyTakeRequest().getSuggestedTags();
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
                    appManager.addRequestFinal(keyWords,stopWords, requestType);
                    //notify the user that the request has been submitted or change the view
                    addToast(getString(R.string.GiveOrTakeReq_successMsg), Toast.LENGTH_SHORT);
                }
            }
        });

        addTagsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyGiveOrTakeRequestActivity.this);
                builder.setTitle(R.string.GiveOrTakeAddTags);

                // Set up the input
                final EditText input = new EditText(addTagsBtn.getContext());

                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo takes wrong input very weird
                        keyWords.add(input.getText().toString());
                        Tag newTag = new Tag(text);
                        setTagDesign(newTag);
                        tagGroup.addTag(newTag);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();



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
        keyWords = myTagsString;
        tagGroup.addTags(myTags);
    }


    private void setOnDeleteEvent(final TagView tagGroup) {
        //set delete listener
        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {

            @Override
            public void onTagDeleted(TagView view, Tag tag, final int position) {
                //todo figure the colors
                tag.setTagTextColor(R.color.red);
                tag.setLayoutColor(R.color.Yellow_Gold);
                tag.setDeletable(false);
                view.remove(position);
                view.addTag(tag);
                stopWords.add(tag.getText());
                keyWords.remove(tag.getText());

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
