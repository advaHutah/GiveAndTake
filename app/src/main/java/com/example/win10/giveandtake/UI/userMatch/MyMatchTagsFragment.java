package com.example.win10.giveandtake.UI.userMatch;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Request;
import com.example.win10.giveandtake.R;

import java.util.ArrayList;

//fragment that display user profile info and function like give and take request

public class MyMatchTagsFragment extends Fragment {
    private static final String TAG = "MyMatchTagsFragment";

    private View view;
    private FragmentManager fragmentManager;

    private GridView tagsGrid;
    private ArrayList<String> myTags;
    private Request.RequestType requestType;
    boolean isTakeRequest ;

    private String selectedTag;

    private AppManager appManager;

    private MyMatchActivity parentActivity ;


    public static MyMatchTagsFragment newInstance(boolean isTakeRequst) {
        MyMatchTagsFragment myMatchTagsFragment = new MyMatchTagsFragment();
        Bundle args = new Bundle();
        args.putBoolean("isTakeRequst", isTakeRequst);
        myMatchTagsFragment.setArguments(args);
        return myMatchTagsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_match_tags, container, false);

        appManager = AppManager.getInstance();
        parentActivity = (MyMatchActivity) getActivity();

        tagsGrid = (GridView) view.findViewById(R.id.my_tags);
        myTags = new ArrayList<String>();
        isTakeRequest=this.getArguments().getBoolean("isTakeRequst");
        if (isTakeRequest) {
            requestType = Request.RequestType.TAKE;
        } else {
            requestType = Request.RequestType.GIVE;
        }

        appManager.getTags(requestType, new AppManager.AppManagerCallback<ArrayList<String>>() {
            @Override
            public void onDataArrived(ArrayList<String> value) {
                myTags = value;
                showTags(myTags);
            }
        });
        return view;
    }

    public void showTags(final ArrayList<String> tags) {
        if (tags != null) {
            ArrayAdapter arrayadapter = new ArrayAdapter<String>(view.getContext(), R.layout.item, tags);
            tagsGrid.setAdapter(arrayadapter);
            tagsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    selectedTag = tags.get(position);
                    parentActivity.changeToUsersFragment(selectedTag,isTakeRequest,false);

                }
            });
        } else
            parentActivity.addToast("no tags were founds", Toast.LENGTH_SHORT);
    }

}
