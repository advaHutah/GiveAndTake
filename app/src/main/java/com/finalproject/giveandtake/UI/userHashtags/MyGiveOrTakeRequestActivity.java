package com.finalproject.giveandtake.UI.userHashtags;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.Logic.Request;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.Util.GeneralUtil;
import com.finalproject.giveandtake.Util.MyConstants;
import com.spark.submitbutton.SubmitButton;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class MyGiveOrTakeRequestActivity extends AppCompatActivity {


    private TextView requestTitle;
    private EditText inputText;
    private String text = "";
    private SubmitButton saveBtn;
    private Button findTextBtn;
    private Button addTagsBtn;

    private TagContainerLayout tagGroup;
    private ProgressBar progressBar;

    private AppManager appManager;
    private ArrayList<String> tagsToDisplay;
    private Request.RequestType requestType;
    private ArrayList<String> keyWords = new ArrayList<>();
    private ArrayList<String> stopWords =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_give_or_take_request);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        boolean isTakeRequest = intent.getBooleanExtra(MyConstants.IS_TAKE_REQUEST, true);

        requestTitle = (TextView) findViewById(R.id.myHashtagActivity_title_request_type);
        appManager = AppManager.getInstance();

        inputText = (EditText) findViewById(R.id.request_input_text);
        saveBtn = (SubmitButton) findViewById(R.id.make_request_btn);
        findTextBtn = (Button) findViewById(R.id.request_find_text_btn);
        addTagsBtn = (Button) findViewById(R.id.add_tag_btn);
        progressBar = (ProgressBar) findViewById(R.id.myHashtagActivity_progress_bar);
        tagGroup = (TagContainerLayout) findViewById(R.id.GiveOrTakeReq_tag_group);

        setOnClickEvent();

        if (isTakeRequest) {
            requestType = Request.RequestType.TAKE;
            requestTitle.setText(R.string.GiveOrTakeReq_discriptionTake);
            if(appManager.getCurrentUser().getMyTakeRequest()!=null)
                text = appManager.getCurrentUser().getMyTakeRequest().getUserInputText();
        } else {
            requestTitle.setText(R.string.GiveOrTakeReq_discriptionGive);
            requestType = Request.RequestType.GIVE;
            if (appManager.getCurrentUser().getMyGiveRequest()!=null)
                text = appManager.getCurrentUser().getMyGiveRequest().getUserInputText();
        }
        //in case request is already exist
        if (text != null && !text.isEmpty()) {
            inputText.setText(text);
            getKeyWords();
        }

        findTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralUtil.hideKeyboard(getMyGiveOrTakeRequestActivity());
                text = inputText.getText().toString().trim();
                if (!text.isEmpty()) {
                    stopWords.clear();
                    appManager.addRequest(text, requestType);
                    progressBar.setActivated(true);
                    progressBar.setVisibility(View.VISIBLE);
                    getSuggestedTags();
                }
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text.isEmpty() && !tagsToDisplay.isEmpty()) {
                    appManager.setFinalRequest(keyWords,stopWords, requestType);
                    //notify the user that the request has been submitted or change the view
                    GeneralUtil.addToast(getString(R.string.GiveOrTakeReq_successMsg), Toast.LENGTH_SHORT,getMyGiveOrTakeRequestActivity());
                }
            }
        });

        addTagsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyGiveOrTakeRequestActivity.this);
                builder.setTitle(R.string.GiveOrTakeAddTags);

                // Set up the input
                final EditText input = new EditText(builder.getContext());

                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dialogText= input.getText().toString();
                        keyWords.add(dialogText);
                        tagGroup.addTag(dialogText);
                    }
                });
                builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();



            }
        });
    }


    private void getKeyWords() {

        appManager.getMyRequestKeyWords(requestType, new AppManager.AppManagerCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                tagsToDisplay = value;
                if (tagsToDisplay != null && !tagsToDisplay.isEmpty()) {
                    showTags(tagsToDisplay);
                    setButtonVisibility(saveBtn, View.VISIBLE);
                } else {
                    setButtonVisibility(saveBtn, View.GONE);
                }

            }
        });
    }

    private void getSuggestedTags() {

        appManager.getMyRequestSuggestedTags(requestType, new AppManager.AppManagerCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                tagsToDisplay = value;
                if (tagsToDisplay != null && !tagsToDisplay.isEmpty()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.setActivated(false);
                    showTags(tagsToDisplay);
                    setButtonVisibility(saveBtn, View.VISIBLE);
                }

            }
        });
    }

    private void setButtonVisibility(SubmitButton button, int state) {
        button.setVisibility(state);
    }


    public void showTags(final ArrayList<String> tagsToDisplay) {
        tagGroup.removeAllTags();
        for (String text : tagsToDisplay) {
            tagGroup.addTag(text);
        }
        tagGroup.setGravity(Gravity.RIGHT);

        keyWords = tagsToDisplay;
    }


    private void setOnClickEvent() {
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                String tagText = tagGroup.getTagText(position);

                if(stopWords.contains(tagText))
                    stopWords.remove(tagText);
                if(!keyWords.contains(tagText)){
                    keyWords.add(tagText);
                    tagGroup.getTagView(position).setTagBackgroundColor(Color.parseColor("#33cc33"));
                }
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                String tagText = tagGroup.getTagText(position);
                stopWords.add(tagText);
                keyWords.remove(tagText);
                tagGroup.getTagView(position).setTagBackgroundColor(Color.RED);
            }
        });
    }

    private MyGiveOrTakeRequestActivity getMyGiveOrTakeRequestActivity() {
        return this;
    }

}
