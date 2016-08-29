package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.divyanshu.smyt.GlobalClasses.SingletonClass;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class SearchUserRvAdapter  extends RecyclerView.Adapter<SearchUserRvAdapter.MyViewHolder> {

    private ArrayList<UserModel> userList;
    private Context context;
    private ImageLoading imageLoading;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTV, ageAndGenderTV, winsCountTV, followersCountTV, aboutTV;
        public ImageView userIV;

        public MyViewHolder(View view) {
            super(view);
            userNameTV = (TextView) view.findViewById(R.id.userNameTV);
            ageAndGenderTV = (TextView) view.findViewById(R.id.ageAndGenderTV);
            winsCountTV = (TextView) view.findViewById(R.id.winsCountTV);
            followersCountTV = (TextView) view.findViewById(R.id.followersCountTV);
            aboutTV = (TextView) view.findViewById(R.id.aboutTV);
            userIV = (ImageView) view.findViewById(R.id.userIV);

        }
    }

    public SearchUserRvAdapter(Context context) {
        this.userList = SingletonClass.getInstance().userModels;
        this.context = context;
        imageLoading = new ImageLoading(context, 5);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_desc_user_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        UserModel userModel = userList.get(position);

        holder.userNameTV.setText(userModel.getName());
        imageLoading.LoadImage(userModel.getImageUrl(), holder.userIV, null);
        // holder.userIV.setImageResource(userModel.getImageResource());
        holder.ageAndGenderTV.setText(userModel.getAge() + " " + userModel.getGender());
        holder.winsCountTV.setText("Wins: " + userModel.getWins());
        holder.followersCountTV.setText("Followers: " + userModel.getFollowers());
        holder.aboutTV.setText(userModel.getAbout());

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

