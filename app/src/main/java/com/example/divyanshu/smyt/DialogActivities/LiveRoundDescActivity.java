package com.example.divyanshu.smyt.DialogActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.divyanshu.smyt.Adapters.CommentsAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.ChallengeRoundTitleView;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.CustomViews.TwoVideoPlayerCustomView;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.TitleBarButtonClickCallback;
import com.example.divyanshu.smyt.Models.ChallengeVideoDescModel;
import com.example.divyanshu.smyt.Models.CommentModel;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.InAppLocalApis;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.Utils.Validation;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerStandardTwo;

/**
 * Created by divyanshu.jain on 10/7/2016.
 */

public class LiveRoundDescActivity extends BaseActivity implements TitleBarButtonClickCallback {


    @InjectView(R.id.challengeTitleView)
    ChallengeRoundTitleView challengeTitleView;
    @InjectView(R.id.twoVideoPlayers)
    TwoVideoPlayerCustomView twoVideoPlayers;
    @InjectView(R.id.firstUserIV)
    RoundedImageView firstUserIV;
    @InjectView(R.id.firstUserNameTV)
    TextView firstUserNameTV;
    @InjectView(R.id.firstUserLL)
    LinearLayout firstUserLL;
    @InjectView(R.id.secondUserNameTV)
    TextView secondUserNameTV;
    @InjectView(R.id.secondUserIV)
    RoundedImageView secondUserIV;
    @InjectView(R.id.secondUserLL)
    LinearLayout secondUserLL;
    @InjectView(R.id.commentsTV)
    TextView commentsTV;
    @InjectView(R.id.viewsCountTV)
    TextView viewsCountTV;
    @InjectView(R.id.uploadedTimeTV)
    TextView uploadedTimeTV;
    @InjectView(R.id.userOneVideoLikeIV)
    ImageView userOneVideoLikeIV;
    @InjectView(R.id.userOneVoteCountTV)
    TextView userOneVoteCountTV;
    @InjectView(R.id.leftSideVotingView)
    LinearLayout leftSideVotingView;
    @InjectView(R.id.userTwoVoteCountTV)
    TextView userTwoVoteCountTV;
    @InjectView(R.id.userTwoVideoLikeIV)
    ImageView userTwoVideoLikeIV;
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
    @InjectView(R.id.votingStatusTV)
    TextView votingStatusTV;
    private Validation validation;
    private HashMap<View, String> validationMap;
    private String customerVideoID;
    private ChallengeVideoDescModel challengeVideoDescModel;
    private CommentsAdapter commentsAdapter;
    private CommentModel deleteCommentModel;
    private ImageLoading imageLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_battle_desc);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
        commentsRV.setLayoutManager(new LinearLayoutManager(this));
        imageLoading = new ImageLoading(this);
        validation = new Validation();
        validation.addValidationField(new ValidationModel(commentsET, Validation.TYPE_EMPTY_FIELD_VALIDATION, getString(R.string.err_cno_comment)));
        challengeTitleView.setUpViewsForListing("",  0,"",this);
        CommonFunctions.changeImageWithAnimation(this, userOneVideoLikeIV, R.drawable.thumb_off);
        CommonFunctions.changeImageWithAnimation(this, userTwoVideoLikeIV, R.drawable.thumb_off);
    }

    @OnClick({R.id.leftSideVotingView, R.id.rightSideVotingView, R.id.sendCommentIV})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.leftSideVotingView:
                sendVote(challengeVideoDescModel.getCustomer_id());
                challengeVideoDescModel.setVote(String.valueOf(Integer.parseInt(challengeVideoDescModel.getVote()) + 1));
                setVoteCount();
                setEnableDisableLikeImage(userOneVideoLikeIV);
                BroadcastSenderClass.getInstance().sendVoteCountBroadcastToLiveTab(this, challengeVideoDescModel.getChallenge_id(), challengeVideoDescModel.getVote(), 0);
                break;
            case R.id.rightSideVotingView:
                sendVote(challengeVideoDescModel.getCustomer_id1());
                challengeVideoDescModel.setVote1(String.valueOf(Integer.parseInt(challengeVideoDescModel.getVote1()) + 1));
                setEnableDisableLikeImage(userTwoVideoLikeIV);
                setVoteCount();
                BroadcastSenderClass.getInstance().sendVoteCountBroadcastToLiveTab(this, challengeVideoDescModel.getChallenge_id(), challengeVideoDescModel.getVote1(), 1);
                break;
            case R.id.sendCommentIV:
                sendComment();
                break;
        }
    }

    private void sendComment() {
        validationMap = validation.validate(this);
        commentsET.setText("");
        setCommentPBVisibility(View.VISIBLE);
        if (validationMap != null) {
            CallWebService.getInstance(this, false, ApiCodes.POST_COMMENT).hitJsonObjectRequestAPI(CallWebService.POST, API.ADD_COMMENT, createJsonForPostComment(), this);
        }
    }

    private void sendVote(String votingCustomerID) {
        CallWebService.getInstance(this, false, ApiCodes.VOTE).hitJsonObjectRequestAPI(CallWebService.POST, API.VOTE_UP, createJsonForVoting(votingCustomerID), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (challengeVideoDescModel == null) {
            customerVideoID = getIntent().getStringExtra(Constants.CUSTOMERS_VIDEO_ID);
            CallWebService.getInstance(this, true, ApiCodes.SINGLE_ROUND_VIDEO_DETAIL).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_SINGLE_ROUND_VIDEO_DETAIL, createJsonForGettingChallengeInfo(), this);
        }
    }


    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case ApiCodes.SINGLE_ROUND_VIDEO_DETAIL:
                challengeVideoDescModel = UniversalParser.getInstance().parseJsonObject(response.getJSONObject(Constants.DATA), ChallengeVideoDescModel.class);
                updateUI();
                break;
            case ApiCodes.POST_COMMENT:
                setCommentPBVisibility(View.GONE);
                CommentModel commentModel = UniversalParser.getInstance().parseJsonObject(response.getJSONObject(Constants.DATA), CommentModel.class);
                CommonFunctions.getInstance().showSuccessSnackBar(this, response.getString(Constants.MESSAGE));
                addNewCommentToList(commentModel);
                updateAndSendCommentsCount();
                break;
            case ApiCodes.DELETE_COMMENT:
                //updateAndSendCommentsCount();
                break;
        }
    }

    private void updateUI() {
        setVotingStatus();
        setUpUsersViews();
        challengeTitleView.setUpViewsForListing(challengeVideoDescModel.getTitle(),  0,challengeVideoDescModel.getCustomers_videos_id(),this);
        setVoteCount();
        setupVideo();
        seetUpCommentAdapter();
        updateAndSendCommentsCount();
        setVoteStatus();
        String currentCustomerID = MySharedPereference.getInstance().getString(this, Constants.CUSTOMER_ID);
        if (!currentCustomerID.equals(challengeVideoDescModel.getCustomer_id()) || !currentCustomerID.equals(challengeVideoDescModel.getCustomer_id1()))
            challengeTitleView.showHideMoreIvButton(false);

    }

    private void setVotingStatus() {
        if (Utils.isDifferenceLowerThanTwentyFourHours(challengeVideoDescModel.getRound_date())) {
            votingStatusTV.setText(R.string.voting_open);
        } else {
            votingStatusTV.setText(R.string.voting_closed);
        }
    }
    private void seetUpCommentAdapter() {
        commentsAdapter = new CommentsAdapter(this, challengeVideoDescModel.getCommentArray(), this);
        commentsRV.setAdapter(commentsAdapter);
    }

    private void setUpUsersViews() {
        imageLoading.LoadImage(challengeVideoDescModel.getProfileimage(), firstUserIV, null);
        imageLoading.LoadImage(challengeVideoDescModel.getProfileimage1(), secondUserIV, null);
        firstUserNameTV.setText(challengeVideoDescModel.getFirst_name());
        secondUserNameTV.setText(challengeVideoDescModel.getFirst_name1());
    }

    private void setVoteStatus() {
        switch (challengeVideoDescModel.getVote_status()) {
            case 1:
                setEnableDisableLikeImage(userOneVideoLikeIV);
                break;
            case 2:
                setEnableDisableLikeImage(userTwoVideoLikeIV);
                break;
        }
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        CallWebService.getInstance(this, false, ApiCodes.DELETE_COMMENT).hitJsonObjectRequestAPI(CallWebService.POST, API.DELETE_COMMENT, createJsonForDeleteComment(position), this);
        decreaseCommentCount();
    }

    private void decreaseCommentCount() {
        challengeVideoDescModel.setVideo_comment_count(challengeVideoDescModel.getVideo_comment_count() - 1);
        updateAndSendCommentsCount();
    }

    private void addNewCommentToList(CommentModel commentModel) {

        commentsAdapter.addNewComment(commentModel);
        challengeVideoDescModel.setVideo_comment_count(challengeVideoDescModel.getVideo_comment_count() + 1);
        updateAndSendCommentsCount();
    }

    private void setupVideo() {
        twoVideoPlayers.setUp(challengeVideoDescModel.getVideo_url(), challengeVideoDescModel.getVideo_url1(), challengeVideoDescModel.getThumbnail(), challengeVideoDescModel.getThumbnail1(), challengeVideoDescModel.getCustomers_videos_id());
    }

    private void setVoteCount() {
        userOneVoteCountTV.setText(String.valueOf(challengeVideoDescModel.getVote()));
        userTwoVoteCountTV.setText(String.valueOf(challengeVideoDescModel.getVote1()));
    }

    private void updateAndSendCommentsCount() {
        String commentsFound = getResources().getQuantityString(R.plurals.numberOfComments, challengeVideoDescModel.getVideo_comment_count(), challengeVideoDescModel.getVideo_comment_count());
        BroadcastSenderClass.getInstance().sendCommentCountBroadcastToLiveTab(this, challengeVideoDescModel.getChallenge_id(), challengeVideoDescModel.getVideo_comment_count());
        commentsTV.setText(commentsFound);
    }

    private void setCommentPBVisibility(int visibility) {
        commentPB.setVisibility(visibility);
    }

    private JSONObject createJsonForGettingChallengeInfo() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForPostComment() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, challengeVideoDescModel.getCustomers_videos_id());
            jsonObject.put(Constants.COMMENT, validationMap.get(commentsET));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForDeleteComment(int pos) {
        JSONObject jsonObject = new JSONObject();
        try {
            deleteCommentModel = commentsAdapter.getCommentModel(pos);
            jsonObject.put(Constants.CUSTOMER_VIDEO_COMMENT_ID, deleteCommentModel.getCustomers_videos_comment_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForVoting(String votingCustomerID) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, challengeVideoDescModel.getCustomers_videos_id());
            jsonObject.put(Constants.VOTING_CUSTOMER_ID, votingCustomerID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private void setEnableDisableLikeImage(ImageView imageView) {
        CommonFunctions.changeImageWithAnimation(this, imageView, R.drawable.thumb_on);
        leftSideVotingView.setEnabled(false);
        rightSideVotingView.setEnabled(false);
        leftSideVotingView.setClickable(false);
        rightSideVotingView.setClickable(false);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        InAppLocalApis.getInstance().sendPurchasedDataToBackend(this,requestCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayerStandard.backPress())
            return;
        else if (JCVideoPlayerStandardTwo.backPress())
            return;
        else {
          //  MediaPlayerHelper.getInstance().releaseAllVideos();
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseVideos();
    }

    private void releaseVideos() {
        JCVideoPlayerStandard.releaseAllVideos();
        JCVideoPlayerStandardTwo.releaseAllVideos();
      //  MediaPlayerHelper.getInstance().releaseAllVideos();
    }

    @Override
    public void onTitleBarButtonClicked(View view,int position) {

    }
}
