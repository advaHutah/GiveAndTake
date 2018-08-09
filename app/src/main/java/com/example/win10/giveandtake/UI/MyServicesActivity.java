package com.example.win10.giveandtake.UI;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Service;
import com.example.win10.giveandtake.R;

import java.util.ArrayList;

public class MyServicesActivity extends AppCompatActivity {


    private FragmentManager fragmentManager= getFragmentManager();
    private ServicesListFragment servicesListFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);

        servicesListFragment = new ServicesListFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.match_activity_frame, servicesListFragment)
                .commit();

    }


}
