package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.SingleVideoPlayerCustomView;
import com.example.divyanshu.smyt.CustomViews.VideoTitleView;
import com.example.divyanshu.smyt.Interfaces.TitleBarButtonClickCallback;
import com.example.divyanshu.smyt.Interfaces.DeleteVideoInterface;
import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;


/**
 * Created by divyanshu.jain on 9/1/2016.
 */
public class UserVideoAdapter extends RecyclerView.Adapter<UserVideoAdapter.SingleVideoHolder> implements PopupItemClicked, TitleBarButtonClickCallback {

    public ArrayList<VideoModel> videoModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;
    private ImageLoading imageLoading;

    private String categoryID = "";


    public class SingleVideoHolder extends RecyclerView.ViewHolder {

        public TextView userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;

        public ImageView videoThumbIV;
        private VideoTitleView videoTitleView;
        public FrameLayout videoFL;
        private SingleVideoPlayerCustomView singleVideoPlayerView;
        private TextView viewsCountTV;

        public SingleVideoHolder(View view) {
            super(view);
            videoTitleView = (VideoTitleView) view.findViewById(R.id.videoTitleView);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            videoThumbIV = (ImageView) view.findViewById(R.id.videoThumbIV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
            singleVideoPlayerView = (SingleVideoPlayerCustomView) view.findViewById(R.id.singleVideoPlayerView);
            viewsCountTV = (TextView) view.findViewById(R.id.viewsCountTV);

        }

    }

    public UserVideoAdapter(Context context, ArrayList<VideoModel> videoModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.videoModels = videoModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
        categoryID = MySharedPereference.getInstance().getString(context, Constants.CATEGORY_ID);
    }

    @Override
    public UserVideoAdapter.SingleVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_video_item, parent, false);
        return new SingleVideoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserVideoAdapter.SingleVideoHolder holder, int position) {
        VideoModel videoModel = videoModels.get(position);
        holder.videoTitleView.setUpViewsForListing(videoModel.getTitle(), holder.getAdapterPosition(), videoModel.getCustomers_videos_id(), this);
        setUpMoreIV(holder, videoModel);
        String commentsFound = context.getResources().getQuantityString(R.plurals.numberOfComments, videoModel.getVideo_comment_count(), videoModel.getVideo_comment_count() / 1);
        holder.commentsTV.setText(commentsFound);
        holder.firstUserNameTV.setText(videoModel.getFirst_name());
        holder.singleVideoPlayerView.setUp(videoModel.getVideo_url(), videoModel.getThumbnail(), videoModel.getCustomers_videos_id());
        holder.uploadedTimeTV.setText(Utils.getChallengeTimeDifference(videoModel.getEdate()));
        holder.viewsCountTV.setText(videoModel.getViews());
        holder.commentsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });
    }

    private void setUpMoreIV(SingleVideoHolder holder, VideoModel videoModel) {
        String currentCustomerID = MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID);
        if (!currentCustomerID.equals(videoModel.getCustomer_id()))
            holder.videoTitleView.showHideMoreIvButton(false);
        else
            holder.videoTitleView.showHideMoreIvButton(true);
    }

    @Override
    public void onPopupMenuClicked(View view, final int position) {
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

    @Override
    public int getItemCount() {
        return videoModels.size();
    }


    public void addUserVideoData(ArrayList<VideoModel> userVideoModels) {
        videoModels = userVideoModels;
        notifyDataSetChanged();
    }

    public void removeItem(int selectedVideoPos) {
        videoModels.remove(selectedVideoPos);
        notifyItemRemoved(selectedVideoPos);
        notifyItemRangeChanged(selectedVideoPos, getItemCount());
    }

    @Override
    public void onTitleBarButtonClicked(View view, final int position) {


        switch (view.getId()) {
            case R.id.deleteVideoTV:
                CommonFunctions.getInstance().deleteVideo(context, videoModels.get(position).getCustomers_videos_id(), new DeleteVideoInterface() {
                    @Override
                    public void onDeleteVideo() {
                        removeItem(position);
                    }
                });
                break;
            case R.id.favIV:
                CallWebService.getInstance(context, false, ApiCodes.ACTION_FAVORITE).hitJsonObjectRequestAPI(CallWebService.POST, API.ACTION_FAVORITE, CommonFunctions.getInstance().createJsonForActionFav(context, videoModels.get(position).getCustomers_videos_id(), updateUiForFavClick((ImageView) view, position)), null);
                break;
        }

    }

    private int updateUiForFavClick(ImageView view, int position) {
        int favStatus = videoModels.get(position).getFavourite_status();
        if (favStatus == 0) {
            view.setImageResource(R.drawable.icon_heart_on);
            favStatus = 1;
        } else {
            view.setImageResource(R.drawable.icon_heart_off);
            favStatus = 0;
        }
        videoModels.get(position).setFavourite_status(favStatus);
        notifyDataSetChanged();
        return favStatus;
    }
}