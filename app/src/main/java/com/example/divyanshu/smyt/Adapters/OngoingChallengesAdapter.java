package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.CustomViews.ChallengeTitleView;
import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;
import com.player.divyanshu.customvideoplayer.TwoVideoPlayers;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class OngoingChallengesAdapter extends RecyclerView.Adapter<OngoingChallengesAdapter.BattleVideoHolder> implements PopupItemClicked {

    private ArrayList<ChallengeModel> challengeModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;
    private ImageLoading imageLoading;

    public class BattleVideoHolder extends RecyclerView.ViewHolder {


        public TextView userTimeTV, commentsTV, uploadedTimeTV;
        private ChallengeTitleView challengeTitleView;
        public FrameLayout videoFL;
        private ImageView playVideosIV;
        private FrameLayout fullscreenFL;
        private ImageView fullscreenIV;
        private TwoVideoPlayers twoVideoPlayers;
        private ImageView firstUserIV, secondUserIV;
        TextView firstUserNameTV, secondUserNameTV;
        TextView userOneVoteCountTV,userTwoVoteCountTV;

        public BattleVideoHolder(View view) {
            super(view);
            challengeTitleView = (ChallengeTitleView) view.findViewById(R.id.challengeTitleView);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            secondUserNameTV = (TextView) view.findViewById(R.id.secondUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            playVideosIV = (ImageView) view.findViewById(R.id.playVideosIV);
            fullscreenFL = (FrameLayout) view.findViewById(R.id.fullscreenFL);
            fullscreenIV = (ImageView) view.findViewById(R.id.fullscreenIV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
            firstUserIV = (ImageView) view.findViewById(R.id.firstUserIV);
            secondUserIV = (ImageView) view.findViewById(R.id.secondUserIV);
            twoVideoPlayers = (TwoVideoPlayers) view.findViewById(R.id.twoVideoPlayers);
            userOneVoteCountTV = (TextView) view.findViewById(R.id.userOneVoteCountTV);
            userTwoVoteCountTV = (TextView) view.findViewById(R.id.userTwoVoteCountTV);
        }
    }

    public OngoingChallengesAdapter(Context context, ArrayList<ChallengeModel> categoryModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.challengeModels = categoryModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
    }

    @Override
    public BattleVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.live_battle_video_item, parent, false);
        return new BattleVideoHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final BattleVideoHolder holder, int position) {
        ChallengeModel challengeModel = challengeModels.get(position);

        holder.challengeTitleView.setUp(challengeModel.getTitle(), this, position);
        holder.challengeTitleView.showHideMoreIvButton(false);

        holder.commentsTV.setText(setCommentCount(challengeModel));
        holder.twoVideoPlayers.setVideoUrls(challengeModel.getVideo_url(), challengeModel.getVideo_url1());
        holder.twoVideoPlayers.setThumbnail(challengeModel.getThumbnail(), challengeModel.getThumbnail1());
        imageLoading.LoadImage(challengeModel.getProfileimage(), holder.firstUserIV, null);
        imageLoading.LoadImage(challengeModel.getProfileimage1(), holder.secondUserIV, null);
        holder.firstUserNameTV.setText(challengeModel.getFirst_name());
        holder.secondUserNameTV.setText(challengeModel.getFirst_name1());
        holder.uploadedTimeTV.setText(Utils.getTimeDifference(challengeModel.getEdate()));
        holder.userOneVoteCountTV.setText(challengeModel.getVote());
        holder.userTwoVoteCountTV.setText(challengeModel.getVote1());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(), v);
            }
        });

    }


    @Override
    public int getItemCount() {
        return challengeModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public void onPopupMenuClicked(View view, int position) {

    }

    @NonNull
    private String setCommentCount(ChallengeModel challengeModel) {
        return context.getResources().getQuantityString(R.plurals.numberOfComments, challengeModel.getVideo_comment_count(), challengeModel.getVideo_comment_count());
    }

    public void addItem(ArrayList<ChallengeModel> challengeModels) {
        this.challengeModels.addAll(challengeModels);
        notifyDataSetChanged();
    }
}


