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

import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by divyanshu.jain on 9/1/2016.
 */
public class UserVideoAdapter extends RecyclerView.Adapter<UserVideoAdapter.SingleVideoHolder> implements View.OnClickListener {

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
        createPopupWindow();
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
        /*PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.inflate(R.menu.video_menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                CommonFunctions.getInstance().showErrorSnackBar((Activity) context, String.valueOf(position));
                return true;
            }
        });
        popup.show();*/
    }

    @Override
    public int getItemCount() {
        return videoModels.size();
    }

    public void addUserVideoData(ArrayList<VideoModel> userVideoModels) {
        videoModels = userVideoModels;
        notifyDataSetChanged();
    }

    private void createPopupWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.single_text_view_in_cardview, null);
        TextView textView = (TextView) view.findViewById(R.id.singleTV);
        textView.setText(context.getString(R.string.delete_video));
        textView.setOnClickListener(this);
        textView.setBackgroundColor(context.getResources().getColor(R.color.white));
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
    }

    @Override
    public void onClick(View v) {
        dismissPopupWindow();
        CommonFunctions.getInstance().showErrorSnackBar((Activity) context, String.valueOf(deleteVideoPos));
    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }
}

