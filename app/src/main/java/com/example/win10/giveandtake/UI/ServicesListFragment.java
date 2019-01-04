package com.example.win10.giveandtake.UI;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.Logic.Session;
import com.example.win10.giveandtake.R;

import java.util.ArrayList;

public class ServicesListFragment extends Fragment {

    private View view;
    private AppManager appManager;
    protected ArrayList<Session> mySessions;
    protected ListView servicesListView;
    private FragmentManager fragmentManager;
    private ServiceInfoFragment serviceInfoFragment;
    ServicesListFragment.ServiceAdapter serviceAdapter;

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
          //  if(mySessions.get(position).getStatus()!= Session.Status.COMPLETED) {
                serviceInfoFragment = new ServiceInfoFragment();
                appManager.setSelectedSession(mySessions.get(position));
                fragmentManager.beginTransaction()
                        .replace(R.id.match_activity_frame, serviceInfoFragment)
                        .commit();
           // }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frament_services_list, container, false);

        appManager = AppManager.getInstance();
        fragmentManager= getFragmentManager();
            mySessions = new ArrayList<Session>(appManager.getCurrentUser().getMyServices().values());
            servicesListView = (ListView) view.findViewById(R.id.service_list_view);
            serviceAdapter = new ServicesListFragment.ServiceAdapter();
            servicesListView.setAdapter(serviceAdapter);
            servicesListView.setOnItemClickListener(onItemClickListener);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    class ServiceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mySessions.size();
        }

        @Override
        public Object getItem(int i) {
            return mySessions.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
//            view = getLayoutInflater().inflate(R.layout.service_layout, null);
//            //initial entry components
//            ImageView icon_req = (ImageView) view.findViewById(R.id.imageView_req_type);
//            TextView other_user_name = (TextView) view.findViewById(R.id.textView_other_user_name);
//            TextView description = (TextView) view.findViewById(R.id.description);
//            TextView status = (TextView) view.findViewById(R.id.status);
//            //if current user is giver
//         //   if (mySessions.get(i).getGiveRequest().getUid().equals(appManager.getCurrentUser().getId())) {
//                //change icon
//                icon_req.setImageResource(R.drawable.out_icon);
//                //change other user name
//                other_user_name.setText(mySessions.get(i).getGiveRequest().getUserName());
//                //set description
//                description.setText(mySessions.get(i).getGiveRequest().getTags().toString());
//                //set status
//                status.setText(mySessions.get(i).getStatus().toString());
//           // } else {
//                //change other user name
//                other_user_name.setText(mySessions.get(i).getTakeRequest().getUserName());
//                //set description
//                description.setText(mySessions.get(i).getTakeRequest().getTags().toString());
//                //set status
//                status.setText(mySessions.get(i).getStatus().toString());
//            }
            return view;
        }
    }
}
