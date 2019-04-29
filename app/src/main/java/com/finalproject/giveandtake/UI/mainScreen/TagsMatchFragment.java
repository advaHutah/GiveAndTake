package com.finalproject.giveandtake.UI.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.Logic.Request;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.UI.userMatch.UserMatchActivity;
import com.finalproject.giveandtake.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import co.lujun.androidtagview.TagContainerLayout;


public class TagsMatchFragment extends Fragment {
    private View view;
    private AppManager appManager;
    private List<String> myTagsString;
    private TagContainerLayout tagGroup;
    private TextView noTagsText;
    boolean isTakeRequest;
    private Request.RequestType requestType;


    public static Fragment newInstance(boolean isTakeRequst) {
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

        tagGroup = (TagContainerLayout) view.findViewById(R.id.fragmentGiveMatch_tag_group);
        myTagsString = new ArrayList<String>();

        requestType = getRequestTypeAccordingToArgs();
        noTagsText = view.findViewById(R.id.noTagsText);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (requestType == Request.RequestType.GIVE) {
            this.handleGiveTags();
        } else {
            this.handleTakeTags();
        }
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
        tagGroup.setOnTagClickListener(new co.lujun.androidtagview.TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                Intent userMatchActivity = new Intent(getMainScreenActivity(), UserMatchActivity.class);
                userMatchActivity.putExtra(MyConstants.SELECTED_TAG, text);
                boolean isTakeRequest = requestType == Request.RequestType.TAKE ? true : false;
                userMatchActivity.putExtra(MyConstants.IS_TAKE_REQUEST, isTakeRequest);
                userMatchActivity.putExtra(MyConstants.IS_FROM_NOTIFICATION, false);
                startActivity(userMatchActivity);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

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
        if (myTagsString == null || myTagsString.isEmpty()) {
            noTagsText.setVisibility(View.VISIBLE);
            noTagsText.setText("לא נמצאו תגיות מתאימות");
            tagGroup.removeAllTags();
        } else {
            tagGroup.removeAllTags();
            for (String text : myTagsString) {
                tagGroup.addTag(text);
                tagGroup.setGravity(Gravity.RIGHT);

            }
            setOnClickEvent();
            noTagsText.setVisibility(View.GONE);
        }

    }


    private MainScreenActivity getMainScreenActivity() {
        return (MainScreenActivity) getActivity();
    }
}