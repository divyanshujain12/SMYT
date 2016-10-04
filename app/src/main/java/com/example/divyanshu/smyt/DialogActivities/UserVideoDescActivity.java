package com.example.divyanshu.smyt.DialogActivities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.divyanshu.smyt.Adapters.CommentsAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.CommentModel;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.Models.VideoDetailModel;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.InternetCheck;
import com.example.divyanshu.smyt.Utils.Validation;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class UserVideoDescActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.titleTV)
    TextView titleTV;
    @InjectView(R.id.moreIV)
    ImageView moreIV;
    @InjectView(R.id.firstVideoPlayer)
    JCVideoPlayerStandard firstVideoPlayer;
    @InjectView(R.id.videoThumbIV)
    ImageView videoThumbIV;
    @InjectView(R.id.playVideoIV)
    ImageView playVideoIV;
    @InjectView(R.id.firstUserNameTV)
    TextView firstUserNameTV;
    @InjectView(R.id.videoFL)
    FrameLayout videoFL;
    @InjectView(R.id.commentsTV)
    TextView commentsTV;
    @InjectView(R.id.uploadedTimeTV)
    TextView uploadedTimeTV;
    @InjectView(R.id.userOneVoteCountTV)
    TextView userOneVoteCountTV;
    @InjectView(R.id.videoLikeIV)
    ImageView videoLikeIV;
    @InjectView(R.id.leftSideVotingView)
    LinearLayout leftSideVotingView;
    @InjectView(R.id.commentsRV)
    RecyclerView commentsRV;
    @InjectView(R.id.commentsET)
    EditText commentsET;
    @InjectView(R.id.commentBar)
    FrameLayout commentBar;
    @InjectView(R.id.sendCommentIV)
    ImageView sendCommentIV;
    @InjectView(R.id.commentPB)
    ProgressBar commentPB;
    private Validation validation;
    private HashMap<View, String> validationMap;

    private VideoDetailModel videoDetailModel;
    private CommentsAdapter commentsAdapter;
    private CommentModel deleteCommentModel;
    private VideoModel videoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_video_desc);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
        validation = new Validation();
        validation.addValidationField(new ValidationModel(commentsET, Validation.TYPE_EMPTY_FIELD_VALIDATION, "Please Enter Comment First!"));
        commentsRV.setLayoutManager(new LinearLayoutManager(this));

    }

    @OnClick({R.id.sendCommentIV, R.id.leftSideVotingView})
    public void onClick(View v) {
        if (v.getId() == R.id.sendCommentIV) {
            if (InternetCheck.isInternetOn(this))
                sendComment();
            else
            CommonFunctions.getInstance().showErrorSnackBar(this, getString(R.string.no_internet_connection));
        } else {
            if (InternetCheck.isInternetOn(this))
                checkAndSendLike();
            else
                CommonFunctions.getInstance().showErrorSnackBar(this, getString(R.string.no_internet_connection));
        }
    }


    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        CallWebService.getInstance(this, false, ApiCodes.DELETE_COMMENT).hitJsonObjectRequestAPI(CallWebService.POST, API.DELETE_COMMENT, createJsonForDeleteComment(position), this);
        setCommentCount();
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case ApiCodes.SINGLE_VIDEO_DATA:
                videoDetailModel = UniversalParser.getInstance().parseJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS).getJSONObject(0), VideoDetailModel.class);
                updateUI();
                break;
            case ApiCodes.POST_COMMENT:
                setCommentPBVisibility(View.GONE);
                CommentModel commentModel = UniversalParser.getInstance().parseJsonObject(response.getJSONObject(Constants.DATA), CommentModel.class);
                CommonFunctions.getInstance().showSuccessSnackBar(this, response.getString(Constants.MESSAGE));
                addNewCommentToList(commentModel);
                updateCommentsCount();
                break;
            case ApiCodes.DELETE_COMMENT:
                //setCommentCount();
                break;
            case ApiCodes.ADD_REMOVE_LIKE:
                break;
        }
    }

    private void setCommentCount() {
        videoDetailModel.setVideo_comment_count(videoDetailModel.getVideo_comment_count() - 1);
        updateCommentsCount();
    }

    @Override
    public void onFailure(String str, int apiType) {
        super.onFailure(str, apiType);
        if (apiType == ApiCodes.POST_COMMENT)
            setCommentPBVisibility(View.GONE);
    }

    private void addNewCommentToList(CommentModel commentModel) {

        commentsAdapter.addNewComment(commentModel);
        videoDetailModel.setVideo_comment_count(videoDetailModel.getVideo_comment_count() + 1);
        updateCommentsCount();
    }

    private void setupVideo() {
        boolean popUp = firstVideoPlayer.setUp(videoDetailModel.getVideo_url(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        if (popUp)
            new ImageLoading(this).LoadImage(videoDetailModel.getThumbnail(), firstVideoPlayer.thumbImageView, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoDetailModel == null) {
            videoModel = getIntent().getExtras().getParcelable(Constants.USER_VIDEO);
            CallWebService.getInstance(this, true, ApiCodes.SINGLE_VIDEO_DATA).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_VIDEO_DETAIL, createJsonForGettingVideoInfo(), this);
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


    private void checkAndSendLike() {
        updateModelForLikesCount();
        setLikeCount();
        CallWebService.getInstance(this, false, ApiCodes.ADD_REMOVE_LIKE).hitJsonObjectRequestAPI(CallWebService.POST, API.LIKE_UNLIKE_VIDEO, createJsonForAddRemoveLike(), this);
    }

    private void updateModelForLikesCount() {
        int likesCount = Integer.parseInt(videoDetailModel.getLikes());

        if (videoDetailModel.getLikestatus() == 0) {
            likesCount = likesCount + 1;
            videoDetailModel.setLikestatus(1);
        } else {
            likesCount = likesCount - 1;
            videoDetailModel.setLikestatus(0);
        }
        setLikeIV();
        videoDetailModel.setLikes(String.valueOf(likesCount));
    }

    private void setLikeIV() {
        if (videoDetailModel.getLikestatus() == 1) {
            CommonFunctions.changeImageWithAnimation(this, videoLikeIV, R.drawable.thumb_on);
        } else {
            CommonFunctions.changeImageWithAnimation(this, videoLikeIV, R.drawable.thumb_off);
        }
    }

    private void updateUI() {
        titleTV.setText(videoDetailModel.getTitle());
        setLikeCount();
        firstUserNameTV.setText(videoDetailModel.getFirst_name());
        setupVideo();
        commentsAdapter = new CommentsAdapter(this, videoDetailModel.getCommentArray(), this);
        commentsRV.setAdapter(commentsAdapter);
        updateCommentsCount();
        setLikeIV();
    }

    private void setLikeCount() {
        userOneVoteCountTV.setText(String.valueOf(videoDetailModel.getLikes()));
    }

    private void updateCommentsCount() {
        commentsAdapter.sendLocalBroadCastForCommentCount(videoDetailModel.getCustomers_videos_id(), videoDetailModel.getVideo_comment_count());
        String commentsFound = getResources().getQuantityString(R.plurals.numberOfComments, videoDetailModel.getVideo_comment_count(), videoDetailModel.getVideo_comment_count());
        commentsTV.setText(commentsFound);
    }


    private void setCommentPBVisibility(int visibility) {
        commentPB.setVisibility(visibility);
    }

    private JSONObject createJsonForGettingVideoInfo() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, videoModel.getCustomers_videos_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForAddRemoveLike() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, videoDetailModel.getCustomers_videos_id());
            jsonObject.put(Constants.LIKES, String.valueOf(videoDetailModel.getLikestatus()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private JSONObject createJsonForPostComment() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, videoDetailModel.getCustomers_videos_id());
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
}
