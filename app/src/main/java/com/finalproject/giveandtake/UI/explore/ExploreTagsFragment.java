package com.finalproject.giveandtake.UI.explore;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.Logic.Request;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.Util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import co.lujun.androidtagview.TagContainerLayout;


public class ExploreTagsFragment extends Fragment {
    private View view;
    private AppManager appManager;
    private List<String> myTagsString;
    private TagContainerLayout tagGroup;
    private TextView noTagsText;
    boolean isTakeRequest;
    private Request.RequestType requestType;
    private Button loadMoreBtn;
    private ProgressBar progressBar;


    public static Fragment newInstance(boolean isTakeRequst) {
        ExploreTagsFragment exploreTagsFragment = new ExploreTagsFragment();
        Bundle args = new Bundle();
        args.putBoolean(MyConstants.IS_TAKE_REQUEST, isTakeRequst);
        exploreTagsFragment.setArguments(args);
        return exploreTagsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore_tags, container, false);

        appManager = AppManager.getInstance();

        tagGroup = (TagContainerLayout) view.findViewById(R.id.fragmentGiveMatch_tag_group);
        myTagsString = new ArrayList<String>();

        requestType = getRequestTypeAccordingToArgs();
        noTagsText = view.findViewById(R.id.noTagsText);
        progressBar = view.findViewById(R.id.explore_progress_bar);
        loadMoreBtn = (Button) view.findViewById(R.id.load_more_btn);
        loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setActivated(true);
                progressBar.setVisibility(View.VISIBLE);
                if (requestType == Request.RequestType.GIVE) {
                    handleGiveTags(false);
                } else {
                    handleTakeTags(false);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (requestType == Request.RequestType.GIVE) {
            this.handleGiveTags(true);
        } else {
            this.handleTakeTags(true);
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
                Intent exploreTagUsersActivity = new Intent(getExploreActivity(), ExploreTagUsersActivity.class);
                exploreTagUsersActivity.putExtra(MyConstants.SELECTED_TAG, text);
                boolean isTakeRequest = requestType == Request.RequestType.TAKE ? true : false;
                exploreTagUsersActivity.putExtra(MyConstants.IS_TAKE_REQUEST, isTakeRequest);
                startActivity(exploreTagUsersActivity);
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

    private void handleTakeTags(boolean isFirstLoad) {
        String lastTag ="";

        if(!isFirstLoad)
            lastTag =myTagsString.get(myTagsString.size()-1);

        appManager.getTagsBulk(Request.RequestType.TAKE, lastTag, new AppManager.AppManagerCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                handleTags(value);
            }
        });
    }




    private void handleGiveTags(boolean isFirstLoad) {
        String lastTag ="";
        if(!isFirstLoad)
            lastTag =myTagsString.get(myTagsString.size()-1);
        appManager.getTagsBulk(Request.RequestType.GIVE,lastTag,new AppManager.AppManagerCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                handleTags(value);

            }
        });
    }

    private void handleTags(ArrayList<String> aString) {
        for (String tag: aString) {
            if(!myTagsString.contains(tag))
                myTagsString.add(tag);
        }
        if (myTagsString == null || myTagsString.isEmpty()) {
            noTagsText.setVisibility(View.VISIBLE);
            noTagsText.setText("לא נמצאו תגיות מתאימות");
        } else {
            tagGroup.removeAllTags();
            for (String text : myTagsString) {
                tagGroup.addTag(text);
                tagGroup.setGravity(Gravity.RIGHT);

            }
            progressBar.setActivated(false);
            progressBar.setVisibility(View.INVISIBLE);

            setOnClickEvent();
            noTagsText.setVisibility(View.GONE);
        }

    }


    private ExploreActivity getExploreActivity() {
        return (ExploreActivity) getActivity();
    }
}