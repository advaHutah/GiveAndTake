package com.example.win10.giveandtake.UI.userHashtags;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.UserHomeDefultFragment;
import com.example.win10.giveandtake.UI.login.LoginActivity;
import com.example.win10.giveandtake.UI.login.LoginFragment;
import com.example.win10.giveandtake.UI.userProfile.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//fragment that display user profile info and function like give and take request

public class MyHashtagsFragment extends Fragment {
    private static final String TAG = "MyHashtagsFragment";

    private View view;
    private FragmentManager fragmentManager;

    private TextView requestTitle;
    private EditText inputText;
    private String text;
    private GridView tagsGrid;
    private Button requestBtn;
    private Button findTextBtn;

    private AppManager appManager;
    private ArrayList<String> tags;
    private Set<String> selectedTags;
    private Request.RequestType requestType;

    public static MyHashtagsFragment newInstance(boolean isTakeRequst) {
        MyHashtagsFragment myHashtagsFragment = new MyHashtagsFragment();
        Bundle args = new Bundle();
        args.putBoolean("isTakeRequst", isTakeRequst);
        myHashtagsFragment.setArguments(args);
        return myHashtagsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_hashtags, container, false);
        requestTitle = (TextView) view.findViewById(R.id.my_hashtags_fragment_title_request_type);
        appManager = AppManager.getInstance();

        if (this.getArguments().getBoolean("isTakeRequst")) {
             requestType = Request.RequestType.TAKE;
            requestTitle.setText("What will you take?");
        } else {
            requestTitle.setText("What will you give?");
             requestType = Request.RequestType.GIVE;
        }
        inputText = (EditText) view.findViewById(R.id.request_input_text);
        requestBtn = (Button) view.findViewById(R.id.make_request_btn);
        findTextBtn = (Button) view.findViewById(R.id.request_find_text_btn);
        tagsGrid = (GridView) view.findViewById(R.id.request_tags_result);

        findTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = inputText.getText().toString().trim();
                if (!text.equals("")) {
                    tags = appManager.findTags(text);
                    selectedTags = new HashSet<String>();
                    showTags(tags);
                }
            }
        });
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text.equals("") && !selectedTags.isEmpty())
                    appManager.addRequest(text, new ArrayList<String>(selectedTags),requestType);

                //TODO notify the user that the request has been submitted or change the view
            }
        });
        return view;
    }

    public void showTags(final ArrayList<String> tags) {
        ArrayAdapter arrayadapter = new ArrayAdapter<String>(view.getContext(), R.layout.item, tags);
        tagsGrid.setAdapter(arrayadapter);
        tagsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                selectedTags.add(tags.get(position));
            }
        });
    }

}
