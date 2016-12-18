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

import com.example.divyanshu.smyt.Adapters.CommentsAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.CustomViews.VideoTitleView;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.DeleteVideoInterface;
import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.Models.CommentModel;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.Models.VideoDetailModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.InAppLocalApis;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.Utils.Validation;
import com.example.divyanshu.smyt.activities.InAppActivity;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;
import com.player.divyanshu.customvideoplayer.MediaPlayerHelper;
import com.player.divyanshu.customvideoplayer.SingleVideoPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_BANNER;
import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_TO_PREMIUM;
import static com.example.divyanshu.smyt.activities.InAppActivity.PREMIUM_CATEGORY_BANNER;


public class UserVideoDescActivity extends BaseActivity implements View.OnClickListener, PopupItemClicked, InAppLocalApis.InAppAvailabilityCalBack {

    @InjectView(R.id.firstVideoPlayer)
    SingleVideoPlayer firstVideoPlayer;
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
    @InjectView(R.id.userOneVideoLikeIV)
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
    @InjectView(R.id.firstUserIV)
    RoundedImageView firstUserIV;
    @InjectView(R.id.videoTitleView)
    VideoTitleView videoTitleView;


    private Validation validation;
    private HashMap<View, String> validationMap;
    private VideoDetailModel videoDetailModel;
    private CommentsAdapter commentsAdapter;
    private CommentModel deleteCommentModel;
    private ImageLoading imageLoading;
    private String customerVideoID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_video_desc);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {

        validation = new Validation();
        validation.addValidationField(new ValidationModel(commentsET, Validation.TYPE_EMPTY_FIELD_VALIDATION, getString(R.string.err_cno_comment)));
        commentsRV.setLayoutManager(new LinearLayoutManager(this));
        imageLoading = new ImageLoading(this);
    }

    @OnClick({R.id.sendCommentIV, R.id.leftSideVotingView})
    public void onClick(View v) {
        if (v.getId() == R.id.sendCommentIV)
            sendComment();
        else
            checkAndSendLike();

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
                //decreaseCommentCount();
                break;
            case ApiCodes.ADD_REMOVE_LIKE:
                break;
        }
    }

    private void updateUI() {
        videoTitleView.setUp(videoDetailModel.getTitle(), this, 0);
        imageLoading.LoadImage(videoDetailModel.getProfileimage(), firstUserIV, null);
        setLikeCountInUI();
        firstUserNameTV.setText(videoDetailModel.getFirst_name());
        setupVideo();
        commentsAdapter = new CommentsAdapter(this, videoDetailModel.getCommentArray(), this);
        commentsRV.setAdapter(commentsAdapter);
        updateCommentsCount();
        setLikeIV();
        String currentCustomerID = MySharedPereference.getInstance().getString(this, Constants.CUSTOMER_ID);
        if (!currentCustomerID.equals(videoDetailModel.getCustomer_id()))
            videoTitleView.showHideMoreIvButton(false);
    }

    private void decreaseCommentCount() {
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
        firstVideoPlayer.setVideoUrl(videoDetailModel.getVideo_url());
        firstVideoPlayer.setThumbnail(videoDetailModel.getThumbnail());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoDetailModel == null) {
            customerVideoID = getIntent().getStringExtra(Constants.CUSTOMERS_VIDEO_ID);
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
        setLikeCountInUI();
    }

    private void setLikeIV() {
        if (videoDetailModel.getLikestatus() == 1) {
            CommonFunctions.changeImageWithAnimation(this, videoLikeIV, R.drawable.thumb_on);
        } else {
            CommonFunctions.changeImageWithAnimation(this, videoLikeIV, R.drawable.thumb_off);
        }
    }


    private void setLikeCountInUI() {
        userOneVoteCountTV.setText(String.valueOf(videoDetailModel.getLikes()));
    }

    private void updateCommentsCount() {
        BroadcastSenderClass.getInstance().sendCommentCountBroadcast(this, videoDetailModel.getCustomers_videos_id(), videoDetailModel.getVideo_comment_count());
        //commentsAdapter.sendLocalBroadCastForCommentCount(videoDetailModel.getCustomers_videos_id(), videoDetailModel.getVideo_comment_count());
        String commentsFound = getResources().getQuantityString(R.plurals.numberOfComments, videoDetailModel.getVideo_comment_count(), videoDetailModel.getVideo_comment_count());
        commentsTV.setText(commentsFound);
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
    public void onBackPressed() {
        MediaPlayerHelper.getInstance().releaseAllVideos();
        super.onBackPressed();
    }

    @Override
    public void onPopupMenuClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.addVideoToBannerTV:
                if (MySharedPereference.getInstance().getString(this, Constants.CATEGORY_ID).equals(getString(R.string.premium_category)))
                    checkAndPayForBannerVideo(PREMIUM_CATEGORY_BANNER);
                else
                    checkAndPayForBannerVideo(OTHER_CATEGORY_BANNER);
                break;
            case R.id.addVideoToPremiumTV:
                checkAndPayForAddVideoToPremium(OTHER_CATEGORY_TO_PREMIUM);
                break;
            case R.id.deleteVideoTV:
                CommonFunctions.getInstance().deleteVideo(this, videoDetailModel.getCustomers_videos_id(), new DeleteVideoInterface() {
                    @Override
                    public void onDeleteVideo() {
                        finish();
                    }
                });
                break;
        }
    }

    private void checkAndPayForBannerVideo(int purchaseType) {
        setUpAvailabilityPurchase(purchaseType);
        InAppLocalApis.getInstance().checkBannerAvailability(this, purchaseType);
    }

    private void checkAndPayForAddVideoToPremium(int purchaseType) {
        setUpAvailabilityPurchase(purchaseType);
        InAppLocalApis.getInstance().checkAddVideoInPremiumCatAvailability(this);
    }

    private void setUpAvailabilityPurchase(int purchaseType) {
        InAppLocalApis.getInstance().setCallback(this);
        InAppLocalApis.getInstance().setPurchaseType(purchaseType);

    }

    @Override
    public void available(int purchaseType) {
        switch (purchaseType) {
            case OTHER_CATEGORY_BANNER:
                InAppLocalApis.getInstance().addBannerToCategory(this, videoDetailModel.getCustomers_videos_id());
                break;
            case OTHER_CATEGORY_TO_PREMIUM:
                InAppLocalApis.getInstance().addVideoToPremiumCategory(this, videoDetailModel.getCustomers_videos_id());
                break;
            case PREMIUM_CATEGORY_BANNER:
                InAppLocalApis.getInstance().addBannerToCategory(this, videoDetailModel.getCustomers_videos_id());
                break;
        }
    }

    @Override
    public void notAvailable(int purchaseType) {
        Intent intent = new Intent(this, InAppActivity.class);
        intent.putExtra(Constants.IN_APP_TYPE, purchaseType);
        startActivityForResult(intent, InAppActivity.PURCHASE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == InAppActivity.PURCHASE_REQUEST) {

                if (data.getBooleanExtra(Constants.IS_PRCHASED, false)) {

                    int type = data.getIntExtra(Constants.TYPE, 0);
                    String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
                    String productID = data.getStringExtra(Constants.PRODUCT_ID);
                    switch (type) {
                        case OTHER_CATEGORY_BANNER:
                            InAppLocalApis.getInstance().purchaseBanner(this, transactionID, productID);
                            break;
                        case OTHER_CATEGORY_TO_PREMIUM:
                            InAppLocalApis.getInstance().purchaseCategory(this, transactionID, productID);
                            break;
                        case PREMIUM_CATEGORY_BANNER:
                            InAppLocalApis.getInstance().purchaseBanner(this, transactionID, productID);
                            break;
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
