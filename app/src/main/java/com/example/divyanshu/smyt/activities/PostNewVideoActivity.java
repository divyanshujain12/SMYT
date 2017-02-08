package com.example.divyanshu.smyt.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.SingleVideoPlayerCustomView;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.DeleteVideoInterface;
import com.example.divyanshu.smyt.Models.ThumbnailGenerateModel;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
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
import static com.example.divyanshu.smyt.Constants.ApiCodes.SEARCH_USER;

public class PostNewVideoActivity extends BaseActivity {

    @InjectView(R.id.singleVideoPlayer)
    SingleVideoPlayerCustomView singleVideoPlayer;
    @InjectView(R.id.deleteVideoTV)
    TextView deleteVideoTV;
    @InjectView(R.id.postVideoTV)
    TextView postVideoTV;
    @InjectView(R.id.activity_post_new_video)
    LinearLayout activityPostNewVideo;

    private String videoName = "";
    private String postVideoData = "";
    private ThumbnailGenerateModel thumbnailGenerateModel;

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
                CallWebService.getInstance(this, true, POST_USER_VIDEO).hitJsonObjectRequestAPI(CallWebService.POST, API.POST_VIDEO, createJsonForPostVideo(), this);
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
                BroadcastSenderClass.getInstance().reloadAllVideoData(this);
                finish();
                break;
            case POST_VIDEO_PREVIOUS:
                thumbnailGenerateModel = UniversalParser.getInstance().parseJsonObject(response.getJSONObject(Constants.DATA), ThumbnailGenerateModel.class);
                setUpVideoPlayer(thumbnailGenerateModel);
                break;
        }

    }

    private void setUpVideoPlayer(ThumbnailGenerateModel thumbnailGenerateModel) {
        singleVideoPlayer.setUp(thumbnailGenerateModel.getVideo_url(), thumbnailGenerateModel.getThumbnail(), "");
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
            JSONObject jsonObject = new JSONObject(postVideoData);
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
}
