package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.ChallengeRoundTitleView;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.CustomViews.SingleVideoPlayerCustomView;
import com.example.divyanshu.smyt.CustomViews.TwoVideoPlayerCustomView;
import com.example.divyanshu.smyt.CustomViews.VideoTitleView;
import com.example.divyanshu.smyt.Interfaces.TitleBarButtonClickCallback;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.activities.OtherUserProfileActivity;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;


/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class OngoingChallengesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TitleBarButtonClickCallback {

    private ArrayList<AllVideoModel> allVideoModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;
    private ImageLoading imageLoading;


    private class SingleVideoHolder extends RecyclerView.ViewHolder {
        private VideoTitleView videoTitleView;
        public TextView userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;
        private ImageView videoThumbIV;
        private FrameLayout videoFL;
        private RoundedImageView firstUserIV;
        private SingleVideoPlayerCustomView singleVideoPlayerView;
        private LinearLayout firstUserLL;
        private TextView viewsCountTV;
        TextView userOneLikesCountTV;

        private SingleVideoHolder(View view) {
            super(view);
            videoTitleView = (VideoTitleView) view.findViewById(R.id.videoTitleView);
            firstUserLL = (LinearLayout) view.findViewById(R.id.firstUserLL);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            videoThumbIV = (ImageView) view.findViewById(R.id.videoThumbIV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
            firstUserIV = (RoundedImageView) view.findViewById(R.id.firstUserIV);
            viewsCountTV = (TextView) view.findViewById(R.id.viewsCountTV);
            singleVideoPlayerView = (SingleVideoPlayerCustomView) view.findViewById(R.id.singleVideoPlayerView);
            userOneLikesCountTV = (TextView) view.findViewById(R.id.userOneLikesCountTV);

        }
    }


    private class BattleVideoHolder extends RecyclerView.ViewHolder {
        public TextView userTimeTV, commentsTV, uploadedTimeTV;
        private ChallengeRoundTitleView challengeTitleView;
        private TwoVideoPlayerCustomView twoVideoPlayers;
        private ImageView firstUserIV, secondUserIV;
        TextView firstUserNameTV, secondUserNameTV;
        TextView userOneLikesCountTV, userTwoLikesCountTV;
        private LinearLayout firstUserLL, secondUserLL;

        private BattleVideoHolder(View view) {
            super(view);
            challengeTitleView = (ChallengeRoundTitleView) view.findViewById(R.id.challengeTitleView);
            firstUserLL = (LinearLayout) view.findViewById(R.id.firstUserLL);
            secondUserLL = (LinearLayout) view.findViewById(R.id.secondUserLL);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            secondUserNameTV = (TextView) view.findViewById(R.id.secondUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            firstUserIV = (ImageView) view.findViewById(R.id.firstUserIV);
            secondUserIV = (ImageView) view.findViewById(R.id.secondUserIV);
            twoVideoPlayers = (TwoVideoPlayerCustomView) view.findViewById(R.id.twoVideoPlayers);
            userOneLikesCountTV = (TextView) view.findViewById(R.id.userOneLikesCountTV);
            userTwoLikesCountTV = (TextView) view.findViewById(R.id.userTwoLikesCountTV);
        }
    }

    public OngoingChallengesAdapter(Context context, ArrayList<AllVideoModel> allVideoModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.allVideoModels = allVideoModels;
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
                        .inflate(R.layout.live_battle_video_item, parent, false);
                return new BattleVideoHolder(itemView);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final AllVideoModel allVideoModel = allVideoModels.get(position);
        if (holder instanceof SingleVideoHolder)
            setupSingleViewHolder((SingleVideoHolder) holder, allVideoModel);
        else if (holder instanceof BattleVideoHolder)
            setupBattleViewHolder((BattleVideoHolder) holder, position, allVideoModel);


    }

    private void setupSingleViewHolder(final SingleVideoHolder holder, final AllVideoModel allVideoModel) {
        setUpSingleVideoTitleBar(holder, allVideoModel);
        holder.singleVideoPlayerView.setUp(allVideoModel.getVideo_url(), allVideoModel.getThumbnail(), allVideoModel.getCustomers_videos_id());
        holder.viewsCountTV.setText(allVideoModel.getViews());
        holder.userOneLikesCountTV.setText(allVideoModel.getLikes());
        imageLoading.LoadImage(allVideoModel.getProfileimage(), holder.firstUserIV, null);
        holder.firstUserNameTV.setText(allVideoModel.getFirst_name());
        holder.commentsTV.setText(setComment(allVideoModel));
        holder.uploadedTimeTV.setText(Utils.getChallengeTimeDifference(allVideoModel.getEdate()));
        holder.firstUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserDetailActivity(allVideoModel.getCustomer_id());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });
    }


    private void setupBattleViewHolder(final BattleVideoHolder holder, int position, final AllVideoModel allVideoModel) {
        setUpBattleTitleBar(holder, allVideoModel);
        holder.commentsTV.setText(setCommentCount(allVideoModel));
        holder.twoVideoPlayers.setUp(allVideoModel.getVideo_url(), allVideoModel.getVideo_url1(), allVideoModel.getThumbnail(), allVideoModel.getThumbnail1(), allVideoModel.getCustomers_videos_id());
        imageLoading.LoadImage(allVideoModel.getProfileimage(), holder.firstUserIV, null);
        imageLoading.LoadImage(allVideoModel.getProfileimage1(), holder.secondUserIV, null);
        holder.firstUserNameTV.setText(allVideoModel.getFirst_name());
        holder.secondUserNameTV.setText(allVideoModel.getFirst_name1());
        holder.uploadedTimeTV.setText(Utils.getChallengeTimeDifference(allVideoModel.getEdate()));
        holder.userOneLikesCountTV.setText(allVideoModel.getVote());
        holder.userTwoLikesCountTV.setText(allVideoModel.getVote1());
        holder.firstUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserDetailActivity(allVideoModel.getCustomer_id());
            }
        });
        holder.secondUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserDetailActivity(allVideoModel.getCustomer_id1());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });
    }

    private void setUpSingleVideoTitleBar(SingleVideoHolder holder, AllVideoModel allVideoModel) {
        holder.videoTitleView.setUpViewsForListing(allVideoModel.getTitle(), holder.getAdapterPosition(), allVideoModel.getCustomers_videos_id(), this);
        holder.videoTitleView.showHideMoreIvButton(false);
        holder.videoTitleView.hideFavButton();
    }

    private void setUpBattleTitleBar(BattleVideoHolder holder, AllVideoModel allVideoModel) {
        holder.challengeTitleView.setUpViewsForListing(allVideoModel.getTitle(), holder.getAdapterPosition(), allVideoModel.getCustomers_videos_id(), this);
        holder.challengeTitleView.showHideMoreIvButton(false);
        holder.challengeTitleView.hideFavButton();
    }

    private String setComment(AllVideoModel allVideoModel) {
        return context.getResources().getQuantityString(R.plurals.numberOfComments, allVideoModel.getVideo_comment_count(), allVideoModel.getVideo_comment_count());
    }

    @Override
    public int getItemCount() {
        return allVideoModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (allVideoModels.get(position).getType().equals("Challenge"))
            return 1;
        else return 0;
    }

    @NonNull
    private String setCommentCount(AllVideoModel allVideoModel) {
        return String.valueOf(allVideoModel.getVideo_comment_count());
        //return context.getResources().getQuantityString(R.plurals.numberOfComments, allVideoModel.getVideo_comment_count(), allVideoModel.getVideo_comment_count());
    }

    public void addItem(ArrayList<AllVideoModel> challengeModels) {
        this.allVideoModels = challengeModels;
        notifyDataSetChanged();
    }

    private void goToUserDetailActivity(String customer_id) {
        if (!customer_id.equals("") && !customer_id.equals(MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID))) {
            Intent intent = new Intent(context, OtherUserProfileActivity.class);
            intent.putExtra(Constants.CUSTOMER_ID, customer_id);
            context.startActivity(intent);
        }
    }

    @Override
    public void onTitleBarButtonClicked(View view, int position) {

    }

}


