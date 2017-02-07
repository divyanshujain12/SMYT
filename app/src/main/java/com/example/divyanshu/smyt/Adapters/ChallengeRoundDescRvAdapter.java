package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.activities.OtherUserProfileActivity;
import com.example.divyanshu.smyt.activities.RecordChallengeVideoActivity;
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
    private int customerNumber;

    public ChallengeRoundDescRvAdapter(Context context, ArrayList<ChallengeModel> categoryModels, RecyclerViewClick recyclerViewClick, int customerNumber) {

        this.challengeModels = categoryModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
        this.recyclerViewClick = recyclerViewClick;
        this.customerNumber = customerNumber;
    }

    private class RoundIncompleteDescViewHolder extends RecyclerView.ViewHolder {
        private ImageView firstUserIV, secondUserIV;
        private TextView firstUserNameTV, secondUserNameTV;
        private TextView roundNumberTV, genreNameTV;
        private TextView challengeTimeTV;
        private LinearLayout firstUserLL, secondUserLL;
        private TextView dateTV;

        private RoundIncompleteDescViewHolder(View view) {
            super(view);
            firstUserLL = (LinearLayout) view.findViewById(R.id.firstUserLL);
            secondUserLL = (LinearLayout) view.findViewById(R.id.secondUserLL);
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

    private class RoundCompletedDescViewHolder extends RecyclerView.ViewHolder {
        private ImageView firstUserIV, secondUserIV;
        private TextView firstUserNameTV, secondUserNameTV;
        private TextView roundNumberTV, genreNameTV;
        private FrameLayout userWinningBar;
        private LinearLayout firstUserLL, secondUserLL;
        private TextView userOneWinLoseTV;

        private RoundCompletedDescViewHolder(View view) {
            super(view);
            firstUserLL = (LinearLayout) view.findViewById(R.id.firstUserLL);
            secondUserLL = (LinearLayout) view.findViewById(R.id.secondUserLL);
            firstUserIV = (ImageView) view.findViewById(R.id.firstUserIV);
            secondUserIV = (ImageView) view.findViewById(R.id.secondUserIV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            secondUserNameTV = (TextView) view.findViewById(R.id.secondUserNameTV);
            roundNumberTV = (TextView) view.findViewById(R.id.roundNumberTV);
            genreNameTV = (TextView) view.findViewById(R.id.genreNameTV);
            userWinningBar = (FrameLayout) view.findViewById(R.id.userWinningBar);
            userOneWinLoseTV = (TextView) view.findViewById(R.id.userOneWinLoseTV);
        }

    }

    private class RoundNotPlayedDescViewHolder extends RecyclerView.ViewHolder {
        private ImageView firstUserIV, secondUserIV;
        private TextView firstUserNameTV, secondUserNameTV;
        private TextView roundNumberTV, genreNameTV;
        private FrameLayout userWinningBar;
        private LinearLayout firstUserLL, secondUserLL;

        private RoundNotPlayedDescViewHolder(View view) {
            super(view);
            firstUserLL = (LinearLayout) view.findViewById(R.id.firstUserLL);
            secondUserLL = (LinearLayout) view.findViewById(R.id.secondUserLL);
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
            return new RoundIncompleteDescViewHolder(itemView);
        } else if (viewType == 1) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.challenge_completed_round_rv_item, parent, false);
            return new RoundCompletedDescViewHolder(itemView);
        } else if (viewType == 2) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.challenge_round_not_played_rv_item, parent, false);
            return new RoundNotPlayedDescViewHolder(itemView);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RoundIncompleteDescViewHolder)
            setViewForIncompleteRound(((RoundIncompleteDescViewHolder) holder), position);
        else if (holder instanceof RoundCompletedDescViewHolder)
            setViewForCompletedRound(((RoundCompletedDescViewHolder) holder), position);
        else if (holder instanceof RoundNotPlayedDescViewHolder)
            setViewForNotPlayedRound(((RoundNotPlayedDescViewHolder) holder), position);
    }

    private void setViewForIncompleteRound(RoundIncompleteDescViewHolder holder, int position) {

        final ChallengeModel challengeModel = challengeModels.get(position);
        long roundDateAndTime = Long.parseLong(challengeModel.getRound_date());
        String timeDifference = Utils.getChallengeTimeDifference(roundDateAndTime);
        imageLoading.LoadImage(challengeModel.getProfileimage(), holder.firstUserIV, null);
        imageLoading.LoadImage(challengeModel.getProfileimage1(), holder.secondUserIV, null);
        holder.roundNumberTV.setText(context.getString(R.string.round_txt) + " " + challengeModel.getRound_no());
        holder.firstUserNameTV.setText(challengeModel.getFirst_name());
        holder.secondUserNameTV.setText(challengeModel.getFirst_name1());
        holder.genreNameTV.setText(challengeModel.getGenre());
        holder.challengeTimeTV.setText(Utils.formatDateAndTime(roundDateAndTime, Utils.TIME_FORMAT) + " (" + timeDifference + " )");
        holder.dateTV.setText(Utils.formatDateAndTime(roundDateAndTime, Utils.DATE_FORMAT));
        holder.firstUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserDetailActivity(challengeModel.getCustomer_id());
            }
        });
        holder.secondUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserDetailActivity(challengeModel.getCustomer_id1());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!challengeModel.getCustomer_id1().equals("") && challengeModel.getCurrent_customer_video_status() != 0) {
                    Intent intent = new Intent(context, RecordChallengeVideoActivity.class);
                    intent.putExtra(Constants.ROUND_TIME, challengeModel.getRound_date());
                    intent.putExtra(Constants.CHALLENGE_ID, challengeModel.getChallenge_id());
                    intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, challengeModel.getCustomers_videos_id());
                    context.startActivity(intent);
                }
            }
        });
    }


    private void setViewForCompletedRound(final RoundCompletedDescViewHolder holder, int position) {

        final ChallengeModel challengeModel = challengeModels.get(position);
        imageLoading.LoadImage(challengeModel.getProfileimage(), holder.firstUserIV, null);
        imageLoading.LoadImage(challengeModel.getProfileimage1(), holder.secondUserIV, null);
        holder.roundNumberTV.setText(context.getString(R.string.round_txt) + " " + challengeModel.getRound_no());
        holder.firstUserNameTV.setText(challengeModel.getFirst_name());
        holder.secondUserNameTV.setText(challengeModel.getFirst_name1());
        holder.genreNameTV.setText(challengeModel.getGenre());
        setRoundStatusView(holder, challengeModel);

        holder.firstUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserDetailActivity(challengeModel.getCustomer_id());
            }
        });
        holder.secondUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserDetailActivity(challengeModel.getCustomer_id1());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });
    }

    private void setRoundStatusView(RoundCompletedDescViewHolder holder, ChallengeModel challengeModel) {
        if (Utils.isDifferenceLowerThanTwentyFourHours(Long.parseLong(challengeModel.getRound_date()))) {
            holder.userWinningBar.addView(addOpenVotingView(challengeModel.getVote(), challengeModel.getVote1()));
        } else {

            switch (challengeModel.getWho_won()) {
                case "1":
                    holder.userWinningBar.addView(UserWinnerBar(R.layout.first_user_win_bar));
                    break;
                case "2":
                    holder.userWinningBar.addView(UserWinnerBar(R.layout.second_user_win_bar));
                    break;
                case "3":
                    holder.userWinningBar.addView(UserWinnerBar(R.layout.round_tie));
                    break;
            }
        }
    }

    private void setViewForNotPlayedRound(final RoundNotPlayedDescViewHolder holder, int position) {

        final ChallengeModel challengeModel = challengeModels.get(position);
        imageLoading.LoadImage(challengeModel.getProfileimage(), holder.firstUserIV, null);
        imageLoading.LoadImage(challengeModel.getProfileimage1(), holder.secondUserIV, null);
        holder.roundNumberTV.setText(context.getString(R.string.round_txt) + " " + challengeModel.getRound_no());
        holder.firstUserNameTV.setText(challengeModel.getFirst_name());
        holder.secondUserNameTV.setText(challengeModel.getFirst_name1());
        holder.genreNameTV.setText(challengeModel.getGenre());

        holder.firstUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserDetailActivity(challengeModel.getCustomer_id());
            }
        });
        holder.secondUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserDetailActivity(challengeModel.getCustomer_id1());
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        String status = challengeModels.get(position).getComplete_status();
        boolean isTimeGone = Utils.isTimeGone(Long.parseLong(challengeModels.get(position).getRound_date()));
        switch (customerNumber) {
            case 1:
                if (isTimeGone) {
                } else {
                }
                break;
            case 2:
                break;
            default:
                return 0;

        }


        if (isTimeGone) {
            if (status.equals("0")) {

            }

            if (status.equals("1"))
                return 1;
            else
                return 2;
        } else
            return 0;


      /*  if (status.equals("1") && !Utils.isTimeGone(Long.parseLong(challengeModels.get(position).getRound_date())))
            return 1;
        else if (!status.equals("1") && Utils.isTimeGone(Long.parseLong(challengeModels.get(position).getRound_date())))
            return 2;
        else return 0;*/
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

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    private void goToUserDetailActivity(String customer_id) {
        if (!customer_id.equals("") && !customer_id.equals(MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID))) {
            Intent intent = new Intent(context, OtherUserProfileActivity.class);
            intent.putExtra(Constants.CUSTOMER_ID, customer_id);
            context.startActivity(intent);
        }
    }

    private View addOpenVotingView(String vote, String vote1) {
        View view = LayoutInflater.from(context).inflate(R.layout.voting_view, null);
        TextView userOneVoteCountTV = (TextView) view.findViewById(R.id.userOneVoteCountTV);
        TextView userTwoVoteCountTV = (TextView) view.findViewById(R.id.userTwoVoteCountTV);
        view.findViewById(R.id.votingStatusTV).setVisibility(View.GONE);
        userOneVoteCountTV.setText(vote);
        userTwoVoteCountTV.setText(vote1);
        return view;
    }
}


