package com.finalproject.giveandtake.UI.explore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.UI.mainScreen.TabAdapter;
import com.finalproject.giveandtake.UI.tags.TagsMatchFragment;
import com.google.android.material.tabs.TabLayout;

public class ExploreActivity extends FragmentActivity {
    private AppManager appManager;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        appManager = AppManager.getInstance();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(ExploreTagsFragment.newInstance(true), "תגיות 'קח'");
        adapter.addFragment(ExploreTagsFragment.newInstance(false), "תגיות 'תן'");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
