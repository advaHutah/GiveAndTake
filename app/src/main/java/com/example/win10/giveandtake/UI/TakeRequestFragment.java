package com.example.win10.giveandtake.UI;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;

public class TakeRequestFragment extends Fragment {

    private View view;
    private EditText inputText;
    private String text;
    private Button requestBtn;
    private AppManager appManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_take_request, container, false);
        appManager=AppManager.getInstance();
        inputText = (EditText)view.findViewById(R.id.take_request_input_text);
        requestBtn = (Button)view.findViewById(R.id.take_request_btn);

        requestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                text =inputText.getText().toString().trim();
                appManager.addTakeRequest(text);
                showTags(text);
            }
        });


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public String[] showTags(String inputText)
    {

        //TODO
        return null;
    }

    public void findMatch()
    {
        //todo
    }
}


