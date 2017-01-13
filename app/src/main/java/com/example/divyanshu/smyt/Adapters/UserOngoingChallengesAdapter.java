package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.ChallengeTitleView;
import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 9/2/2016.
 */
public class UserOngoingChallengesAdapter extends RecyclerView.Adapter<UserOngoingChallengesAdapter.MyViewHolder> implements PopupItemClicked {


    private ArrayList<ChallengeModel> challengeModels;
    private Context context;
    private ImageLoading imageLoading;
    private RecyclerViewClick recyclerViewClick;
    String round_count_string = "(%s/%s)";
    private int acceptRejectPos = -1;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ChallengeTitleView challengeTitleView;
        ImageView moreIV;
        TextView genreNameTV;
        TextView challengeTypeTV;
        TextView roundsCountTV;
        TextView challengeTimeTV;
        TextView declineTV;
        TextView acceptTV;
        LinearLayout acceptAndDeclineLL;

        public MyViewHolder(View view) {
            super(view);
            challengeTitleView = (ChallengeTitleView) view.findViewById(R.id.challengeTitleView);
            moreIV = (ImageView) view.findViewById(R.id.moreIV);
            genreNameTV = (TextView) view.findViewById(R.id.genreNameTV);
            challengeTypeTV = (TextView) view.findViewById(R.id.challengeTypeTV);
            roundsCountTV = (TextView) view.findViewById(R.id.roundsCountTV);
            challengeTimeTV = (TextView) view.findViewById(R.id.challengeTimeTV);
            declineTV = (TextView) view.findViewById(R.id.declineTV);
            acceptTV = (TextView) view.findViewById(R.id.acceptTV);
            acceptAndDeclineLL = (LinearLayout) view.findViewById(R.id.acceptAndDeclineLL);
        }
    }

    public UserOngoingChallengesAdapter(Context context, ArrayList<ChallengeModel> challengeModels, RecyclerViewClick recyclerViewClick) {
        this.challengeModels = challengeModels;
        this.context = context;
        this.recyclerViewClick = recyclerViewClick;
        imageLoading = new ImageLoading(context, 5);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_ongoing_challenge_rv_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ChallengeModel challengeModel = challengeModels.get(position);
        holder.challengeTitleView.setUp(challengeModel.getTitle(), this, position);
        setUpMoreIvButtonVisibilityForSingleVideo(holder, challengeModel);
        //holder.titleTV.setText(challengeModel.getTitle());
        holder.genreNameTV.setText(challengeModel.getGenre());
        holder.challengeTypeTV.setText(challengeModel.getShare_status());
        holder.roundsCountTV.setText(String.format(round_count_string, challengeModel.getRound_no(), challengeModel.getTotal_round()));
        holder.challengeTimeTV.setText(Utils.getChallengeTimeDifference(Long.parseLong(challengeModel.getRound_date())));


        if (challengeModel.getCurrent_customer_video_status() == 0)
            holder.acceptAndDeclineLL.setVisibility(View.VISIBLE);
        else
            holder.acceptAndDeclineLL.setVisibility(View.GONE);

        holder.acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });

        holder.declineTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return challengeModels.size();
    }

    public void addItems(ArrayList<ChallengeModel> challengeModels) {
        this.challengeModels = challengeModels;
        notifyDataSetChanged();
    }


    public void removeItem(int position) {
        challengeModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

    }

    public void clear() {
        challengeModels.clear();
        notifyDataSetChanged();
    }

    public void updateAcceptStatusIntoList(int pos) {
        challengeModels.get(pos).setCurrent_customer_video_status(1);
        notifyDataSetChanged();
    }

    @Override
    public void onPopupMenuClicked(View view, int position) {
        recyclerViewClick.onClickItem(position, view);
    }


    private void setUpMoreIvButtonVisibilityForSingleVideo(MyViewHolder holder, ChallengeModel challengeModel) {
        String currentCustomerID = MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID);
        if (!currentCustomerID.equals(challengeModel.getCustomer_id()))
            holder.challengeTitleView.showHideMoreIvButton(false);
        else
            holder.challengeTitleView.showHideMoreIvButton(true);
    }
}

