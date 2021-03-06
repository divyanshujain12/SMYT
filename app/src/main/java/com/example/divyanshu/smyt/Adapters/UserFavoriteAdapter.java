package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.ChallengeRoundTitleView;
import com.example.divyanshu.smyt.CustomViews.CustomMusicPlayer;
import com.example.divyanshu.smyt.CustomViews.ReusedCodes;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.CustomViews.SingleVideoPlayerCustomView;
import com.example.divyanshu.smyt.CustomViews.TwoVideoPlayerCustomView;
import com.example.divyanshu.smyt.CustomViews.VideoTitleView;
import com.example.divyanshu.smyt.Interfaces.DeleteVideoInterface;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Interfaces.TitleBarButtonClickCallback;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.CustomLinearLayoutManager;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.activities.OtherUserProfileActivity;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by divyanshuPC on 2/2/2017.
 */

public class UserFavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TitleBarButtonClickCallback {

    public ArrayList<AllVideoModel> allVideoModels = new ArrayList<>();
    private ArrayList<AllVideoModel> bannerVideos;
    private Context context;
    private RecyclerViewClick recyclerViewClick;
    private ImageLoading imageLoading;
    private String currentProfileCustomerID = "";

    public UserFavoriteAdapter(Context context, ArrayList<AllVideoModel> categoryModels, String currentProfileCustomerID, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.allVideoModels = categoryModels;
        this.context = context;
        this.currentProfileCustomerID = currentProfileCustomerID;
        imageLoading = new ImageLoading(context);
        //allVideoModels.add(0,new AllVideoModel());
    }

    private class SingleVideoHolder extends RecyclerView.ViewHolder {
        private VideoTitleView videoTitleView;
        public TextView userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;
        private ImageView videoThumbIV;
        public FrameLayout videoFL;
        private RoundedImageView firstUserIV;
        private SingleVideoPlayerCustomView singleVideoPlayerView;
        private LinearLayout firstUserLL;
        private TextView viewsCountTV;
        private TextView userOneLikesCountTV;

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

    private class MusicPlayerViewHolder extends RecyclerView.ViewHolder {
        private VideoTitleView videoTitleView;
        public TextView userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;
        private ImageView videoThumbIV;
        public FrameLayout videoFL;
        private RoundedImageView firstUserIV;
        private CustomMusicPlayer customMusicPlayer;
        private LinearLayout firstUserLL;
        private TextView viewsCountTV;
        private TextView userOneLikesCountTV;

        private MusicPlayerViewHolder(View view) {
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
            customMusicPlayer = (CustomMusicPlayer) view.findViewById(R.id.customMusicPlayer);
            userOneLikesCountTV = (TextView) view.findViewById(R.id.userOneLikesCountTV);

        }
    }

    private class BattleVideoHolder extends RecyclerView.ViewHolder {
        private ChallengeRoundTitleView challengeTitleView;
        public TextView userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV, secondUserNameTV;
        public FrameLayout videoFL;
        // private TwoVideoPlayers twoVideoPlayers;
        private TwoVideoPlayerCustomView twoVideoPlayers;
        private RoundedImageView firstUserIV, secondUserIV;
        private LinearLayout firstUserLL, secondUserLL;
        private TextView viewsCountTV;
        private TextView userOneLikesCountTV, userTwoLikesCountTV;

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
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
            twoVideoPlayers = (TwoVideoPlayerCustomView) view.findViewById(R.id.twoVideoPlayers);
            viewsCountTV = (TextView) view.findViewById(R.id.viewsCountTV);
            firstUserIV = (RoundedImageView) view.findViewById(R.id.firstUserIV);
            secondUserIV = (RoundedImageView) view.findViewById(R.id.secondUserIV);
            userOneLikesCountTV = (TextView) view.findViewById(R.id.userOneLikesCountTV);
            userTwoLikesCountTV = (TextView) view.findViewById(R.id.userTwoLikesCountTV);

        }
    }

    private class TopRatedVideoHolder extends RecyclerView.ViewHolder {
        private RecyclerView topRatedVideosRV;
        private TopRatedVideosAdapter topRatedVideosAdapter;

        private TopRatedVideoHolder(View itemView) {
            super(itemView);
            topRatedVideosRV = (RecyclerView) itemView.findViewById(R.id.topRatedVideosRV);
            topRatedVideosRV.setLayoutManager(new CustomLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            topRatedVideosAdapter = new TopRatedVideosAdapter(context, new ArrayList<AllVideoModel>());
            topRatedVideosRV.setAdapter(topRatedVideosAdapter);
        }
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
                        .inflate(R.layout.music_player_item, parent, false);
                return new MusicPlayerViewHolder(itemView);
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
        if (holder instanceof MusicPlayerViewHolder) {
            setupMusicPlayerViewHolder((MusicPlayerViewHolder) holder, allVideoModels.get(position));
        }
    }


    private void setupSingleViewHolder(final SingleVideoHolder holder, final AllVideoModel allVideoModel) {
        setUpSingleVideoTitleBar(holder, allVideoModel);
        holder.singleVideoPlayerView.setUp(allVideoModel.getVideo_url(), allVideoModel.getThumbnail(), allVideoModel.getCustomers_videos_id());
        imageLoading.LoadImage(allVideoModel.getProfileimage(), holder.firstUserIV, null);
        holder.firstUserNameTV.setText(allVideoModel.getFirst_name());
        holder.uploadedTimeTV.setText(Utils.getChallengeTimeDifference(allVideoModel.getEdate()));
        holder.userOneLikesCountTV.setText(ReusedCodes.getLikes(context, allVideoModel.getLikes()));
        holder.commentsTV.setText(ReusedCodes.getComment(context, allVideoModel.getVideo_comment_count()));
        holder.viewsCountTV.setText(ReusedCodes.getViews(context, allVideoModel.getViews()));
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

    private void setupMusicPlayerViewHolder(final MusicPlayerViewHolder holder, final AllVideoModel allVideoModel) {
        setUpMusicPlayerTitleBar(holder, allVideoModel);
        //holder.customMusicPlayer.setMediaUrl(allVideoModel.getVideo_url());
        imageLoading.LoadImage(allVideoModel.getProfileimage(), holder.firstUserIV, null);
        holder.firstUserNameTV.setText(allVideoModel.getFirst_name());
        holder.userOneLikesCountTV.setText(ReusedCodes.getLikes(context, allVideoModel.getLikes()));
        holder.commentsTV.setText(ReusedCodes.getComment(context, allVideoModel.getVideo_comment_count()));
        holder.viewsCountTV.setText(ReusedCodes.getViews(context, allVideoModel.getViews()));
        holder.uploadedTimeTV.setText(Utils.getChallengeTimeDifference(allVideoModel.getEdate()));
        holder.firstUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserDetailActivity(allVideoModel.getCustomer_id());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(),view);
            }
        });
    }

    private void setupBattleViewHolder(final BattleVideoHolder holder, final AllVideoModel allVideoModel) {
        //String title = allVideoModel.getTitle() + "(" + allVideoModel.getRound_no() + ")";
        setUpBattleTitleBar(holder, allVideoModel);
        holder.twoVideoPlayers.setUp(allVideoModel.getVideo_url(), allVideoModel.getVideo_url1(), allVideoModel.getThumbnail(), allVideoModel.getThumbnail1(), allVideoModel.getCustomers_videos_id());
        imageLoading.LoadImage(allVideoModel.getProfileimage(), holder.firstUserIV, null);
        imageLoading.LoadImage(allVideoModel.getProfileimage1(), holder.secondUserIV, null);
        holder.firstUserNameTV.setText(allVideoModel.getFirst_name());
        holder.secondUserNameTV.setText(allVideoModel.getFirst_name1());
        holder.userOneLikesCountTV.setText(ReusedCodes.getUserOneVote(context, allVideoModel.getVote()));
        holder.userTwoLikesCountTV.setText(ReusedCodes.getUserTwoVote(context, allVideoModel.getVote1()));
        holder.commentsTV.setText(ReusedCodes.getComment(context, allVideoModel.getVideo_comment_count()));
        holder.viewsCountTV.setText(ReusedCodes.getViews(context, allVideoModel.getViews()));
        holder.uploadedTimeTV.setText(Utils.getChallengeTimeDifference(allVideoModel.getEdate()));


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
        holder.videoTitleView.setUpViewsForFavVideosListing(allVideoModel.getTitle(), holder.getAdapterPosition(), allVideoModel.getCustomers_videos_id(), this);
        setUpMoreIvButtonVisibilityForSingleVideo(holder, allVideoModel);
        holder.videoTitleView.setUpFavIVButton(allVideoModel.getFavourite_status());
    }

    private void setUpMusicPlayerTitleBar(MusicPlayerViewHolder holder, AllVideoModel allVideoModel) {
        holder.videoTitleView.setUpViewsForListing(allVideoModel.getTitle(), holder.getAdapterPosition(), allVideoModel.getCustomers_videos_id(), this);
        setUpMoreIvButtonVisibilityForMusicPlayer(holder, allVideoModel);
        holder.videoTitleView.setUpFavIVButton(allVideoModel.getFavourite_status());
    }

    private void setUpBattleTitleBar(BattleVideoHolder holder, AllVideoModel allVideoModel) {
        holder.challengeTitleView.setUpViewsForFavVideosListing(allVideoModel.getTitle() + "(" + allVideoModel.getRound_no() + ")", holder.getAdapterPosition(), allVideoModel.getCustomers_videos_id(), this);
        setUpMoreIvButtonVisibility(holder, allVideoModel);
        holder.challengeTitleView.setUpFavIVButton(allVideoModel.getFavourite_status());
    }


    private void setUpMoreIvButtonVisibilityForSingleVideo(SingleVideoHolder holder, AllVideoModel allVideoModel) {
        String currentCustomerID = MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID);
        if (!currentCustomerID.equals(currentProfileCustomerID)) {
            holder.videoTitleView.showHideMoreIvButton(false);
            holder.videoTitleView.hideFavButton();
        } else {
            holder.videoTitleView.showHideMoreIvButton(true);
        }
    }

    private void setUpMoreIvButtonVisibilityForMusicPlayer(MusicPlayerViewHolder holder, AllVideoModel allVideoModel) {
        String currentCustomerID = MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID);
        if (!currentCustomerID.equals(allVideoModel.getCustomer_id()) && !currentCustomerID.equals(allVideoModel.getCustomer_id1()))
            holder.videoTitleView.showHideMoreIvButton(false);
        else
            holder.videoTitleView.showHideMoreIvButton(true);
    }

    private void setUpMoreIvButtonVisibility(BattleVideoHolder holder, AllVideoModel allVideoModel) {
        String currentCustomerID = MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID);
        if (!currentCustomerID.equals(currentProfileCustomerID)) {
            holder.challengeTitleView.showHideMoreIvButton(false);
            holder.challengeTitleView.hideFavButton();
        } else
            holder.challengeTitleView.showHideMoreIvButton(true);
    }


    @Override
    public int getItemCount() {
        return allVideoModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (allVideoModels.get(position).getType().equals("Challenge"))
            return 1;
        if (allVideoModels.get(position).getType().equalsIgnoreCase("Mp3"))
            return 2;
        else return 0;
    }

    public void updateData(ArrayList<AllVideoModel> allVideoModels) {
        this.allVideoModels = allVideoModels;
        //this.allVideoModels.add(0, new AllVideoModel());
        notifyDataSetChanged();
    }

    public void addNewData(ArrayList<AllVideoModel> allVideoModels) {
        this.allVideoModels = (allVideoModels);
        notifyDataSetChanged();
    }

    public void addDataToBannerArray(ArrayList<AllVideoModel> allVideoModels) {
        bannerVideos = allVideoModels;
        notifyDataSetChanged();
    }

    public void removeItem(int selectedVideoPos) {
        allVideoModels.remove(selectedVideoPos);
        notifyItemRemoved(selectedVideoPos);
        notifyItemRangeChanged(selectedVideoPos, getItemCount());
    }

    private JSONObject createJsonForUpdateViewsCount(String customerVideoID) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(context);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void goToUserDetailActivity(String customer_id) {
        if (!customer_id.equals("") && !customer_id.equals(MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID))) {
            Intent intent = new Intent(context, OtherUserProfileActivity.class);
            intent.putExtra(Constants.CUSTOMER_ID, customer_id);
            context.startActivity(intent);
        }
    }

    @Override
    public void onTitleBarButtonClicked(View view, final int position) {
        switch (view.getId()) {
            case R.id.deleteVideoTV:
                CommonFunctions.getInstance().deleteVideo(context, allVideoModels.get(position).getCustomers_videos_id(), new DeleteVideoInterface() {
                    @Override
                    public void onDeleteVideo() {
                        removeItem(position);
                    }
                });
                break;
            case R.id.favIV:
                CallWebService.getInstance(context, false, ApiCodes.ACTION_FAVORITE).hitJsonObjectRequestAPI(CallWebService.POST, API.ACTION_FAVORITE, CommonFunctions.getInstance().createJsonForActionFav(context, allVideoModels.get(position).getCustomers_videos_id(), updateUiForFavClick((ImageView) view, position)), null);
                break;
        }

    }

    private int updateUiForFavClick(ImageView view, int position) {
        int favStatus = allVideoModels.get(position).getFavourite_status();
        if (favStatus == 0) {
            view.setImageResource(R.drawable.ic_fav_select);
            favStatus = 1;
        } else {
            view.setImageResource(R.drawable.ic_fav_un_select);
            favStatus = 0;
        }

        BroadcastSenderClass.getInstance().sendVideoFavoriteBroadcast(context, allVideoModels.get(position), favStatus);
        removeItem(position);
        return favStatus;
    }
}
