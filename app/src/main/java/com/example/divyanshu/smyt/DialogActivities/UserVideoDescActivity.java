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
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
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
                //  CommentModel commentModel = UniversalParser.getInstance().parseJsonObject(response.getJSONObject(Constants.DATA), CommentModel.class);
                CommonFunctions.getInstance().showSuccessSnackBar(this, response.getString(Constants.MESSAGE));
                //addNewCommentToList(commentModel);
                updateCommentsCount();
                break;
            case ApiCodes.DELETE_COMMENT:
                videoDetailModel.setVideo_comment_count(videoDetailModel.getVideo_comment_count() - 1);
                commentsAdapter.removeComment(deleteCommentModel);
                updateCommentsCount();
                break;
        }
    }

    @Override
    public void onFailure(String str, int apiType) {
        super.onFailure(str, apiType);
        if (apiType == ApiCodes.POST_COMMENT)
            setCommentPBVisibility(View.GONE);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        CallWebService.getInstance(this, false, ApiCodes.DELETE_COMMENT).hitJsonObjectRequestAPI(CallWebService.POST, API.DELETE_COMMENT, createJsonForDeleteComment(position), this);
    }

    private void updateUI() {
        titleTV.setText(videoDetailModel.getTitle());
        userOneVoteCountTV.setText(String.valueOf(videoDetailModel.getLikes()));
        firstUserNameTV.setText(videoDetailModel.getFirst_name());
        setupVideo();
        commentsAdapter = new CommentsAdapter(this, videoDetailModel.getCommentArray(), this);
        commentsRV.setAdapter(commentsAdapter);
        updateCommentsCount();
    }

    private void updateCommentsCount() {
        commentsAdapter.sendLocalBroadCastForCommentCount(videoDetailModel.getCustomers_videos_id(), videoDetailModel.getVideo_comment_count());
        String commentsFound = getResources().getQuantityString(R.plurals.numberOfComments, videoDetailModel.getVideo_comment_count(), videoDetailModel.getVideo_comment_count());
        commentsTV.setText(commentsFound);
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
            String videoID = getIntent().getExtras().getString(Constants.CUSTOMERS_VIDEO_ID);
            CallWebService.getInstance(this, true, ApiCodes.SINGLE_VIDEO_DATA).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_VIDEO_DETAIL, createJsonForGettingVideoInfo(videoID), this);
        }
    }


    @Override
    public void onClick(View v) {

    }

    @OnClick(R.id.sendCommentIV)
    public void onClick() {
        validationMap = validation.validate(this);
        setCommentPBVisibility(View.VISIBLE);
        if (validationMap != null) {
            CallWebService.getInstance(this, false, ApiCodes.POST_COMMENT).hitJsonObjectRequestAPI(CallWebService.POST, API.ADD_COMMENT, createJsonForPostComment(), this);
        }
    }


    private void setCommentPBVisibility(int visibility) {
        commentPB.setVisibility(visibility);
    }

    private JSONObject createJsonForGettingVideoInfo(String videoId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, videoId);
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
