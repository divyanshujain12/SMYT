package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.SingleVideoPlayerCustomView;
import com.example.divyanshu.smyt.CustomViews.TwoVideoPlayerCustomView;
import com.example.divyanshu.smyt.DialogActivities.UploadedBattleRoundDescActivity;
import com.example.divyanshu.smyt.DialogActivities.UserVideoDescActivity;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class TopRatedVideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AllVideoModel> videoList;
    private Context context;
    private ImageLoading imageLoading;


    protected TopRatedVideosAdapter(Context context, ArrayList<AllVideoModel> videoList) {
        this.videoList = videoList;
        this.context = context;
        imageLoading = new ImageLoading(context, 5);
    }

    private class SingleVideoHolder extends RecyclerView.ViewHolder {
        public TextView userNameTV, userTimeTV;
        public SingleVideoPlayerCustomView singleVideoPlayer;

        public SingleVideoHolder(View view) {
            super(view);
            userNameTV = (TextView) view.findViewById(R.id.userNameTV);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            singleVideoPlayer = (SingleVideoPlayerCustomView) view.findViewById(R.id.singleVideoPlayer);
        }
    }

    private class ChallengeVideosHolder extends RecyclerView.ViewHolder {
        public TextView userNameTV, userTimeTV;
        private TwoVideoPlayerCustomView twoVideoPlayer;

        private ChallengeVideosHolder(View view) {
            super(view);
            userNameTV = (TextView) view.findViewById(R.id.userNameTV);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            twoVideoPlayer = (TwoVideoPlayerCustomView) view.findViewById(R.id.twoVideoPlayers);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.all_video_top_rated_challenge_video, parent, false);
            return new ChallengeVideosHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.all_video_top_rated_single_item, parent, false);
            return new SingleVideoHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        AllVideoModel videoModel = videoList.get(position);
        if (holder instanceof SingleVideoHolder)
            setUpForSingleVideo((SingleVideoHolder) holder, videoModel);
        else
            setUpForTwoVideo((ChallengeVideosHolder) holder, videoModel);

    }

    private void setUpForSingleVideo(final SingleVideoHolder holder, AllVideoModel videoModel) {

        holder.userNameTV.setText(videoModel.getFirst_name());
        holder.singleVideoPlayer.setUp(videoModel.getVideo_url(), videoModel.getThumbnail(), videoModel.getCustomers_videos_id());
        holder.userTimeTV.setText(Utils.getChallengeTimeDifference(videoModel.getEdate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserVideoDescActivity.class);
                intent.putExtra(Constants.FROM_BANNER, true);
                intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, videoList.get(holder.getAdapterPosition()).getCustomers_videos_id());
                context.startActivity(intent);

            }
        });
    }

    private void setUpForTwoVideo(final ChallengeVideosHolder holder, AllVideoModel videoModel) {
        holder.userNameTV.setText(videoModel.getFirst_name());
        holder.twoVideoPlayer.setUp(videoModel.getVideo_url(), videoModel.getVideo_url1(), videoModel.getThumbnail(), videoModel.getThumbnail1(), videoModel.getCustomers_videos_id());
        holder.userTimeTV.setText(Utils.getChallengeTimeDifference(videoModel.getEdate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UploadedBattleRoundDescActivity.class);
                intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, videoList.get(holder.getAdapterPosition()).getCustomers_videos_id());
                intent.putExtra(Constants.FROM_BANNER, true);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void addVideos(ArrayList<AllVideoModel> videoList) {
        if (videoList != null) {
            this.videoList = videoList;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (videoList.get(position).getType().equals("Challenge"))
            return 1;
        else return 0;


    }

}
