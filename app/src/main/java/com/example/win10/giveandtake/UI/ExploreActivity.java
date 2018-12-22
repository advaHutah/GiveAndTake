package com.example.win10.giveandtake.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.example.win10.giveandtake.Logic.Service;
import com.example.win10.giveandtake.R;

import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity {

    private FirebaseManager firebaseManager;
    private ArrayList<String> tags;
    private GridView tagsGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        firebaseManager = FirebaseManager.getInstance();
        tagsGrid = (GridView) findViewById(R.id.explore_tags_result);

//        FirebaseManager.getInstance().getAllTagsFromDB(new FirebaseManager.FirebaseCallback<ArrayList<String>>() {
//            @Override
//            public void onDataArrived(ArrayList<String> value) {
//                //todo
//                //set all tags in grid layout
//                showTags(value);
//
//
//            }
//        });

    }

    public void showTags(final ArrayList<String> tags) {
        ArrayAdapter arrayadapter = new ArrayAdapter<String>(this, R.layout.item, tags);
        tagsGrid.setAdapter(arrayadapter);
        tagsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               // selectedTags.add(tags.get(position));
                //todo
            }
        });
    }

}
