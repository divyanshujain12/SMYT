package com.example.divyanshu.smyt.Adapters;

/**
 * Created by divyanshu on 8/26/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

public class CategoryUserRvAdapter extends RecyclerView.Adapter<CategoryUserRvAdapter.MyViewHolder> {

    private ArrayList<UserModel> userList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTV, userTimeTV;
        public ImageView userIV;

        public MyViewHolder(View view) {
            super(view);
            userNameTV = (TextView) view.findViewById(R.id.userNameTV);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            userIV = (ImageView) view.findViewById(R.id.userIV);

        }
    }

    public CategoryUserRvAdapter(Context context, ArrayList<UserModel> userList) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_user_rv_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        UserModel userModel = userList.get(position);

        holder.userNameTV.setText(userModel.getName());
        holder.userIV.setImageResource(userModel.getImageResource());
        holder.userTimeTV.setText(userModel.getAgoTime());
        holder.userIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}