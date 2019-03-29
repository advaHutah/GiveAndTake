package com.example.win10.giveandtake.UI.tags;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.userMatch.UserMatchActivity;
import com.example.win10.giveandtake.UI.mainScreen.MainScreenActivity;

import java.util.ArrayList;
import java.util.List;

public class TagsMatchFragment extends Fragment {
    private View view;
    private AppManager appManager;
    private List<String> myTagsString;
    private TagView tagGroup;
    private MainScreenActivity parentActivity;
    boolean isTakeRequest;
    private Request.RequestType requestType;


    public static TagsMatchFragment newInstance(boolean isTakeRequst) {
        TagsMatchFragment myMatchTagsFragment = new TagsMatchFragment();
        Bundle args = new Bundle();
        args.putBoolean("isTakeRequst", isTakeRequst);
        myMatchTagsFragment.setArguments(args);
        return myMatchTagsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tags_match, container, false);

        appManager = AppManager.getInstance();
        parentActivity = (MainScreenActivity) getActivity();

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
        isTakeRequest = this.getArguments().getBoolean("isTakeRequst");
        if (isTakeRequest) {
            return Request.RequestType.TAKE;
        } else {
            return Request.RequestType.GIVE;
        }
    }

    private void handleTakeTags() {
        appManager.getMyTakeMatchTags(new AppManager.AppManagerCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                myTagsString = value;
                ArrayList<Tag> myTags = new ArrayList<>();
                for (String text : myTagsString) {
                    Tag newTag = new Tag(text);
                    myTags.add(newTag);
                }
                tagGroup.addTags(myTags);
            }
        });

        setOnClickEvent();
    }

    private void setOnClickEvent() {
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                Intent userMatchActivity = new Intent(parentActivity, UserMatchActivity.class);
                userMatchActivity.putExtra("tag", tag.getText());
                boolean isTakeRequest = requestType == Request.RequestType.TAKE ? true : false;
                userMatchActivity.putExtra("isTakeRequst", isTakeRequest);
                userMatchActivity.putExtra("isfromNotification", false);
                startActivity(userMatchActivity);
            }
        });
    }


    private void handleGiveTags() {
        appManager.getMyGiveMatchTags(new AppManager.AppManagerCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                myTagsString = value;
                ArrayList<Tag> myTags = new ArrayList<>();
                for (String text : myTagsString) {
                    Tag newTag = new Tag(text);
                    setTagDesign(newTag);
                    myTags.add(newTag);
                }
//todo remove
                Tag t = new Tag("ssssss");
                setTagDesign(t);
                Tag t2 = new Tag("ssssss2");
                setTagDesign(t2);
                myTags.add(t);
                myTags.add(t2);

                tagGroup.addTags(myTags);
            }

        });
        setOnClickEvent();
    }

    private void setTagDesign(Tag tag) {
        //todo check why R.color.tsgColor is not working
        tag.setLayoutColor(Color.parseColor("#66ccff"));
        tag.setTagTextSize(30);
        tag.setRadius(30f);
    }
}