package com.finalproject.giveandtake.UI.sessionsHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.giveandtake.Logic.AppManager;
import com.finalproject.giveandtake.Logic.Session;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.UI.mainScreen.MainScreenActivity;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HistoryFragment extends Fragment {
    private View view;
    private AppManager appManager;
    private RecyclerView recyclerView;
    private SessionsListAdapter adapter;
    boolean isTakeSessions;



    public static Fragment newInstance(boolean isTakeSessions) {
        HistoryFragment historyFragment = new HistoryFragment();
        historyFragment.isTakeSessions = isTakeSessions;
        return historyFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_session_history, container, false);
        appManager = AppManager.getInstance();
        initList();

        return view;
    }


    private void initList() {
        adapter = new SessionsListAdapter(isTakeSessions);
        recyclerView = (RecyclerView) view.findViewById(R.id.history_items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        appManager.getMySessionHistory(isTakeSessions,new AppManager.AppManagerCallback<ArrayList<Session>>() {
            @Override
            public void onDataArrived(ArrayList<Session> value) {
                adapter.addUserItems(value);
            }
        });
    }

}