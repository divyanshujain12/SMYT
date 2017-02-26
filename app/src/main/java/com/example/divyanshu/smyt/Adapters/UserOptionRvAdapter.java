package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by divyanshuPC on 2/24/2017.
 */

public class UserOptionRvAdapter extends RecyclerView.Adapter<UserOptionRvAdapter.UserOptionHolder> {
    private RecyclerViewClick recyclerViewClick;
    private Context context;

    private int[] userOptionsIconsArray = {R.drawable.ic_settings_videos, R.drawable.ic_settings_challenge, R.drawable.ic_settings_music, R.drawable.ic_settings_followers, R.drawable.ic_settings_application, R.drawable.ic_settings_manage_orders, R.drawable.ic_settings_about_us, R.drawable.ic_settings_contact_us, R.drawable.ic_settings_logout};
    private String[] userOptionsStringArray;

    public UserOptionRvAdapter(Context context, RecyclerViewClick recyclerViewClick) {
        this.context = context;
        this.recyclerViewClick = recyclerViewClick;
        userOptionsStringArray = context.getResources().getStringArray(R.array.user_options_name);
    }

    public class UserOptionHolder extends RecyclerView.ViewHolder {
        private ImageView optionIV;
        private TextView optionNameTV;

        public UserOptionHolder(View itemView) {
            super(itemView);
            optionIV = (ImageView) itemView.findViewById(R.id.optionIV);
            optionNameTV = (TextView) itemView.findViewById(R.id.optionNameTV);
        }
    }

    @Override
    public UserOptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_options_rv_adapter, parent, false);
        return new UserOptionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserOptionHolder holder, int position) {
        holder.optionIV.setImageResource(userOptionsIconsArray[position]);
        holder.optionNameTV.setText(userOptionsStringArray[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userOptionsIconsArray.length;
    }
}
