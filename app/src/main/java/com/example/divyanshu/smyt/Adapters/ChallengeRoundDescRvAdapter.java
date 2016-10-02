package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.ChallangeModel;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu on 9/3/2016.
 */
public class ChallengeRoundDescRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChallangeModel> categoryModels;
    private Context context;

    public class ChallengeDescViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTV, userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;
        public ImageView videoThumbIV, moreIV;
        public FrameLayout videoFL;

        public ChallengeDescViewHolder(View view) {
            super(view);
            titleTV = (TextView) view.findViewById(R.id.titleTV);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            videoThumbIV = (ImageView) view.findViewById(R.id.videoThumbIV);
            moreIV = (ImageView) view.findViewById(R.id.moreIV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
        }
    }


    public ChallengeRoundDescRvAdapter(Context context, ArrayList<ChallangeModel> categoryModels) {

        this.categoryModels = categoryModels;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.challenge_round_desc_rv_item, parent, false);
        return new ChallengeDescViewHolder(itemView);


    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //  VideoModel userModel = categoryModels.get(position);

       /* holder.categoryNameTV.setText(userModel.getcategory_name());
        holder.categoryIV.setImageResource(userModel.getIcon());
        holder.categoryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingletonClass.getInstance().setSelectedCategoryPos(position);
                Intent intent = new Intent(context, CategoryDescriptionActivity.class);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}


