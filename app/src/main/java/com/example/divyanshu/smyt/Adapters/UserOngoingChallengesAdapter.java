package com.example.divyanshu.smyt.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.divyanshu.smyt.Constants.ApiCodes.CHALLENGE_ACCEPT;
import static com.example.divyanshu.smyt.Constants.ApiCodes.CHALLENGE_REJECT;

/**
 * Created by divyanshu.jain on 9/2/2016.
 */
public class UserOngoingChallengesAdapter extends RecyclerView.Adapter<UserOngoingChallengesAdapter.MyViewHolder> implements CallWebService.ObjectResponseCallBack {


    private ArrayList<ChallengeModel> challengeModels;
    private Context context;
    private ImageLoading imageLoading;
    private RecyclerViewClick recyclerViewClick;
    private String ACTIVE = "Active";
    private String IN_ACTIVE = "Inactive";
    String round_count_string = "(%s/%s)";
    private int acceptRejectPos = -1;

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

        final ChallengeModel challengeModel = challengeModels.get(position);
        holder.titleTV.setText(challengeModel.getTitle());
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
                acceptRejectPos = position;
                CallWebService.getInstance(context, true, CHALLENGE_ACCEPT).hitJsonObjectRequestAPI(CallWebService.POST, API.ACCEPT_REJECT_CHALLENGE, createJsonForAcceptRejectChallenge(challengeModel.getChallenge_id(), "1"), UserOngoingChallengesAdapter.this);
            }
        });

        holder.declineTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRejectPos = position;
                CallWebService.getInstance(context, true, CHALLENGE_REJECT).hitJsonObjectRequestAPI(CallWebService.POST, API.ACCEPT_REJECT_CHALLENGE, createJsonForAcceptRejectChallenge(challengeModel.getChallenge_id(), "0"), UserOngoingChallengesAdapter.this);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(position, v);
            }
        });
    }

    private JSONObject createJsonForAcceptRejectChallenge(String challengeID, String s) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(context);
        try {
            jsonObject.put(Constants.CHALLENGE_ID, challengeID);
            jsonObject.put(Constants.STATUS, s);
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public int getItemCount() {
        return challengeModels.size();
    }

    public void addItems(ArrayList<ChallengeModel> challengeModels) {
        this.challengeModels.addAll(challengeModels);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        challengeModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

    }
    public void clear(){
        challengeModels.clear();
        notifyDataSetChanged();
    }

    public void updateAcceptStatusIntoList(int pos) {
        challengeModels.get(pos).setCurrent_customer_video_status(1);
        notifyDataSetChanged();
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        switch (apiType) {
            case CHALLENGE_ACCEPT:
                updateAcceptStatusIntoList(acceptRejectPos);
                CommonFunctions.getInstance().showSuccessSnackBar(((Activity) context), response.getString(Constants.MESSAGE));
                break;
            case CHALLENGE_REJECT:
                removeItem(acceptRejectPos);
                CommonFunctions.getInstance().showSuccessSnackBar(((Activity) context), response.getString(Constants.MESSAGE));
                break;
        }
    }

    @Override
    public void onFailure(String str, int apiType) {
        CommonFunctions.getInstance().showErrorSnackBar(((Activity) context), str);
    }


}

