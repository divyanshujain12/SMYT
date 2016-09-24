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
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class LiveVideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<VideoModel> categoryModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;


    public class BattleVideoHolder extends RecyclerView.ViewHolder {
        public TextView titleTV, userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV, secondUserNameTV;
        public ImageView  moreIV;
        public FrameLayout videoFL;
        private JCVideoPlayerStandard firstVideoPlayer, secondVideoPlayer;
        public BattleVideoHolder(View view) {
            super(view);

            titleTV = (TextView) view.findViewById(R.id.titleTV);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            secondUserNameTV = (TextView) view.findViewById(R.id.secondUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            moreIV = (ImageView) view.findViewById(R.id.moreIV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
            firstVideoPlayer = (JCVideoPlayerStandard) view.findViewById(R.id.firstVideoPlayer);
            secondVideoPlayer = (JCVideoPlayerStandard) view.findViewById(R.id.secondVideoPlayer);

        }
    }

    public LiveVideosAdapter(Context context, ArrayList<VideoModel> categoryModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(position, v);
            }
        });
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


