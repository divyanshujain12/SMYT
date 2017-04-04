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
import com.example.divyanshu.smyt.CustomViews.CustomMusicPlayer;
import com.example.divyanshu.smyt.CustomViews.ReusedCodes;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.CustomViews.VideoTitleView;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.DeleteVideoInterface;
import com.example.divyanshu.smyt.Interfaces.TitleBarButtonClickCallback;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.Models.CommentModel;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.Models.VideoDetailModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.InAppLocalApis;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.Utils.Validation;
import com.example.divyanshu.smyt.activities.OtherUserProfileActivity;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerStandardTwo;

public class MusicDescriptionActivity extends BaseActivity implements View.OnClickListener, TitleBarButtonClickCallback {

    @InjectView(R.id.customMusicPlayer)
    CustomMusicPlayer customMusicPlayer;
    @InjectView(R.id.videoTitleView)
    VideoTitleView videoTitleView;
    @InjectView(R.id.firstUserIV)
    RoundedImageView firstUserIV;
    @InjectView(R.id.firstUserNameTV)
    TextView firstUserNameTV;
    @InjectView(R.id.uploadedTimeTV)
    TextView uploadedTimeTV;
    @InjectView(R.id.firstUserLL)
    LinearLayout firstUserLL;
    @InjectView(R.id.commentsTV)
    TextView commentsTV;
    @InjectView(R.id.viewsCountTV)
    TextView viewsCountTV;
    @InjectView(R.id.userOneLikesCountTV)
    TextView userOneLikesCountTV;
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
    @InjectView(R.id.activity_music_description)
    RelativeLayout activityMusicDescription;
    @InjectView(R.id.playMusicFL)
    FrameLayout playMusicFL;

    private Validation validation;
    private HashMap<View, String> validationMap;
    private VideoDetailModel videoDetailModel;
    private CommentsAdapter commentsAdapter;
    private CommentModel deleteCommentModel;
    private ImageLoading imageLoading;
    private String customerVideoID = "";
    private boolean fromBanner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_description);
        ButterKnife.inject(this);

        initViews();
    }

    private void initViews() {
        fromBanner = getIntent().getBooleanExtra(Constants.FROM_BANNER, false);
        validation = new Validation();
        validation.addValidationField(new ValidationModel(commentsET, Validation.TYPE_EMPTY_FIELD_VALIDATION, getString(R.string.err_cno_comment)));
        commentsRV.setLayoutManager(new LinearLayoutManager(this));
        imageLoading = new ImageLoading(this);
    }

    @OnClick({R.id.sendCommentIV, R.id.userOneLikesCountTV, R.id.playMusicFL})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendCommentIV:
                sendComment();
                break;
            case R.id.userOneLikesCountTV:
                checkAndSendLike();
                break;
            case R.id.playMusicFL:
                customMusicPlayer.setVisibility(View.VISIBLE);
                customMusicPlayer.playAudio(0);
                break;
        }
    }


    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        CallWebService.getInstance(this, false, ApiCodes.DELETE_COMMENT).hitJsonObjectRequestAPI(CallWebService.POST, API.DELETE_COMMENT, createJsonForDeleteComment(position), this);
        decreaseCommentCount();
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
                break;
            case ApiCodes.ADD_REMOVE_LIKE:
                break;
        }
    }

    @Override
    public void onFailure(String str, int apiType) {
        super.onFailure(str, apiType);
        if (apiType == ApiCodes.POST_COMMENT)
            setCommentPBVisibility(View.GONE);
    }

    private void updateUI() {
        setUpTitleBarPopupWindow(videoDetailModel.getTitle());
        imageLoading.LoadImage(videoDetailModel.getProfileimage(), firstUserIV, null);
        commentsAdapter = new CommentsAdapter(this, videoDetailModel.getCommentArray(), this);
        commentsRV.setAdapter(commentsAdapter);
        firstUserNameTV.setText(videoDetailModel.getFirst_name());
        viewsCountTV.setText(ReusedCodes.getViews(this, videoDetailModel.getViews()));
        setLikeCountInUI();
        setLikeIV();
        setupVideo();
        updateCommentsCount();
        String currentCustomerID = MySharedPereference.getInstance().getString(this, Constants.CUSTOMER_ID);
        if (!currentCustomerID.equals(videoDetailModel.getCustomer_id()))
            videoTitleView.showHideMoreIvButton(false);
    }

    private void setUpTitleBarPopupWindow(String title) {
        if (fromBanner)
            videoTitleView.setUpForBannerVideo(videoDetailModel.getTitle(), 0, videoDetailModel.getCustomers_videos_id(), this);
        else
            videoTitleView.setUpForSingleVideo(videoDetailModel.getTitle(), 0, videoDetailModel.getCustomers_videos_id(), this);

        videoTitleView.setUpFavIVButton(videoDetailModel.getFavourite_status());
    }

    private void decreaseCommentCount() {
        videoDetailModel.setVideo_comment_count(videoDetailModel.getVideo_comment_count() - 1);
        updateCommentsCount();
    }


    private void addNewCommentToList(CommentModel commentModel) {
        commentsAdapter.addNewComment(commentModel);
        videoDetailModel.setVideo_comment_count(videoDetailModel.getVideo_comment_count() + 1);
        updateCommentsCount();
    }

    private void setupVideo() {
        AllVideoModel allVideoModel = new AllVideoModel();
        allVideoModel.setVideo_url(videoDetailModel.getVideo_url());
        allVideoModel.setTitle(videoDetailModel.getTitle());
        ArrayList<AllVideoModel> allVideoModels = new ArrayList<>();
        allVideoModels.add(allVideoModel);
        customMusicPlayer.initialize(allVideoModels);
    }


    private void sendComment() {
        validationMap = validation.validate(this);
        commentsET.setText("");
        //setCommentPBVisibility(View.VISIBLE);
        if (validationMap != null) {
            CallWebService.getInstance(this, false, ApiCodes.POST_COMMENT).hitJsonObjectRequestAPI(CallWebService.POST, API.ADD_COMMENT, createJsonForPostComment(), this);
        }
    }


    private void checkAndSendLike() {
        updateModelForLikesCount();
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
        BroadcastSenderClass.getInstance().sendLikesCountBroadcast(this, videoDetailModel.getCustomers_videos_id(), videoDetailModel.getLikes());
        setLikeCountInUI();
    }

    private void setLikeIV() {
        if (videoDetailModel.getLikestatus() == 1) {
            userOneLikesCountTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cv_like_active, 0, 0);
        } else {
            userOneLikesCountTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cv_like_inactive, 0, 0);
        }
    }


    private void setLikeCountInUI() {
        userOneLikesCountTV.setText(ReusedCodes.getLikes(this, String.valueOf(videoDetailModel.getLikes())));
    }

    private void updateCommentsCount() {
        BroadcastSenderClass.getInstance().sendCommentCountBroadcast(this, videoDetailModel.getCustomers_videos_id(), videoDetailModel.getVideo_comment_count());
        commentsTV.setText(ReusedCodes.getComment(this, videoDetailModel.getVideo_comment_count()));
    }


    private void setCommentPBVisibility(int visibility) {
        commentPB.setVisibility(visibility);
    }

    private JSONObject createJsonForGettingVideoInfo() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
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
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
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
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        InAppLocalApis.getInstance().sendPurchasedDataToBackend(this, requestCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.firstUserLL)
    public void onClick() {
        if (!CommonFunctions.getInstance().isThisMe(this, videoDetailModel.getCustomer_id())) {
            Intent intent = new Intent(this, OtherUserProfileActivity.class);
            intent.putExtra(Constants.CUSTOMER_ID, videoDetailModel.getCustomer_id());
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        customMusicPlayer.stopService();
            //      MediaPlayerHelper.getInstance().releaseAllVideos();
            super.onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseVideos();
    }

    private void releaseVideos() {
        JCVideoPlayerStandard.releaseAllVideos();
        JCVideoPlayerStandardTwo.releaseAllVideos();
        //     MediaPlayerHelper.getInstance().releaseAllVideos();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoDetailModel == null) {
            customerVideoID = getIntent().getStringExtra(Constants.CUSTOMERS_VIDEO_ID);
            CallWebService.getInstance(this, true, ApiCodes.SINGLE_VIDEO_DATA).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_VIDEO_DETAIL, createJsonForGettingVideoInfo(), this);
        }
    }

    @Override
    public void onTitleBarButtonClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.deleteVideoTV:
                CommonFunctions.getInstance().deleteVideo(this, videoDetailModel.getCustomers_videos_id(), new DeleteVideoInterface() {
                    @Override
                    public void onDeleteVideo() {
                        BroadcastSenderClass.getInstance().reloadAllVideoData(MusicDescriptionActivity.this);
                        finish();
                    }
                });
                break;
            case R.id.favIV:
                CallWebService.getInstance(this, false, ApiCodes.ACTION_FAVORITE).hitJsonObjectRequestAPI(CallWebService.POST, API.ACTION_FAVORITE, CommonFunctions.getInstance().createJsonForActionFav(this, videoDetailModel.getCustomers_videos_id(), updateUiForFavClick((ImageView) view, position)), null);
                break;
        }
    }

    private int updateUiForFavClick(ImageView view, int position) {
        int favStatus = videoDetailModel.getFavourite_status();
        if (favStatus == 0) {
            view.setImageResource(R.drawable.ic_fav_select);
            favStatus = 1;
        } else {
            view.setImageResource(R.drawable.ic_fav_un_select);
            favStatus = 0;
        }
        videoDetailModel.setFavourite_status(favStatus);
        AllVideoModel allVideoModel = new AllVideoModel();
        allVideoModel.setCustomers_videos_id(videoDetailModel.getCustomers_videos_id());
        if (customerVideoID.equals(MySharedPereference.getInstance().getString(this, Constants.CUSTOMER_ID)))
            BroadcastSenderClass.getInstance().sendVideoFavoriteBroadcast(this, allVideoModel, favStatus);
        return favStatus;
    }


}