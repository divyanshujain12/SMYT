package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.divyanshu.smyt.CustomViews.ReusedCodes;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.CustomViews.VideoTitleView;
import com.example.divyanshu.smyt.Interfaces.DeleteVideoInterface;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Interfaces.TitleBarButtonClickCallback;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.activities.OtherUserProfileActivity;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu on 3/28/2017.
 */

public class UserMusicPlayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TitleBarButtonClickCallback {

    public ArrayList<AllVideoModel> allVideoModels = new ArrayList<>();
    private ArrayList<AllVideoModel> bannerVideos;
    private Context context;
    private RecyclerViewClick recyclerViewClick;
    private ImageLoading imageLoading;


    private class MusicPlayerViewHolder extends RecyclerView.ViewHolder {
        private VideoTitleView videoTitleView;
        public TextView userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;
        private ImageView videoThumbIV;
        public FrameLayout videoFL, playMusicFL;
        private RoundedImageView firstUserIV;
        //private CustomMusicPlayer customMusicPlayer;
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
            playMusicFL = (FrameLayout) view.findViewById(R.id.playMusicFL);
            firstUserIV = (RoundedImageView) view.findViewById(R.id.firstUserIV);
            viewsCountTV = (TextView) view.findViewById(R.id.viewsCountTV);
            // customMusicPlayer = (CustomMusicPlayer) view.findViewById(R.id.customMusicPlayer);
            userOneLikesCountTV = (TextView) view.findViewById(R.id.userOneLikesCountTV);

        }
    }

    public UserMusicPlayerAdapter(Context context, ArrayList<AllVideoModel> categoryModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.allVideoModels = categoryModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
        //allVideoModels.add(0,new AllVideoModel());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.music_player_item, parent, false);
        return new MusicPlayerViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        setupMusicPlayerViewHolder((MusicPlayerViewHolder) holder, allVideoModels.get(position));

    }


    private void setupMusicPlayerViewHolder(final MusicPlayerViewHolder holder, final AllVideoModel allVideoModel) {
        setUpMusicPlayerTitleBar(holder, allVideoModel);
        // holder.customMusicPlayer.setMediaUrl(allVideoModel.getVideo_url());
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
        holder.playMusicFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });
    }


    private void setUpMusicPlayerTitleBar(MusicPlayerViewHolder holder, AllVideoModel allVideoModel) {
        holder.videoTitleView.setUpViewsForListing(allVideoModel.getTitle(), holder.getAdapterPosition(), allVideoModel.getCustomers_videos_id(), this);
        setUpMoreIvButtonVisibilityForMusicPlayer(holder, allVideoModel);
        holder.videoTitleView.setUpFavIVButton(allVideoModel.getFavourite_status());
    }

    private void setUpMoreIvButtonVisibilityForMusicPlayer(MusicPlayerViewHolder holder, AllVideoModel allVideoModel) {
        String currentCustomerID = MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID);
        if (!currentCustomerID.equals(allVideoModel.getCustomer_id()) && !currentCustomerID.equals(allVideoModel.getCustomer_id1()))
            holder.videoTitleView.showHideMoreIvButton(false);
        else
            holder.videoTitleView.showHideMoreIvButton(true);
    }

    @Override
    public int getItemCount() {
        return allVideoModels.size();
    }

    public void addNewData(ArrayList<AllVideoModel> allVideoModels) {
        this.allVideoModels = (allVideoModels);
        notifyDataSetChanged();
    }

    public void removeItem(int selectedVideoPos) {
        allVideoModels.remove(selectedVideoPos);
        notifyItemRemoved(selectedVideoPos);
        notifyItemRangeChanged(selectedVideoPos, getItemCount());
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
        allVideoModels.get(position).setFavourite_status(favStatus);
        notifyDataSetChanged();
        return favStatus;
    }
}
