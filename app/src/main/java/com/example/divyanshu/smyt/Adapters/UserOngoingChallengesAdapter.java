package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.divyanshu.smyt.GlobalClasses.SingletonClass;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 9/2/2016.
 */
public class UserOngoingChallengesAdapter extends RecyclerView.Adapter<UserOngoingChallengesAdapter.MyViewHolder> implements View.OnClickListener {


    private ArrayList<UserModel> userList;
    private Context context;
    private ImageLoading imageLoading;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        ImageView moreIV;
        TextView genreNameTV;
        TextView challengeTypeTV;
        TextView roundsCountTV;
        TextView challengeTimeTV;
        TextView declineTV;
        TextView acceptTV;


        public MyViewHolder(View view) {
            super(view);
            titleTV = (TextView) view.findViewById(R.id.titleTV);
            moreIV = (ImageView) view.findViewById(R.id.moreIV);
            genreNameTV = (TextView) view.findViewById(R.id.genreNameTV);
            challengeTypeTV = (TextView) view.findViewById(R.id.challengeTypeTV);
            roundsCountTV = (TextView) view.findViewById(R.id.roundsCountTV);
            challengeTimeTV = (TextView) view.findViewById(R.id.challengeTimeTV);
            declineTV = (TextView) view.findViewById(R.id.declineTV);
            acceptTV = (TextView) view.findViewById(R.id.acceptTV);
            declineTV.setOnClickListener(UserOngoingChallengesAdapter.this);
            acceptTV.setOnClickListener(UserOngoingChallengesAdapter.this);
            view.setOnClickListener(UserOngoingChallengesAdapter.this);
        }
    }

    public UserOngoingChallengesAdapter(Context context) {
        this.userList = SingletonClass.getInstance().userModels;
        this.context = context;
        imageLoading = new ImageLoading(context, 5);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_ongoing_challenge_rv_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       // UserModel userModel = userList.get(position);
    }

    @Override
    public int getItemCount() {
        return 8;
    }


    @Override
    public void onClick(View v) {

    }
}

