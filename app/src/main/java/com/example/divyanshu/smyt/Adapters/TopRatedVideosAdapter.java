package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.UserVideoDescActivity;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;
import com.player.divyanshu.customvideoplayer.SingleVideoPlayer;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class TopRatedVideosAdapter extends RecyclerView.Adapter<TopRatedVideosAdapter.MyViewHolder> {

    private ArrayList<VideoModel> videoList;
    private Context context;
    private ImageLoading imageLoading;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTV, userTimeTV;
        public SingleVideoPlayer firstVideoPlayer;

        public MyViewHolder(View view) {
            super(view);
            userNameTV = (TextView) view.findViewById(R.id.userNameTV);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstVideoPlayer = (SingleVideoPlayer) view.findViewById(R.id.firstVideoPlayer);
        }
    }

    public TopRatedVideosAdapter(Context context, ArrayList<VideoModel> videoList) {
        this.videoList = videoList;
        this.context = context;
        imageLoading = new ImageLoading(context, 5);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_video_top_rated_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        VideoModel videoModel = videoList.get(position);
        holder.userNameTV.setText(videoModel.getFirst_name());
        holder.firstVideoPlayer.setThumbnail(videoModel.getThumbnail());
        holder.firstVideoPlayer.setVideoUrl(videoModel.getVideo_url());
        holder.userTimeTV.setText(Utils.getChallengeTimeDifference(videoModel.getEdate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserVideoDescActivity.class);
                intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, videoList.get(holder.getAdapterPosition()).getCustomers_videos_id());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void addVideos(ArrayList<VideoModel> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }
}
