package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.CustomViews.SingleVideoPlayerCustomView;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.DeleteVideoInterface;
import com.example.divyanshu.smyt.Models.ThumbnailGenerateModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.divyanshu.smyt.Constants.ApiCodes.POST_USER_VIDEO;
import static com.example.divyanshu.smyt.Constants.ApiCodes.POST_VIDEO_PREVIOUS;

public class PostNewVideoActivity extends BaseActivity {

    @InjectView(R.id.singleVideoPlayer)
    SingleVideoPlayerCustomView singleVideoPlayer;
    @InjectView(R.id.deleteVideoTV)
    TextView deleteVideoTV;
    @InjectView(R.id.postVideoTV)
    TextView postVideoTV;
    @InjectView(R.id.activity_post_new_video)
    LinearLayout activityPostNewVideo;
    @InjectView(R.id.titleTV)
    TextView titleTV;
    @InjectView(R.id.userIV)
    RoundedImageView userIV;
    @InjectView(R.id.sharedWithNameTV)
    TextView sharedWithNameTV;

    private String videoName = "";
    private String postVideoData = "";
    private ThumbnailGenerateModel thumbnailGenerateModel;

    private ImageLoading imageLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_video);
        ButterKnife.inject(this);

        initViews();
    }

    private void initViews() {
        postVideoData = getIntent().getStringExtra(Constants.POST_VIDEO_DATA);
        videoName = getIntent().getStringExtra(Constants.VIDEO_NAME);
        imageLoading = new ImageLoading(this);
        setUpUi();
        CallWebService.getInstance(this, true, ApiCodes.POST_VIDEO_PREVIOUS).hitJsonObjectRequestAPI(CallWebService.POST, API.THUMBNAIL_GENERATE, createJsonForGetPreviousVideoDetail(), this);
    }

    @OnClick({R.id.deleteVideoTV, R.id.postVideoTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteVideoTV:
                if (thumbnailGenerateModel.getCustomer_video_id() != 0)
                    deleteVideo();
                else
                    finish();
                break;
            case R.id.postVideoTV:
                if (!getIntent().getBooleanExtra(Constants.LIVE_STATUS, false))
                    CallWebService.getInstance(this, true, POST_USER_VIDEO).hitJsonObjectRequestAPI(CallWebService.POST, API.POST_VIDEO, createJsonForPostVideo(), this);
                else
                    onPostSuccess();

                break;
        }
    }

    private void deleteVideo() {
        CommonFunctions.getInstance().deleteVideo(this, String.valueOf(thumbnailGenerateModel.getCustomer_video_id()), new DeleteVideoInterface() {
            @Override
            public void onDeleteVideo() {
                finish();
            }
        });
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);

        switch (apiType) {
            case POST_USER_VIDEO:
                CommonFunctions.getInstance().showSuccessSnackBar(this, response.getString(Constants.MESSAGE));
                onPostSuccess();
                break;
            case POST_VIDEO_PREVIOUS:
                thumbnailGenerateModel = UniversalParser.getInstance().parseJsonObject(response.getJSONObject(Constants.DATA), ThumbnailGenerateModel.class);
                setUpVideoPlayer(thumbnailGenerateModel);
                break;
        }

    }

    private void onPostSuccess() {
        BroadcastSenderClass.getInstance().reloadAllVideoData(this);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setUpVideoPlayer(ThumbnailGenerateModel thumbnailGenerateModel) {
        singleVideoPlayer.setUp(thumbnailGenerateModel.getVideo_url(), thumbnailGenerateModel.getThumbnail(), "");

    }

    private void setUpUi() {
        try {
            JSONObject jsonObject = getJsonObject();
            String shareType = jsonObject.getString(Constants.SHARE_STATUS);
            titleTV.setText(jsonObject.getString(Constants.TITLE));
            switch (shareType) {
                case "Public":
                    sharedWithNameTV.setText(shareType);
                    userIV.setVisibility(View.GONE);
                    break;
                case "Friend":
                    sharedWithNameTV.setText(jsonObject.getString(Constants.NAME));
                    imageLoading.LoadImage(jsonObject.getString(Constants.PROFILE_IMAGE), userIV, null);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createJsonForGetPreviousVideoDetail() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.VIDEO_NAME, videoName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForPostVideo() {
        try {
            JSONObject jsonObject = getJsonObject();
            jsonObject.put(Constants.VIDEO_URL, thumbnailGenerateModel.getVideo_url());
            jsonObject.put(Constants.THUMBNAIL, thumbnailGenerateModel.getThumbnail());
            jsonObject.put(Constants.VIDEO_NAME, videoName);
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());

            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    private JSONObject getJsonObject() throws JSONException {
        return new JSONObject(postVideoData);
    }
}
