package com.finalproject.giveandtake.UI.userMatch;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.giveandtake.Logic.TagUserInfo;
import com.finalproject.giveandtake.R;
import com.finalproject.giveandtake.UI.mainScreen.AdapterClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ItemViewHolder> {

    private static AdapterClickListener clickListener = null;
    private List<TagUserInfo> usersList;

    public UserListAdapter() {
        usersList = new ArrayList<>();
    }

    public UserListAdapter(List<TagUserInfo> usersList) {
        this.usersList = usersList;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardItem;
        ImageView image;
        TextView userName;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            cardItem = (CardView) itemView.findViewById(R.id.cv_item);
            image = (ImageView) itemView.findViewById(R.id.user_image_rv);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            cardItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.itemClicked(v, getAdapterPosition());
            }
        }
    }

    public void setClickListener(AdapterClickListener clickListener) {
        UserListAdapter.clickListener = clickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (getItemCount() > 0) {
            //todo fix image
            holder.image.setImageResource(R.drawable.default_user);
            holder.userName.setText(usersList.get(position).getUserName());
        }
    }

    @Override
    public int getItemCount() {
        return usersList == null ? 0 : usersList.size();
    }

    public void addUserItem(TagUserInfo user) {
        usersList.add(user);
        notifyDataSetChanged();
    }

    public void addUserItems(ArrayList<TagUserInfo> users) {
        for (TagUserInfo u : users) {
            this.addUserItem(u);
        }
    }

    public TagUserInfo getEventItem(int position) {
        return usersList.get(position);
    }

    public void clearEventList() {
        if (usersList != null) {
            usersList.clear();
            notifyDataSetChanged();
        }
    }

}
