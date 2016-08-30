package com.example.divyanshu.smyt.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Fragments.PlaySingleVideoFragment;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class LiveVideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private ArrayList<VideoModel> categoryModels;
    private Context context;


    public class BattleVideoHolder extends RecyclerView.ViewHolder {
        public TextView titleTV, userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV, secondUserNameTV;
        public ImageView videoThumbOneIV, videoThumbTwoIV, moreIV;
        public FrameLayout videoFL;

        public BattleVideoHolder(View view) {
            super(view);

            titleTV = (TextView) view.findViewById(R.id.titleTV);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            secondUserNameTV = (TextView) view.findViewById(R.id.secondUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            videoThumbOneIV = (ImageView) view.findViewById(R.id.videoThumbOneIV);
            videoThumbTwoIV = (ImageView) view.findViewById(R.id.videoThumbTwoIV);
            moreIV = (ImageView) view.findViewById(R.id.moreIV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);

            view.setOnClickListener(LiveVideosAdapter.this);
        }
    }

    public LiveVideosAdapter(Context context, ArrayList<VideoModel> categoryModels) {
        this.categoryModels = categoryModels;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.live_battle_video_item, parent, false);
        return new BattleVideoHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //  VideoModel userModel = categoryModels.get(position);

       /* holder.categoryNameTV.setText(userModel.getName());
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
        return 8;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        PlaySingleVideoFragment dialogFragment = new PlaySingleVideoFragment();
        dialogFragment.show(fm, "Sample Fragment");
    }

}


