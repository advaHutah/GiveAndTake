package com.example.win10.giveandtake.UI.userHashtags;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;
import com.example.win10.giveandtake.UI.UserHomeDefultFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TakeRequestFragment extends Fragment {

    private View view;
    private EditText inputText;
    private GridView tagsGrid;
    private String text;
    private Button findText;
    private Button requestBtn;

    private AppManager appManager;
    private ArrayList<String> tags;
    private Set<String> selectedTags;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_take_request, container, false);
        appManager = AppManager.getInstance();
        inputText = (EditText) view.findViewById(R.id.take_request_input_text);
        requestBtn = (Button) view.findViewById(R.id.take_request_btn);
        findText = (Button) view.findViewById(R.id.take_request_find_text_btn);
        tagsGrid = (GridView) view.findViewById(R.id.take_request_tags_result);

        findText.setOnClickListener(new View.OnClickListener() {
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
                if (!text.equals("") &&  !selectedTags.isEmpty())
                    appManager.addTakeRequest(text, new ArrayList<String>(selectedTags));
                //change fragment to defult
                UserHomeDefultFragment userHomeDefultFragment = new UserHomeDefultFragment();
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, userHomeDefultFragment)
//                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
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



