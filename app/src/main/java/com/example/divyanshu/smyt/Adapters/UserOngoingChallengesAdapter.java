package com.example.divyanshu.smyt.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Fragments.OngoingChallengeDescriptionFragment;
import com.example.divyanshu.smyt.GlobalClasses.SingletonClass;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 9/2/2016.
 */
public class UserOngoingChallengesAdapter extends RecyclerView.Adapter<UserOngoingChallengesAdapter.MyViewHolder> implements View.OnClickListener {


    private ArrayList<ChallengeModel> challengeModels;
    private Context context;
    private ImageLoading imageLoading;
    private RecyclerViewClick recyclerViewClick;
    private String ACTIVE = "Active";
    private String IN_ACTIVE = "Inactive";
    String round_count_string = "(%s/%s)";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
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
            titleTV = (TextView) view.findViewById(R.id.titleTV);
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ChallengeModel challengeModel = challengeModels.get(position);
        holder.titleTV.setText(challengeModel.getTitle());
        holder.genreNameTV.setText(challengeModel.getGenre());
        holder.challengeTypeTV.setText(challengeModel.getShare_status());
        holder.roundsCountTV.setText(String.format(round_count_string, challengeModel.getRound_no(), challengeModel.getTotal_round()));
        holder.challengeTimeTV.setText(challengeModel.getRound_date());
        if (challengeModel.getStatus().equals(ACTIVE)) {
            holder.acceptAndDeclineLL.setVisibility(View.GONE);
        } else
            holder.acceptAndDeclineLL.setVisibility(View.VISIBLE);

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

    public void addItems(ArrayList<ChallengeModel> challengeModels) {
        this.challengeModels.addAll(challengeModels);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        OngoingChallengeDescriptionFragment challangeFragment = new OngoingChallengeDescriptionFragment();
        challangeFragment.show(fragmentManager, challangeFragment.getClass().getName());
    }
}

