package com.example.win10.giveandtake.UI.tags;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.userMatch.UserMatchActivity;
import com.example.win10.giveandtake.UI.mainScreen.MainScreenActivity;
import com.example.win10.giveandtake.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

public class TagsMatchFragment extends Fragment {
    private View view;
    private AppManager appManager;
    private List<String> myTagsString;
    private TagView tagGroup;
    private TextView noTagsText;
    boolean isTakeRequest;
    private Request.RequestType requestType;


    public static TagsMatchFragment newInstance(boolean isTakeRequst) {
        TagsMatchFragment myMatchTagsFragment = new TagsMatchFragment();
        Bundle args = new Bundle();
        args.putBoolean(MyConstants.IS_TAKE_REQUEST, isTakeRequst);
        myMatchTagsFragment.setArguments(args);
        return myMatchTagsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tags_match, container, false);

        appManager = AppManager.getInstance();

        tagGroup = (TagView) view.findViewById(R.id.fragmentGiveMatch_tag_group);
        myTagsString = new ArrayList<String>();

        requestType = getRequestTypeAccordingToArgs();

        if (requestType == Request.RequestType.GIVE) {
            this.handleGiveTags();
        } else {
            this.handleTakeTags();
        }
        return view;
    }

    private Request.RequestType getRequestTypeAccordingToArgs() {
        isTakeRequest = this.getArguments().getBoolean(MyConstants.IS_TAKE_REQUEST);
        if (isTakeRequest) {
            return Request.RequestType.TAKE;
        } else {
            return Request.RequestType.GIVE;
        }
    }


    private void setOnClickEvent() {
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                Intent userMatchActivity = new Intent(getMainScreenActivity(), UserMatchActivity.class);
                userMatchActivity.putExtra(MyConstants.SELECTED_TAG, tag.getText());
                boolean isTakeRequest = requestType == Request.RequestType.TAKE ? true : false;
                userMatchActivity.putExtra(MyConstants.IS_TAKE_REQUEST, isTakeRequest);
                userMatchActivity.putExtra(MyConstants.IS_FROM_NOTIFICATION, false);
                startActivity(userMatchActivity);
            }
        });
    }

    private void handleTakeTags() {
        appManager.getMyTakeMatchTags(new AppManager.AppManagerCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                handleTags(value);
            }
        });

    }


    private void handleGiveTags() {
        appManager.getMyGiveMatchTags(new AppManager.AppManagerCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                handleTags(value);
            }
        });
    }

    private void handleTags(ArrayList<String> aString) {
        myTagsString = aString;
        ArrayList<Tag> myTags = new ArrayList<>();
        if (myTagsString == null || myTagsString.isEmpty()) {
            noTagsText = view.findViewById(R.id.noTagsText);
            noTagsText.setVisibility(View.VISIBLE);
            noTagsText.setText("לא נמצאו תגיות מתאימות");
        } else {
            for (String text : myTagsString) {
                Tag newTag = new Tag(text);
                setTagDesign(newTag);
                myTags.add(newTag);
            }
            tagGroup.addTags(myTags);
            setOnClickEvent();
        }

    }

    private void setTagDesign(Tag tag) {
        //todo check why R.color.tsgColor is not working
        tag.setLayoutColor(Color.parseColor("#66ccff"));
        tag.setTagTextSize(30);
        tag.setRadius(30f);
    }

    private MainScreenActivity getMainScreenActivity() {
        return (MainScreenActivity) getActivity();
    }
}