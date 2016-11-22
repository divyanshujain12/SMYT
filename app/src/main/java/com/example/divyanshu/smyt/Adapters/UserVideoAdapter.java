package com.example.divyanshu.smyt.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.CustomViews.CustomViewsHandler;
import com.example.divyanshu.smyt.CustomViews.VideoTitleView;
import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.InternetCheck;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;
import com.player.divyanshu.customvideoplayer.SingleVideoPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by divyanshu.jain on 9/1/2016.
 */
public class UserVideoAdapter extends RecyclerView.Adapter<UserVideoAdapter.SingleVideoHolder> implements SnackBarCallback, PopupItemClicked {

    public ArrayList<VideoModel> videoModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;
    private ImageLoading imageLoading;
    private PopupWindow popupWindow = null;
    private int selectedVideoPos = -1;

    public class SingleVideoHolder extends RecyclerView.ViewHolder {

        public TextView userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;

        public ImageView videoThumbIV;
        private VideoTitleView videoTitleView;
        public FrameLayout videoFL;
        private SingleVideoPlayer firstVideoPlayer;

        public SingleVideoHolder(View view) {
            super(view);
            videoTitleView = (VideoTitleView) view.findViewById(R.id.videoTitleView);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            videoThumbIV = (ImageView) view.findViewById(R.id.videoThumbIV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
            firstVideoPlayer = (SingleVideoPlayer) view.findViewById(R.id.firstVideoPlayer);
        }

    }

    public UserVideoAdapter(Context context, ArrayList<VideoModel> videoModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.videoModels = videoModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
        popupWindow = CustomViewsHandler.getInstance().createUserVideosPopupWindow(context, this, selectedVideoPos);
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
        holder.videoTitleView.setUp(videoModel.getTitle(), this, position);
        setUpMoreIV(holder, videoModel);
        String commentsFound = context.getResources().getQuantityString(R.plurals.numberOfComments, videoModel.getVideo_comment_count(), videoModel.getVideo_comment_count() / 1);
        holder.commentsTV.setText(commentsFound);
        holder.firstUserNameTV.setText(videoModel.getFirst_name());
        holder.firstVideoPlayer.setVideoUrl(videoModel.getVideo_url());
        holder.firstVideoPlayer.setThumbnail(videoModel.getThumbnail());
        holder.uploadedTimeTV.setText(Utils.getTimeDifference(videoModel.getEdate()));
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
    public void onPopupMenuClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.addVideoToBannerTV:
                recyclerViewClick.onClickItem(position, view);
                break;
            case R.id.addVideoToPremiumTV:
                recyclerViewClick.onClickItem(position, view);
                break;
            case R.id.deleteVideoTV:
                deleteVideoAlert();
                break;
        }
    }

    private void deleteVideoAlert() {
        CustomAlertDialogs.showAlertDialogWithCallBack(context, context.getString(R.string.alert), context.getString(R.string.delete_video_alert_msg), this);
    }


    @Override
    public int getItemCount() {
        return videoModels.size();
    }

    public void addUserVideoData(ArrayList<VideoModel> userVideoModels) {
        videoModels = userVideoModels;
        notifyDataSetChanged();
    }

    @Override
    public void doAction() {
        if (InternetCheck.isInternetOn(context)) {
            removeItem();
            CallWebService.getInstance(context, false, ApiCodes.DELETE_VIDEO).hitJsonObjectRequestAPI(CallWebService.POST, API.DELETE_CUSTOMER_VIDEO, createJsonForDeleteVideo(), null);
        } else {
            CommonFunctions.getInstance().showErrorSnackBar((Activity) context, context.getString(R.string.no_internet_connection));
        }
    }

    private void removeItem() {
        videoModels.remove(selectedVideoPos);
        notifyItemRemoved(selectedVideoPos);
        notifyItemRangeChanged(selectedVideoPos, getItemCount());
    }

    private JSONObject createJsonForDeleteVideo() {
        String videoId = videoModels.get(selectedVideoPos).getCustomers_videos_id();
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(context);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, videoId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}


