package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Fragments.UserCompletedChallengeDescFragment;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 9/2/2016.
 */
public class UserCompletedChallengesAdapter extends RecyclerView.Adapter<UserCompletedChallengesAdapter.MyViewHolder> implements View.OnClickListener {


    private ArrayList<ChallengeModel> challengeModels;
    private Context context;
    private ImageLoading imageLoading;
    private RecyclerViewClick recyclerViewClick;
    String round_count_string = "(%s/%s)";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        ImageView moreIV;
        TextView genreNameTV;
        TextView challengeTypeTV;
        TextView roundsCountTV;
        TextView challengeTimeTV;

        public MyViewHolder(View view) {
            super(view);
            titleTV = (TextView) view.findViewById(R.id.titleTV);
            moreIV = (ImageView) view.findViewById(R.id.moreIV);
            genreNameTV = (TextView) view.findViewById(R.id.genreNameTV);
            challengeTypeTV = (TextView) view.findViewById(R.id.challengeTypeTV);
            roundsCountTV = (TextView) view.findViewById(R.id.roundsCountTV);
            challengeTimeTV = (TextView) view.findViewById(R.id.challengeTimeTV);
        }
    }

    public UserCompletedChallengesAdapter(Context context, ArrayList<ChallengeModel> challengeModels, RecyclerViewClick recyclerViewClick) {
        this.challengeModels = challengeModels;
        this.context = context;
        this.recyclerViewClick = recyclerViewClick;
        imageLoading = new ImageLoading(context, 5);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_completed_challenge_rv_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ChallengeModel challengeModel = challengeModels.get(position);
        holder.titleTV.setText(challengeModel.getTitle());
        holder.genreNameTV.setText(challengeModel.getGenre());
        holder.challengeTypeTV.setText(challengeModel.getShare_status());
        holder.roundsCountTV.setText(String.format(round_count_string, challengeModel.getRound_no(), challengeModel.getTotal_round()));
        holder.challengeTimeTV.setText(challengeModel.getRound_date());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return challengeModels.size();
    }


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        UserCompletedChallengeDescFragment challangeFragment = new UserCompletedChallengeDescFragment();
        challangeFragment.show(fragmentManager, challangeFragment.getClass().getName());
    }

    public void addItems(ArrayList<ChallengeModel> challengeModels) {
        this.challengeModels.addAll(challengeModels);
        notifyDataSetChanged();
    }
}

