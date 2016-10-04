package com.example.divyanshu.smyt.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.CustomViews.CustomViewsHandler;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.InternetCheck;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by divyanshu.jain on 9/1/2016.
 */
public class UserVideoAdapter extends RecyclerView.Adapter<UserVideoAdapter.SingleVideoHolder> implements View.OnClickListener, SnackBarCallback {

    public ArrayList<VideoModel> videoModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;
    private ImageLoading imageLoading;
    private PopupWindow popupWindow = null;
    private int deleteVideoPos = -1;

    public class SingleVideoHolder extends RecyclerView.ViewHolder {

        public TextView titleTV, userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;

        public ImageView videoThumbIV, moreIV;
        public FrameLayout videoFL;
        private JCVideoPlayerStandard firstVideoPlayer;

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
            firstVideoPlayer = (JCVideoPlayerStandard) view.findViewById(R.id.firstVideoPlayer);
        }

    }

    public UserVideoAdapter(Context context, ArrayList<VideoModel> videoModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.videoModels = videoModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
        popupWindow = CustomViewsHandler.createPopupWindow(context, this);
    }

    @Override
    public UserVideoAdapter.SingleVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_video_item, parent, false);
        return new SingleVideoHolder(itemView);


    }


    @Override
    public void onBindViewHolder(final UserVideoAdapter.SingleVideoHolder holder, final int position) {

        VideoModel userModel = videoModels.get(position);
        holder.titleTV.setText(userModel.getTitle());

        String commentsFound = context.getResources().getQuantityString(R.plurals.numberOfComments, userModel.getVideo_comment_count(), userModel.getVideo_comment_count() / 1);
        holder.commentsTV.setText(commentsFound);
        holder.firstUserNameTV.setText(userModel.getFirst_name());
        boolean setUp = holder.firstVideoPlayer.setUp(userModel.getVideo_url(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        if (setUp) {
            imageLoading.LoadImage(userModel.getThumbnail(), holder.firstVideoPlayer.thumbImageView, null);
        }

        holder.moreIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMoreIvClick(v, position);
            }
        });


        holder.commentsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(position, v);
            }
        });
    }

    private void onMoreIvClick(View view, final int position) {
        deleteVideoPos = position;
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
        else
            popupWindow.showAsDropDown(view);

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
    public void onClick(View v) {
        dismissPopupWindow();
        CustomAlertDialogs.showAlertDialogWithCallBack(context, context.getString(R.string.alert), context.getString(R.string.delete_video_alert_msg), this);

    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    @Override
    public void doAction() {
        if (InternetCheck.isInternetOn(context)) {
            removeItem();
            //CallWebService.getInstance(context, false, ApiCodes.DELETE_VIDEO).hitJsonObjectRequestAPI(CallWebService.POST, API.DELETE_CUSTOMER_VIDEO, createJsonForDeleteVideo(), null);
        } else {
            CommonFunctions.getInstance().showErrorSnackBar((Activity) context, context.getString(R.string.no_internet_connection));
        }
    }

    private void removeItem() {
        videoModels.remove(deleteVideoPos);
        notifyItemRemoved(deleteVideoPos);
        notifyItemRangeChanged(deleteVideoPos, getItemCount());
    }

    private JSONObject createJsonForDeleteVideo() {
        String videoId = videoModels.get(deleteVideoPos).getCustomers_videos_id();
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(context);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, videoId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}


