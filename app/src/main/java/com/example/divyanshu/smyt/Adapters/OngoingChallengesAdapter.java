package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.CustomViews.ChallengeTitleView;
import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;
import com.player.divyanshu.customvideoplayer.TwoVideoPlayers;

import java.util.ArrayList;


/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class OngoingChallengesAdapter extends RecyclerView.Adapter<OngoingChallengesAdapter.BattleVideoHolder> implements PopupItemClicked {

    private ArrayList<VideoModel> categoryModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;

    public class BattleVideoHolder extends RecyclerView.ViewHolder {


        public TextView userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV, secondUserNameTV;
        private ChallengeTitleView challengeTitleView;
        public FrameLayout videoFL;
        private ImageView playVideosIV;
        private FrameLayout fullscreenFL;
        private ImageView fullscreenIV;
        private TwoVideoPlayers twoVideoPlayers;

        public BattleVideoHolder(View view) {
            super(view);
            challengeTitleView = (ChallengeTitleView) view.findViewById(R.id.challengeTitleView);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            secondUserNameTV = (TextView) view.findViewById(R.id.secondUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            playVideosIV = (ImageView) view.findViewById(R.id.playVideosIV);
            fullscreenFL = (FrameLayout) view.findViewById(R.id.fullscreenFL);
            fullscreenIV = (ImageView) view.findViewById(R.id.fullscreenIV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
            twoVideoPlayers = (TwoVideoPlayers) view.findViewById(R.id.twoVideoPlayers);
        }
    }
    public OngoingChallengesAdapter(Context context, ArrayList<VideoModel> categoryModels, RecyclerViewClick recyclerViewClick) {
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
    public void onBindViewHolder(final BattleVideoHolder holder, int position) {
        //  VideoModel userModel = categoryModels.get(position);
        holder.challengeTitleView.setUp("", this, position);
        holder.challengeTitleView.showHideMoreIvButton(false);
        playVideos(holder);
        holder.playVideosIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideos(holder);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });

    }


    private void playVideos(BattleVideoHolder holder) {
        holder.twoVideoPlayers.setVideoUrls(context.getString(R.string.dummy_m3u8_video), context.getString(R.string.dummy_m3u8_video));
        holder.twoVideoPlayers.setThumbnail(context.getString(R.string.dummy_image_url), context.getString(R.string.dummy_image_url));
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

    @Override
    public void onPopupMenuClicked(View view, int position) {

    }

}


