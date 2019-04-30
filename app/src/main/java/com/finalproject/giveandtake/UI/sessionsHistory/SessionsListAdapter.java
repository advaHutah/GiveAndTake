package com.finalproject.giveandtake.UI.sessionsHistory;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.giveandtake.Logic.Session;
import com.finalproject.giveandtake.Logic.TagUserInfo;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.UI.mainScreen.AdapterClickListener;
import com.finalproject.giveandtake.util.TimeConvertUtil;
import com.squareup.okhttp.internal.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class SessionsListAdapter extends RecyclerView.Adapter<SessionsListAdapter.ItemViewHolder> {

    private List<Session> sessionsList;
    private boolean isTake;

    public SessionsListAdapter(boolean isTake) {
        sessionsList = new ArrayList<>();
        this.isTake = isTake;

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardItem;
        ImageView image;
        TextView sessionDescription;
        TextView otherUserName;
        TextView sessionTime;



        public ItemViewHolder(final View itemView) {
            super(itemView);
            cardItem = (CardView) itemView.findViewById(R.id.cv_item);
            image = (ImageView) itemView.findViewById(R.id.user_image_rv);
            sessionDescription = (TextView) itemView.findViewById(R.id.session_description);
            otherUserName = (TextView) itemView.findViewById(R.id.other_user_name);
            sessionTime = (TextView) itemView.findViewById(R.id.sesion_time);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_history_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (getItemCount() > 0) {
            //todo fix image
            holder.image.setImageResource(R.drawable.default_user);
            if(isTake) {
                holder.cardItem.setCardBackgroundColor(Color.RED);
                holder.otherUserName.setText(sessionsList.get(position).getGiveRequest().getUserName());
            }
            else {
                holder.cardItem.setCardBackgroundColor(Color.GREEN);
                holder.otherUserName.setText(sessionsList.get(position).getTakeRequest().getUserName());
            }
            holder.sessionDescription.setText(sessionsList.get(position).getDescription());
            holder.sessionTime.setText(TimeConvertUtil.convertTime(sessionsList.get(position).getSessionTime()));


        }
    }

    @Override
    public int getItemCount() {
        return sessionsList == null ? 0 : sessionsList.size();
    }

    public void addUserItem(Session session) {
        sessionsList.add(session);
        notifyDataSetChanged();
    }

    public void addUserItems(ArrayList<Session> sessionsList) {
        for (Session session : sessionsList) {
            if(session.getStatus() == Session.Status.terminated)
                this.addUserItem(session);
        }
    }

    public Session getEventItem(int position) {
        return sessionsList.get(position);
    }

    public void clearEventList() {
        if (sessionsList != null) {
            sessionsList.clear();
            notifyDataSetChanged();
        }
    }

}
