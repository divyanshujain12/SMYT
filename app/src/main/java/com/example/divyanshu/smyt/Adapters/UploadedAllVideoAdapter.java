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
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.CustomViews.SingleVideoPlayerCustomView;
import com.example.divyanshu.smyt.CustomViews.TwoVideoPlayerCustomView;
import com.example.divyanshu.smyt.CustomViews.VideoTitleView;
import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.CustomLinearLayoutManager;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.activities.OtherUserProfileActivity;
import com.neopixl.pixlui.components.textview.TextView;
import com.player.divyanshu.customvideoplayer.PlayVideoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class UploadedAllVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PopupItemClicked, PlayVideoInterface {

    private ArrayList<AllVideoModel> allVideoModels = new ArrayList<>();
    private ArrayList<AllVideoModel> bannerVideos;
    private Context context;
    private RecyclerViewClick recyclerViewClick;
    private ImageLoading imageLoading;


    private class SingleVideoHolder extends RecyclerView.ViewHolder {
        private VideoTitleView videoTitleView;
        public TextView userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;
        private ImageView videoThumbIV;
        public FrameLayout videoFL;
        private RoundedImageView firstUserIV;
        private SingleVideoPlayerCustomView singleVideoPlayerView;
        private LinearLayout firstUserLL;
        private TextView viewsCountTV;

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

    public UploadedAllVideoAdapter(Context context, ArrayList<AllVideoModel> categoryModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.allVideoModels = categoryModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
        //allVideoModels.add(0,new AllVideoModel());
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
        if (holder instanceof TopRatedVideoHolder) {
            setUpTopRatedViewHolder((TopRatedVideoHolder) holder);
        }
    }

    private void setUpTopRatedViewHolder(TopRatedVideoHolder holder) {
        holder.topRatedVideosAdapter.addVideos(bannerVideos);
    }

    private void setupSingleViewHolder(final SingleVideoHolder holder, final AllVideoModel allVideoModel) {
        holder.videoTitleView.setUp(allVideoModel.getTitle(), this, holder.getAdapterPosition());
        setUpMoreIvButtonVisibilityForSingleVideo(holder, allVideoModel);
        //holder.singleVideoPlayerView.setUp(allVideoModel.getVideo_url(), allVideoModel.getThumbnail(), allVideoModel.getCustomer_id());
        holder.singleVideoPlayerView.setUp(context.getString(R.string.dummy_video_url), context.getString(R.string.dummy_image_url), allVideoModel.getCustomer_id());
        holder.viewsCountTV.setText(allVideoModel.getViews());
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

    private void setupBattleViewHolder(final BattleVideoHolder holder, final AllVideoModel allVideoModel) {
        String title = allVideoModel.getTitle() + "(" + allVideoModel.getRound_no() + ")";
        holder.challengeTitleView.setUp(title, this, holder.getAdapterPosition());
        setUpMoreIvButtonVisibility(holder, allVideoModel);
        holder.viewsCountTV.setText(allVideoModel.getViews());

        //holder.twoVideoPlayers.setUp(allVideoModel.getVideo_url(), allVideoModel.getThumbnail(), allVideoModel.getVideo_url1(), allVideoModel.getThumbnail1(), allVideoModel.getCustomers_videos_id());
        holder.twoVideoPlayers.setUp(context.getString(R.string.dummy_video_url), context.getString(R.string.dummy_video_url), context.getString(R.string.dummy_image_url), context.getString(R.string.dummy_image_url), allVideoModel.getCustomers_videos_id());
        imageLoading.LoadImage(allVideoModel.getProfileimage(), holder.firstUserIV, null);
        imageLoading.LoadImage(allVideoModel.getProfileimage1(), holder.secondUserIV, null);
        holder.firstUserNameTV.setText(allVideoModel.getFirst_name());
        holder.secondUserNameTV.setText(allVideoModel.getFirst_name1());
        holder.commentsTV.setText(setComment(allVideoModel));
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

    public void updateData(ArrayList<AllVideoModel> allVideoModels) {
        this.allVideoModels = allVideoModels;
        //this.allVideoModels.add(0, new AllVideoModel());
        notifyDataSetChanged();
    }

    public void addNewData(ArrayList<AllVideoModel> allVideoModels) {
        this.allVideoModels.addAll(allVideoModels);
        notifyDataSetChanged();
    }

    public void addDataToBannerArray(ArrayList<AllVideoModel> allVideoModels) {
        bannerVideos = allVideoModels;
        notifyDataSetChanged();
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
                recyclerViewClick.onClickItem(position, view);
                break;
        }
    }

    public void removeItem(int selectedVideoPos) {
        allVideoModels.remove(selectedVideoPos);
        notifyItemRemoved(selectedVideoPos);
        notifyItemRangeChanged(selectedVideoPos, getItemCount());
    }

    @Override
    public void onVideoPlay(int position) {
        CallWebService.getInstance(context, false, ApiCodes.UPDATE_VIDEO_VIEW_COUNT).hitJsonObjectRequestAPI(CallWebService.POST, API.UPDATE_VIDEO_VIEWS_COUNT, createJsonForUpdateViewsCount(allVideoModels.get(position).getCustomers_videos_id()), null);
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
}

