package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.ChallengeTitleView;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.CustomViews.VideoTitleView;
import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CustomLinearLayoutManager;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;
import com.player.divyanshu.customvideoplayer.SingleVideoPlayer;
import com.player.divyanshu.customvideoplayer.TwoVideoPlayers;

import java.util.ArrayList;


/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class UploadedAllVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PopupItemClicked {

    private ArrayList<AllVideoModel> allVideoModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;
    private ImageLoading imageLoading;
    private TopRatedVideosAdapter topRatedVideosAdapter;


    private class SingleVideoHolder extends RecyclerView.ViewHolder {
        private VideoTitleView videoTitleView;
        public TextView userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;
        private ImageView videoThumbIV;
        public FrameLayout videoFL;
        private RoundedImageView firstUserIV;
        private SingleVideoPlayer firstVideoPlayer;

        private SingleVideoHolder(View view) {
            super(view);
            videoTitleView = (VideoTitleView) view.findViewById(R.id.videoTitleView);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            videoThumbIV = (ImageView) view.findViewById(R.id.videoThumbIV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
            firstUserIV = (RoundedImageView) view.findViewById(R.id.firstUserIV);
            firstVideoPlayer = (SingleVideoPlayer) view.findViewById(R.id.firstVideoPlayer);
        }
    }

    private class BattleVideoHolder extends RecyclerView.ViewHolder {
        private ChallengeTitleView challengeTitleView;
        public TextView userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV, secondUserNameTV;
        public FrameLayout videoFL;
        private TwoVideoPlayers twoVideoPlayers;
        private RoundedImageView firstUserIV, secondUserIV;

        private BattleVideoHolder(View view) {
            super(view);
            challengeTitleView = (ChallengeTitleView) view.findViewById(R.id.challengeTitleView);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            secondUserNameTV = (TextView) view.findViewById(R.id.secondUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
            twoVideoPlayers = (TwoVideoPlayers) view.findViewById(R.id.twoVideoPlayers);
            firstUserIV = (RoundedImageView) view.findViewById(R.id.firstUserIV);
            secondUserIV = (RoundedImageView) view.findViewById(R.id.secondUserIV);

        }
    }

    private class TopRatedVideoHolder extends RecyclerView.ViewHolder {
        private RecyclerView topRatedVideosRV;

        private TopRatedVideoHolder(View itemView) {
            super(itemView);
            topRatedVideosRV = (RecyclerView) itemView.findViewById(R.id.topRatedVideosRV);
            topRatedVideosRV.setLayoutManager(new CustomLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            topRatedVideosAdapter = new TopRatedVideosAdapter(context, new ArrayList<AllVideoModel>());
            topRatedVideosRV.setAdapter(topRatedVideosAdapter);
        }
    }

    public UploadedAllVideoAdapter(Context context, ArrayList<AllVideoModel> categoryModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.allVideoModels = categoryModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;


        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_video_item, parent, false);
                return new SingleVideoHolder(itemView);

            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.uploaded_battle_video_item, parent, false);
                return new BattleVideoHolder(itemView);

            case 2:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.top_rated_videos_rv, parent, false);
                return new TopRatedVideoHolder(itemView);
            default:
                return null;
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BattleVideoHolder) {
            BattleVideoHolder battleVideoHolder = (BattleVideoHolder) holder;
            setupBattleViewHolder(battleVideoHolder, allVideoModels.get(position));
        }
        if (holder instanceof SingleVideoHolder) {
            SingleVideoHolder singleVideoHolder = (SingleVideoHolder) holder;
            setupSingleViewHolder(singleVideoHolder, allVideoModels.get(position));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });


    }

    private void setupSingleViewHolder(SingleVideoHolder holder, AllVideoModel allVideoModel) {
        holder.videoTitleView.setUp(allVideoModel.getTitle(), this, holder.getAdapterPosition());
        holder.firstVideoPlayer.setVideoUrl(allVideoModel.getVideo_url());
        holder.firstVideoPlayer.setThumbnail(allVideoModel.getThumbnail());
        imageLoading.LoadImage(allVideoModel.getProfileimage(), holder.firstUserIV, null);
        holder.firstUserNameTV.setText(allVideoModel.getFirst_name());
        holder.commentsTV.setText(setComment(allVideoModel));
        holder.uploadedTimeTV.setText(Utils.getChallengeTimeDifference(allVideoModel.getEdate()));
        setUpMoreIvButtonVisibilityForSingleVideo(holder, allVideoModel);
    }

    private void setupBattleViewHolder(final BattleVideoHolder holder, AllVideoModel allVideoModel) {
        holder.challengeTitleView.setUp(allVideoModel.getTitle(), this, holder.getAdapterPosition());
        setUpMoreIvButtonVisibility(holder, allVideoModel);
        holder.twoVideoPlayers.setVideoUrls(allVideoModel.getVideo_url(), allVideoModel.getVideo_url1());
        holder.twoVideoPlayers.setThumbnail(allVideoModel.getThumbnail(), allVideoModel.getThumbnail1());
        imageLoading.LoadImage(allVideoModel.getProfileimage(), holder.firstUserIV, null);
        imageLoading.LoadImage(allVideoModel.getProfileimage1(), holder.secondUserIV, null);
        holder.firstUserNameTV.setText(allVideoModel.getFirst_name());
        holder.secondUserNameTV.setText(allVideoModel.getFirst_name1());
        holder.commentsTV.setText(setComment(allVideoModel));
        holder.uploadedTimeTV.setText(Utils.getChallengeTimeDifference(allVideoModel.getEdate()));
    }

    private void setUpMoreIvButtonVisibilityForSingleVideo(SingleVideoHolder holder, AllVideoModel allVideoModel) {
        String currentCustomerID = MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID);
        if (!currentCustomerID.equals(allVideoModel.getCustomer_id()) && !currentCustomerID.equals(allVideoModel.getCustomer_id1()))
            holder.videoTitleView.showHideMoreIvButton(false);
        else
            holder.videoTitleView.showHideMoreIvButton(true);
    }

    private void setUpMoreIvButtonVisibility(BattleVideoHolder holder, AllVideoModel allVideoModel) {
        String currentCustomerID = MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID);
        if (!currentCustomerID.equals(allVideoModel.getCustomer_id()) && !currentCustomerID.equals(allVideoModel.getCustomer_id1()))
            holder.challengeTitleView.showHideMoreIvButton(false);
        else
            holder.challengeTitleView.showHideMoreIvButton(true);
    }

    @NonNull
    private String setComment(AllVideoModel allVideoModel) {
        return context.getResources().getQuantityString(R.plurals.numberOfComments, allVideoModel.getVideo_comment_count(), allVideoModel.getVideo_comment_count());
    }


    @Override
    public int getItemCount() {
        return allVideoModels.size();
    }

    @Override
    public int getItemViewType(int position) {


        if (position == 0)
            return 2;
        else if (allVideoModels.get(position).getType().equals("Challenge"))
            return 1;
        else return 0;


    }

    public void addData(ArrayList<AllVideoModel> allVideoModels) {
        this.allVideoModels = allVideoModels;
        this.allVideoModels.add(0, new AllVideoModel());
        notifyDataSetChanged();
    }

    public void addDataToBanner(ArrayList<AllVideoModel> allVideoModels) {
        topRatedVideosAdapter.addVideos(allVideoModels);
    }

    @Override
    public void onPopupMenuClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.addVideoToBannerTV:
                recyclerViewClick.onClickItem(position, view);
                break;
            case R.id.addVideoToPremiumTV:
                recyclerViewClick.onClickItem(position, view);
                break;
            case R.id.deleteVideoTV:
                break;
        }
    }
}

