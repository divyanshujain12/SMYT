package com.example.divyanshu.smyt.Adapters;

/**
 * Created by divyanshu on 8/26/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.activities.OtherUserProfileActivity;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

public class CategoryUserRvAdapter extends RecyclerView.Adapter<CategoryUserRvAdapter.MyViewHolder> {

    private ArrayList<UserModel> userList;
    private Context context;
    private ImageLoading imageLoading;
    private RecyclerViewClick recyclerViewClick;

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

    public CategoryUserRvAdapter(Context context, ArrayList<UserModel> userList, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.userList = userList;
        this.context = context;
        imageLoading = new ImageLoading(context, 5);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_user_rv_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UserModel userModel = userList.get(position);

        holder.userNameTV.setText(userModel.getFirst_name());
        imageLoading.LoadImage(userModel.getProfileimage(), holder.userIV, null);
        holder.userTimeTV.setText(userModel.getActive_before());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherUserProfileActivity.class);
                intent.putExtra(Constants.CUSTOMER_ID,userModel.getCustomer_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}