package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerStandardTwo;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class LiveVideosAdapter extends RecyclerView.Adapter<LiveVideosAdapter.BattleVideoHolder> {

    private ArrayList<VideoModel> categoryModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;


    public class BattleVideoHolder extends RecyclerView.ViewHolder {
        public TextView titleTV, userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV, secondUserNameTV;
        public ImageView moreIV;
        public FrameLayout videoFL;
        private ImageView playVideosIV;
        private FrameLayout fullscreenFL;
        private ImageView fullscreenIV;
        private JCVideoPlayerStandard firstVideoPlayer;
        JCVideoPlayerStandardTwo secondVideoPlayer;

        public BattleVideoHolder(View view) {
            super(view);

            titleTV = (TextView) view.findViewById(R.id.titleTV);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            secondUserNameTV = (TextView) view.findViewById(R.id.secondUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            moreIV = (ImageView) view.findViewById(R.id.moreIV);
            playVideosIV = (ImageView) view.findViewById(R.id.playVideosIV);
            fullscreenFL = (FrameLayout) view.findViewById(R.id.fullscreenFL);
            fullscreenIV = (ImageView) view.findViewById(R.id.fullscreenIV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
            firstVideoPlayer = (JCVideoPlayerStandard) view.findViewById(R.id.firstVideoPlayer);
            secondVideoPlayer = (JCVideoPlayerStandardTwo) view.findViewById(R.id.secondVideoPlayer);

        }
    }

    public LiveVideosAdapter(Context context, ArrayList<VideoModel> categoryModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.categoryModels = categoryModels;
        this.context = context;
    }

    @Override
    public BattleVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.live_battle_video_item, parent, false);
        return new BattleVideoHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final BattleVideoHolder holder, final int position) {
        //  VideoModel userModel = categoryModels.get(position);
        holder.playVideosIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideos(holder);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {recyclerViewClick.onClickItem(position, v);
            }
        });

    }

    private void playVideos(BattleVideoHolder holder) {
        holder.firstVideoPlayer.startPlayLogic();
        holder.secondVideoPlayer.startPlayLogic();
        holder.fullscreenFL.setVisibility(View.VISIBLE);
        holder.playVideosIV.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

}


