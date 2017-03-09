package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.AllNotificationModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 3/8/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private ArrayList<AllNotificationModel> allNotificationModels;
    private Context context;
    private ImageLoading imageLoading;
    private RecyclerViewClick recyclerViewClick;
    private String NEW_CHALLENGE_DESC_STRING = "New Challenge posted in %s for %s rounds at %s";
    private String UPCOMING_ROUND_TITLE_STRING = "Upcoming Round In %s";
    private String UPCOMING_ROUND_DESC_STRING = "Your %snd round of %s start in next %s";
    private String UPCOMING_ROUND_RESPONSE_STRING = "Go to Round %s";

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView userIV;
        private TextView titleMsgTV, descTV, responseTV;

        private MyViewHolder(View view) {
            super(view);
            userIV = (RoundedImageView) view.findViewById(R.id.userIV);
            titleMsgTV = (TextView) view.findViewById(R.id.titleMsgTV);
            descTV = (TextView) view.findViewById(R.id.descTV);
            responseTV = (TextView) view.findViewById(R.id.responseTV);
        }
    }

    public NotificationAdapter(Context context, ArrayList<AllNotificationModel> allNotificationModels, RecyclerViewClick recyclerViewClick) {
        this.allNotificationModels = allNotificationModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
        this.recyclerViewClick = recyclerViewClick;
    }

    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_rv_item, parent, false);

        return new NotificationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.MyViewHolder holder, int position) {
        AllNotificationModel allNotificationModel = allNotificationModels.get(position);
        if (allNotificationModels.get(position).getStatus().equalsIgnoreCase("Inactive")) {
            setUpViewForMewChallenge(holder, allNotificationModel);
        } else {
            setUpViewForUpcomingChallenge(holder, allNotificationModel);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), view);
            }
        });
    }

    private void setUpViewForMewChallenge(MyViewHolder holder, AllNotificationModel allNotificationModel) {
        String timeDifference = Utils.formatDateAndTime(allNotificationModel.getRound_date(), Utils.CURRENT_DATE_FORMAT);
        holder.titleMsgTV.setText(R.string.new_challenge);
        holder.descTV.setText(String.format(NEW_CHALLENGE_DESC_STRING, allNotificationModel.getGenre(), allNotificationModel.getTotal_round(), timeDifference));
        holder.responseTV.setText(R.string.respond);
    }

    private void setUpViewForUpcomingChallenge(MyViewHolder holder, AllNotificationModel allNotificationModel) {
        String timeDifference = Utils.getChallengeTimeDifference(allNotificationModel.getRound_date());
        holder.titleMsgTV.setText(String.format(UPCOMING_ROUND_TITLE_STRING, timeDifference));
        holder.descTV.setText(String.format(UPCOMING_ROUND_DESC_STRING, allNotificationModel.getRound_no(), allNotificationModel.getTitle(), timeDifference));
        holder.responseTV.setText(String.format(UPCOMING_ROUND_RESPONSE_STRING, allNotificationModel.getRound_no()));

    }

    @Override
    public int getItemCount() {
        return allNotificationModels.size();
    }


}
