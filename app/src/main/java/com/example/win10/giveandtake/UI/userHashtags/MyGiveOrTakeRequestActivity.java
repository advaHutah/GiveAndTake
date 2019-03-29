package com.example.win10.giveandtake.UI.userHashtags;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MyGiveOrTakeRequestActivity extends AppCompatActivity {


    private TextView requestTitle;
    private EditText inputText;
    private String text = "";
    private GridView tagsGrid;
    private Button requestBtn;
    private Button findTextBtn;

    private AppManager appManager;
    private ArrayList<String> tags;
    private Set<String> selectedTags;
    private Request.RequestType requestType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_give_or_take_request);

        Intent intent = getIntent();
        boolean isTakeRequest = intent.getBooleanExtra("isTakeRequst",true);

        requestTitle = (TextView)findViewById(R.id.myHashtagActivity_title_request_type);
        appManager = AppManager.getInstance();

        inputText = (EditText) findViewById(R.id.request_input_text);
        requestBtn = (Button) findViewById(R.id.make_request_btn);
        findTextBtn = (Button) findViewById(R.id.request_find_text_btn);
        tagsGrid = (GridView) findViewById(R.id.request_tags_result);

        if (isTakeRequest) {
            requestType = Request.RequestType.TAKE;
            requestTitle.setText("What will you take?");
            text = appManager.getCurrentUser().getMyTakeRequest().getUserInputText();

        } else {
            requestTitle.setText("What will you give?");
            requestType = Request.RequestType.GIVE;
            text = appManager.getCurrentUser().getMyGiveRequest().getUserInputText();

        }
        //in case request is already exist
        if (text != null && !text.equals("")) {
            inputText.setText(text);
            getTags();
        }

        findTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = inputText.getText().toString().trim();
                if (!text.equals("")) {
                    appManager.addRequestNotFinal(text, requestType);
                    getTags();
                }
            }
        });
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text.equals("") && !selectedTags.isEmpty()) {
                    appManager.addRequestFinal(selectedTags, requestType);
                    //notify the user that the request has been submitted or change the view
                    addToast("The request was submitted", Toast.LENGTH_SHORT);
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
                tags = value;
                selectedTags = new HashSet<String>(tags);
                showTags(tags);
            }
        });
    }

    public void showTags(final ArrayList<String> tags) {
        if (tags != null) {
            ArrayAdapter arrayadapter = new ArrayAdapter<String>(this, R.layout.item, tags);
            tagsGrid.setAdapter(arrayadapter);
            tagsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    selectedTags.add(tags.get(position));
                }
            });
        } else
            addToast("no tags were founds", Toast.LENGTH_SHORT);
    }
}
