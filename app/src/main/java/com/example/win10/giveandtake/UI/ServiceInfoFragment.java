package com.example.win10.giveandtake.UI;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.Session;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;

public class ServiceInfoFragment extends Fragment {

    private View view;
    private AppManager appManager;
    private Session theSession;

    private TextView giverName;
    private TextView takerName;
    private TextView description;
    private Button startServiceBtn;
    private Button endServiceBtn;
    private Button cancelServiceBtn;

    private FragmentManager fragmentManager;
    private ServicesListFragment servicesListFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service, container, false);

        appManager = AppManager.getInstance();
        fragmentManager = getFragmentManager();
        theSession = appManager.getSelectedSession();

        //initial view
        giverName = (TextView) view.findViewById(R.id.giver_user_name_text);
        takerName = (TextView) view.findViewById(R.id.taker_user_name_text);
        description = (TextView) view.findViewById(R.id.service_description_text);
        startServiceBtn = (Button) view.findViewById(R.id.start_service_btn);
        endServiceBtn = (Button) view.findViewById(R.id.end_service_btn);
        cancelServiceBtn = (Button) view.findViewById(R.id.cancel_service_btn);

        //set service info in view
//        giverName.setText(theSession.getGiveRequest().getUserName());
//        takerName.setText(theSession.getTakeRequest().getUserName());
//        description.setText(theSession.getDescription());

        //start service
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start timer
                //todo


            }
        });
        //end service
        endServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calculate total time int minuts
                //todo
                int minuts = 0;
                //update manager that service end
                appManager.serviceEnd(theSession, minuts);
                //change status in the list
                servicesListFragment = new ServicesListFragment();
                //go to service list
                fragmentManager.beginTransaction()
                        .replace(R.id.match_activity_frame, servicesListFragment)
                        .commit();
                //hand shake
                //todo

            }
        });

        cancelServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //app manager remove this service
                appManager.removeService(theSession);
                //change to list
                servicesListFragment = new ServicesListFragment();
                //go to service list
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
