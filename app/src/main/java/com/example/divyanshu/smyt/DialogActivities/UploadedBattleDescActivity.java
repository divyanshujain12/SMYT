package com.example.divyanshu.smyt.DialogActivities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
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

public class UploadedBattleDescActivity extends BaseActivity {
    @InjectView(R.id.titleTV)
    TextView titleTV;
    @InjectView(R.id.moreIV)
    ImageView moreIV;
    @InjectView(R.id.twoVideoPlayers)
    TwoVideoPlayers firstVideoPlayer;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_battle_desc);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {

    }

    @OnClick(R.id.sendCommentIV)
    public void onClick() {
    }
}
