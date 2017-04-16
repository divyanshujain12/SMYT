package com.example.divyanshu.smyt.activities;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
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
import com.example.divyanshu.smyt.Interfaces.MusicPlayerClickEvent;
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
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.Utils.Validation;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class PlayMusicActivity extends BaseActivity implements MusicPlayerClickEvent, TitleBarButtonClickCallback {

    /*  @InjectView(R.id.toolbarView)
      Toolbar toolbarView;*/
    @InjectView(R.id.customMusicPlayer)
    CustomMusicPlayer customMusicPlayer;
    public static ArrayList<AllVideoModel> allVideoModels;
    @InjectView(R.id.musicThumbIV)
    ImageView musicThumbIV;
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
    @InjectView(R.id.musicBottomRL)
    RelativeLayout musicBottomRL;

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
    @InjectView(R.id.dragViewLL)
    RelativeLayout dragViewLL;
    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    @InjectView(R.id.audioNameTV)
    TextView audioNameTV;

    private int selectedSongPos = 0;
    private Menu menu;
    private ImageLoading imageLoading;
    private VideoDetailModel videoDetailModel;
    private CommentsAdapter commentsAdapter;
    private Validation validation;
    private HashMap<View, String> validationMap;
    private CommentModel deleteCommentModel;
    int DrawableImage[] = {R.drawable.g1, R.drawable.g3, R.drawable.g4, R.drawable.g6,  R.drawable.g2,R.drawable.g7, R.drawable.g8,R.drawable.g5, R.drawable.g9, R.drawable.g10};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_music);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
        selectedSongPos = getIntent().getIntExtra(Constants.SELECTED_SONG_POS, 0);
        customMusicPlayer.initialize(allVideoModels);
        customMusicPlayer.playAudio(selectedSongPos);
        customMusicPlayer.setMusicPlayerClickEvent(this);
        validation = new Validation();
        validation.addValidationField(new ValidationModel(commentsET, Validation.TYPE_EMPTY_FIELD_VALIDATION, getString(R.string.err_cno_comment)));
        videoTitleView.getTitleTV().setTextColor(getResources().getColor(android.R.color.white));
        videoTitleView.getTitleTV().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.eighteen_sp));
        commentsRV.setLayoutManager(new LinearLayoutManager(this));
        imageLoading = new ImageLoading(this);
        getCurrentTrackData();
        setUpUi(selectedSongPos);
        setFadeInBgAnimation();
        String fileName = URLUtil.guessFileName(allVideoModels.get(selectedSongPos).getVideo_url(), null, null);
        audioNameTV.setText(fileName.substring(0, fileName.lastIndexOf(".")));

        //Utils.configureToolbarWithOutBackButton(this, toolbarView, allVideoModels.get(selectedSongPos).getTitle());
        //toolbarView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

    private void getCurrentTrackData() {
        CallWebService.getInstance(this, true, ApiCodes.SINGLE_VIDEO_DATA).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_VIDEO_DETAIL, createJsonForGettingVideoInfo(), this);
    }

    @Override
    public void onBackPressed() {

        if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.sendCommentIV, R.id.firstUserLL, R.id.commentsTV, R.id.viewsCountTV, R.id.userOneLikesCountTV, R.id.musicBottomRL})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.firstUserLL:
                break;
            case R.id.commentsTV:
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
            case R.id.viewsCountTV:
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
            case R.id.musicBottomRL:
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
            case R.id.sendCommentIV:
                sendComment();
                break;
            case R.id.userOneLikesCountTV:
                checkAndSendLike();
                break;

        }
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
        }
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        CallWebService.getInstance(this, false, ApiCodes.DELETE_COMMENT).hitJsonObjectRequestAPI(CallWebService.POST, API.DELETE_COMMENT, createJsonForDeleteComment(position), this);
        decreaseCommentCount();
    }

    @Override
    public void onNextClick(int pos) {
        setUpUi(pos);
    }

    @Override
    public void onPrevClick(int pos) {
        setUpUi(pos);
    }

    private void setUpUi(int pos) {
        selectedSongPos = pos;
        uploadedTimeTV.setText(Utils.formatDateAndTime(allVideoModels.get(selectedSongPos).getEdate(), Utils.CURRENT_DATE_FORMAT));
        getCurrentTrackData();
        // updateUI();
        //  setUpMusicTitleBar(allVideoModels.get(pos));
    }

    private void setCommentPBVisibility(int visibility) {
        commentPB.setVisibility(visibility);
    }

    private void updateUI() {

        setUpTitleBarPopupWindow();
        imageLoading.LoadImage(videoDetailModel.getProfileimage(), firstUserIV, null);
        imageLoading.LoadImage(videoDetailModel.getThumbnail(), musicThumbIV, null);
        commentsAdapter = new CommentsAdapter(this, videoDetailModel.getCommentArray(), this);
        commentsRV.setAdapter(commentsAdapter);
        firstUserNameTV.setText(videoDetailModel.getFirst_name());
        viewsCountTV.setText(ReusedCodes.getViews(this, videoDetailModel.getViews()));
        setLikeCountInUI();
        setLikeIV();
        updateCommentsCount();

    }

    private void setUpTitleBarPopupWindow() {
        videoTitleView.setUpForSingleVideo(videoDetailModel.getTitle(), 0, videoDetailModel.getCustomers_videos_id(), this);
        videoTitleView.setUpFavIVButton(videoDetailModel.getFavourite_status());
        String currentCustomerID = MySharedPereference.getInstance().getString(this, Constants.CUSTOMER_ID);
        // if (!currentCustomerID.equals(videoDetailModel.getCustomer_id()))
        videoTitleView.showHideMoreIvButton(false);
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

    private void setFadeInBgAnimation() {

        final Handler handler = new Handler();
        final int[] i = {0};
        final int[] j = {1};
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Resources res = getApplicationContext().getResources();
                        TransitionDrawable out = new TransitionDrawable(new Drawable[]{res.getDrawable(DrawableImage[i[0]]), res.getDrawable(DrawableImage[j[0]])});
                        out.setCrossFadeEnabled(true);
                        slidingLayout.setBackgroundDrawable(out);
                        out.startTransition(4000);
                        i[0]++;
                        j[0]++;
                        if (j[0] == DrawableImage.length) {
                            j[0] = 0;
                        }
                        if (i[0] == DrawableImage.length) {
                            i[0] = 0;
                        }
                        handler.postDelayed(this, 8000);
                    }
                });
            }
        }, 0);
    }

    private void setLikeCountInUI() {
        userOneLikesCountTV.setText(ReusedCodes.getLikes(this, String.valueOf(videoDetailModel.getLikes())));
    }


    private void updateCommentsCount() {
        BroadcastSenderClass.getInstance().sendCommentCountBroadcast(this, videoDetailModel.getCustomers_videos_id(), videoDetailModel.getVideo_comment_count());
        commentsTV.setText(ReusedCodes.getComment(this, videoDetailModel.getVideo_comment_count()));
    }

    private JSONObject createJsonForGettingVideoInfo() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, allVideoModels.get(selectedSongPos).getCustomers_videos_id());
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
    public void onTitleBarButtonClicked(View view, final int position) {
        switch (view.getId()) {
            case R.id.deleteVideoTV:
                CommonFunctions.getInstance().deleteVideo(this, allVideoModels.get(position).getCustomers_videos_id(), new DeleteVideoInterface() {
                    @Override
                    public void onDeleteVideo() {
                        BroadcastSenderClass.getInstance().reloadAllVideoData(PlayMusicActivity.this);
                        removeItemFromArrayList(position);
                    }
                });
                break;
            case R.id.favIV:
                CallWebService.getInstance(this, false, ApiCodes.ACTION_FAVORITE).hitJsonObjectRequestAPI(CallWebService.POST, API.ACTION_FAVORITE, CommonFunctions.getInstance().createJsonForActionFav(this, allVideoModels.get(position).getCustomers_videos_id(), updateUiForFavClick((ImageView) view, position)), null);
                break;
        }
    }

    private void removeItemFromArrayList(int pos) {
        allVideoModels.remove(pos);
        customMusicPlayer.initialize(allVideoModels);
        if (allVideoModels.size() > selectedSongPos) {
            customMusicPlayer.playAudio(selectedSongPos);
        } else
            customMusicPlayer.playAudio(0);
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
        return favStatus;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomMusicPlayer.stopService();
    }
}
