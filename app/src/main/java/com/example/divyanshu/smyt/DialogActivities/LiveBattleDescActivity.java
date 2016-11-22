package com.example.divyanshu.smyt.DialogActivities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.divyanshu.smyt.CustomViews.ChallengeTitleView;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;
import com.player.divyanshu.customvideoplayer.TwoVideoPlayers;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu.jain on 10/7/2016.
 */

public class LiveBattleDescActivity extends BaseActivity implements PopupItemClicked {

    @InjectView(R.id.twoVideoPlayers)
    TwoVideoPlayers twoVideoPlayers;
    @InjectView(R.id.playVideoIV)
    ImageView playVideoIV;
    @InjectView(R.id.firstUserNameTV)
    TextView firstUserNameTV;
    @InjectView(R.id.secondUserNameTV)
    TextView secondUserNameTV;
    @InjectView(R.id.fullscreenIV)
    ImageView fullscreenIV;
    @InjectView(R.id.fullscreenFL)
    FrameLayout fullscreenFL;
    @InjectView(R.id.playVideosIV)
    ImageView playVideosIV;
    @InjectView(R.id.videoFL)
    FrameLayout videoFL;
    @InjectView(R.id.commentsTV)
    TextView commentsTV;
    @InjectView(R.id.uploadedTimeTV)
    TextView uploadedTimeTV;
    @InjectView(R.id.videoLikeIV)
    ImageView videoLikeIV;
    @InjectView(R.id.userOneVoteCountTV)
    TextView userOneVoteCountTV;
    @InjectView(R.id.leftSideVotingView)
    LinearLayout leftSideVotingView;
    @InjectView(R.id.userTwoVoteCountTV)
    TextView userTwoVoteCountTV;
    @InjectView(R.id.rightSideVotingView)
    LinearLayout rightSideVotingView;
    @InjectView(R.id.voteRL)
    RelativeLayout voteRL;
    @InjectView(R.id.commentsRV)
    RecyclerView commentsRV;
    @InjectView(R.id.commentsET)
    EditText commentsET;
    @InjectView(R.id.sendCommentIV)
    ImageView sendCommentIV;
    @InjectView(R.id.commentPB)
    ProgressBar commentPB;
    @InjectView(R.id.commentBar)
    FrameLayout commentBar;

    @InjectView(R.id.firstUserIV)
    RoundedImageView firstUserIV;
    @InjectView(R.id.secondUserIV)
    RoundedImageView secondUserIV;
    @InjectView(R.id.challengeTitleView)
    ChallengeTitleView challengeTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_battle_desc);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
        challengeTitleView.setUp("", this, 0);
    }

    @OnClick({R.id.leftSideVotingView, R.id.rightSideVotingView, R.id.sendCommentIV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leftSideVotingView:
                break;
            case R.id.rightSideVotingView:
                break;
            case R.id.sendCommentIV:
                break;
        }
    }

    @Override
    public void onPopupMenuClicked(View view, int position) {

    }
}
