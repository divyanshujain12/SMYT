package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu on 9/3/2016.
 */
public class ChallengeRoundDescRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChallengeModel> challengeModels;
    private Context context;
    private ImageLoading imageLoading;
    private RecyclerViewClick recyclerViewClick;

    public ChallengeRoundDescRvAdapter(Context context, ArrayList<ChallengeModel> categoryModels, RecyclerViewClick recyclerViewClick) {

        this.challengeModels = categoryModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
        this.recyclerViewClick = recyclerViewClick;
    }

    public class ChallengeIncompleteDescViewHolder extends RecyclerView.ViewHolder {
        private ImageView firstUserIV, secondUserIV;
        private TextView firstUserNameTV, secondUserNameTV;
        private TextView roundNumberTV, genreNameTV;
        private TextView challengeTimeTV;

        private TextView dateTV;

        public ChallengeIncompleteDescViewHolder(View view) {
            super(view);
            firstUserIV = (ImageView) view.findViewById(R.id.firstUserIV);
            secondUserIV = (ImageView) view.findViewById(R.id.secondUserIV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            secondUserNameTV = (TextView) view.findViewById(R.id.secondUserNameTV);
            roundNumberTV = (TextView) view.findViewById(R.id.roundNumberTV);
            genreNameTV = (TextView) view.findViewById(R.id.genreNameTV);
            challengeTimeTV = (TextView) view.findViewById(R.id.challengeTimeTV);
            dateTV = (TextView) view.findViewById(R.id.dateTV);

        }

    }

    public class ChallengeCompletedDescViewHolder extends RecyclerView.ViewHolder {
        private ImageView firstUserIV, secondUserIV;
        private TextView firstUserNameTV, secondUserNameTV;
        private TextView roundNumberTV, genreNameTV;
        private FrameLayout userWinningBar;

        public ChallengeCompletedDescViewHolder(View view) {
            super(view);
            firstUserIV = (ImageView) view.findViewById(R.id.firstUserIV);
            secondUserIV = (ImageView) view.findViewById(R.id.secondUserIV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            secondUserNameTV = (TextView) view.findViewById(R.id.secondUserNameTV);
            roundNumberTV = (TextView) view.findViewById(R.id.roundNumberTV);
            genreNameTV = (TextView) view.findViewById(R.id.genreNameTV);

            userWinningBar = (FrameLayout) view.findViewById(R.id.userWinningBar);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.challenge_incomplete_round_rv_item, parent, false);
            return new ChallengeIncompleteDescViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.challenge_completed_round_rv_item, parent, false);
            return new ChallengeCompletedDescViewHolder(itemView);
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ChallengeIncompleteDescViewHolder)
            setViewForIncompleteview(((ChallengeIncompleteDescViewHolder) holder), position);
        else
            setViewForCompleteview(((ChallengeCompletedDescViewHolder) holder), position);
    }

    private void setViewForIncompleteview(ChallengeIncompleteDescViewHolder holder, int position) {

        ChallengeModel challengeModel = challengeModels.get(position);
        //String[] splitDate = challengeModel.getRound_date().split(" ");
        long roundDateAndTime = Long.parseLong(challengeModel.getRound_date());
        String timeDifference = Utils.getTimeDifference(roundDateAndTime);
        imageLoading.LoadImage(challengeModel.getProfileimage(), holder.firstUserIV, null);
        imageLoading.LoadImage(challengeModel.getProfileimage1(), holder.secondUserIV, null);
        holder.roundNumberTV.setText(context.getString(R.string.round_txt) + " " + challengeModel.getRound_no());
        holder.firstUserNameTV.setText(challengeModel.getFirst_name());
        holder.secondUserNameTV.setText(challengeModel.getFirst_name1());
        holder.genreNameTV.setText(challengeModel.getGenre());
        holder.challengeTimeTV.setText(Utils.formatDateAndTime(roundDateAndTime, Utils.TIME_FORMAT) + " (" + timeDifference + " left)");
        holder.dateTV.setText(Utils.formatDateAndTime(roundDateAndTime, Utils.DATE_FORMAT));
    }

    private void setViewForCompleteview(final ChallengeCompletedDescViewHolder holder, int position) {

        ChallengeModel challengeModel = challengeModels.get(position);
        imageLoading.LoadImage(challengeModel.getProfileimage(), holder.firstUserIV, null);
        imageLoading.LoadImage(challengeModel.getProfileimage1(), holder.secondUserIV, null);
        holder.roundNumberTV.setText(context.getString(R.string.round_txt) + " " + challengeModel.getRound_no());
        holder.firstUserNameTV.setText(challengeModel.getFirst_name());
        holder.secondUserNameTV.setText(challengeModel.getFirst_name1());
        holder.genreNameTV.setText(challengeModel.getGenre());
        int voteInt = Integer.parseInt(challengeModel.getVote());
        int vote1Int = Integer.parseInt(challengeModel.getVote1());

        if (voteInt > vote1Int)
            holder.userWinningBar.addView(UserWinnerBar(R.layout.first_user_win_bar));
        else if (vote1Int > voteInt)
            holder.userWinningBar.addView(UserWinnerBar(R.layout.second_user_win_bar));
        else
            holder.userWinningBar.addView(UserWinnerBar(R.layout.round_tie));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });
    }


    @Override
    public int getItemViewType(int position) {

        if (challengeModels.get(position).getComplete_status().equals("1"))
            return 1;
        else return 0;
    }

    @Override
    public int getItemCount() {
        if (challengeModels == null)
            return 0;
        else
            return challengeModels.size();
    }

    private View UserWinnerBar(int resourceID) {
        return LayoutInflater.from(context).inflate(resourceID, null);
    }

    public void addItems(ArrayList<ChallengeModel> challengeModels) {
        this.challengeModels = challengeModels;
        notifyDataSetChanged();
    }
}


