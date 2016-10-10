package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CustomLinearLayoutManager;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.VideoSetupUtils;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerStandardTwo;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class UploadedAllVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<VideoModel> categoryModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;
    private ImageLoading imageLoading;

    public class SingleVideoHolder extends RecyclerView.ViewHolder {
        public TextView titleTV, userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;
        public ImageView videoThumbIV, moreIV;
        public FrameLayout videoFL;

        public SingleVideoHolder(View view) {
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

    public class BattleVideoHolder extends RecyclerView.ViewHolder {
        public TextView titleTV, userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV, secondUserNameTV;
        public ImageView moreIV;
        public FrameLayout videoFL;
        private JCVideoPlayerStandard firstVideoPlayer;
        public JCVideoPlayerStandardTwo secondVideoPlayer;
        private ImageView playVideosIV;
        private FrameLayout fullscreenFL;
        private ImageView fullscreenIV;

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
            //  setVideoPlayerPlayButtonVisibility(false, this);
        }
    }

    public class TopRatedVideoHolder extends RecyclerView.ViewHolder {
        public RecyclerView topRatedVideosRV;

        public TopRatedVideoHolder(View itemView) {
            super(itemView);
            topRatedVideosRV = (RecyclerView) itemView.findViewById(R.id.topRatedVideosRV);
            topRatedVideosRV.setLayoutManager(new CustomLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            TopRatedVideosAdapter topRatedVideosAdapter = new TopRatedVideosAdapter(context, new ArrayList<VideoModel>());
            topRatedVideosRV.setAdapter(topRatedVideosAdapter);
        }
    }

    public UploadedAllVideoAdapter(Context context, ArrayList<VideoModel> categoryModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.categoryModels = categoryModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;


        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_video_item, parent, false);
                return new SingleVideoHolder(itemView);

            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.uploaded_battle_video_item, parent, false);
                return new BattleVideoHolder(itemView);

            case 3:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.top_rated_videos_rv, parent, false);
                return new TopRatedVideoHolder(itemView);
            default:
                return null;
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //      recyclerViewClick.onClickItem(getItemViewType(position), v);
            }
        });

        if (holder instanceof BattleVideoHolder) {
            BattleVideoHolder battleVideoHolder = (BattleVideoHolder) holder;
            setupBattleViewHolder(battleVideoHolder);

        }

    }

    private void setupBattleViewHolder(final BattleVideoHolder battleVideoHolder) {
        JCVideoPlayerStandard firstVideoPlayer = battleVideoHolder.firstVideoPlayer;
        JCVideoPlayerStandardTwo secondVideoPlayer = battleVideoHolder.secondVideoPlayer;

        VideoSetupUtils.getInstance(context).setUpFirstVideoPlayer(firstVideoPlayer, context.getString(R.string.dummy_m3u8_video), context.getString(R.string.dummy_image_url));
        VideoSetupUtils.getInstance(context).setUpSecondVideoPlayer(secondVideoPlayer, context.getString(R.string.dummy_video_url), context.getString(R.string.dummy_image_url));


        battleVideoHolder.fullscreenIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VideoSetupUtils.getInstance(context).onBackButtonClick(battleVideoHolder.firstVideoPlayer, battleVideoHolder.secondVideoPlayer);
                fullScreenClicked(battleVideoHolder);
            }
        });

        battleVideoHolder.playVideosIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSetupUtils.getInstance(context).playMusic(battleVideoHolder.firstVideoPlayer, battleVideoHolder.secondVideoPlayer);
            }
        });
    }

    private void fullScreenClicked(BattleVideoHolder battleVideoHolder) {
        JCVideoPlayerStandard firstVideoPlayer = battleVideoHolder.firstVideoPlayer;
        JCVideoPlayerStandardTwo secondVideoPlayer = battleVideoHolder.secondVideoPlayer;

        VideoSetupUtils.getInstance(context).playMusic(firstVideoPlayer, secondVideoPlayer);

        firstVideoPlayer.startWindowTiny(Gravity.TOP);
        secondVideoPlayer.startWindowTiny(Gravity.BOTTOM);
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 3;
        return position % 2;
    }


}

