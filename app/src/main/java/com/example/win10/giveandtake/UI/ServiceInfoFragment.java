package com.example.win10.giveandtake.UI;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.Service;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;

public class ServiceInfoFragment extends Fragment {

    private View view;
    private AppManager appManager;
    private Service theService;

    private TextView giverName;
    private TextView takerName;
    private TextView description;
    private EditText minuts;
    private Button confirmMeeting_btn;

    private FragmentManager fragmentManager ;
    private ServicesListFragment servicesListFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service, container, false);

        appManager = AppManager.getInstance();
        fragmentManager = getFragmentManager();
        theService = appManager.getSelectedService();

        //initial view
        giverName = (TextView) view.findViewById(R.id.giver_user_name_text);
        takerName = (TextView) view.findViewById(R.id.taker_user_name_text);
        description = (TextView) view.findViewById(R.id.service_description_text);
        minuts = (EditText) view.findViewById(R.id.minut_input_text);
        confirmMeeting_btn = (Button) view.findViewById(R.id.confirm_meeting_btn);

        //set service info in view
        giverName.setText(theService.getGiveRequest().getUserName());
        takerName.setText(theService.getTakeRequest().getUserName());
        description.setText(theService.getDescription());

        //finish meeting
        confirmMeeting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update manager that service end
                appManager.serviceEnd(theService, Integer.parseInt(minuts.getText().toString()));
                servicesListFragment = new ServicesListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.match_activity_frame, servicesListFragment)
                        .commit();

            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
