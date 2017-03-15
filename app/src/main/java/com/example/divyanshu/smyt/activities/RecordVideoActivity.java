package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GocoderConfigAndUi.CameraActivityBase;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.AutoFocusListener;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.MultiStateButton;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.TimerView;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.InAppLocalApis;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.wowza.gocoder.sdk.api.devices.WZCamera;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_TO_PREMIUM;

/**
 * Created by divyanshu.jain on 9/2/2016.
 */
public class RecordVideoActivity extends CameraActivityBase implements CameraActivityBase.GoCoderCallBack, InAppLocalApis.InAppAvailabilityCalBack {


    @InjectView(R.id.ic_switch_camera)
    MultiStateButton icSwitchCamera;
    @InjectView(R.id.ic_torch)
    MultiStateButton icTorch;
    @InjectView(R.id.ic_broadcast)
    MultiStateButton icBroadcast;
    @InjectView(R.id.txtTimer)
    TimerView txtTimer;

    protected GestureDetectorCompat mAutoFocusDetector = null;
    public static String screenshotBaseImage;
    @InjectView(R.id.surfaceViewFL)
    FrameLayout surfaceViewFL;
    private boolean isLive = false;
    private String postVideoData = "";
    private String customerVideoID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.record_video_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {
        postVideoData = getIntent().getStringExtra(Constants.POST_VIDEO_DATA);
        isLive = getIntent().getBooleanExtra(Constants.LIVE_STATUS, false);
        txtTimer.setTenMinutesCallback(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (MySharedPereference.getInstance().getString(this, Constants.CATEGORY_ID).equals(getString(R.string.premium_category))) {
            checkAndPayForAddVideoToPremium();
        } else {
            configureGocoder();
        }
    }

    private void configureGocoder() {

        if (mAutoFocusDetector == null) {
            mAutoFocusDetector = new GestureDetectorCompat(this, new AutoFocusListener(this, mWZCameraView));
        }
        configureGocoderAPI();
        setGoCoderCallBack(this);
    }

    public void onSwitchCamera(View v) {
        if (mWZCameraView == null) return;

        icTorch.setState(false);
        icTorch.setEnabled(false);

        WZCamera newCamera = mWZCameraView.switchCamera();
        if (newCamera != null) {
            if (newCamera.hasCapability(WZCamera.FOCUS_MODE_CONTINUOUS))
                newCamera.setFocusMode(WZCamera.FOCUS_MODE_CONTINUOUS);

            boolean hasTorch = newCamera.hasCapability(WZCamera.TORCH);
            if (hasTorch) {
                icTorch.setState(newCamera.isTorchOn());
                icTorch.setEnabled(true);
            }
        }
    }

    /**
     * Click handler for the torch/flashlight button
     */
    public void onToggleTorch(View v) {
        if (mWZCameraView == null) return;

        WZCamera activeCamera = mWZCameraView.getCamera();
        activeCamera.setTorchOn(icTorch.toggleState());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mAutoFocusDetector != null)
            mAutoFocusDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected boolean syncUIControlState() {
        boolean disableControls = super.syncUIControlState();

        if (disableControls) {
            icSwitchCamera.setEnabled(false);
            icTorch.setEnabled(false);
        } else {
            boolean isDisplayingVideo = (getBroadcastConfig().isVideoEnabled() && mWZCameraView.getCameras().length > 0);
            boolean isStreaming = getBroadcast().getStatus().isRunning();

            if (isDisplayingVideo) {
                WZCamera activeCamera = mWZCameraView.getCamera();

                boolean hasTorch = (activeCamera != null && activeCamera.hasCapability(WZCamera.TORCH));
                icTorch.setEnabled(hasTorch);
                if (hasTorch) {
                    icTorch.setState(activeCamera.isTorchOn());
                }

                icSwitchCamera.setEnabled(mWZCameraView.getCameras().length > 0);
                //mBtnSwitchCamera.setEnabled(mWZCameraView.isSwitchCameraAvailable());
            } else {
                icSwitchCamera.setEnabled(false);
                icTorch.setEnabled(false);
            }

            if (isStreaming && !txtTimer.isRunning()) {
                txtTimer.startTimer();
            } else if (getBroadcast().getStatus().isIdle() && txtTimer.isRunning()) {
                txtTimer.stopTimer();
            } else if (!isStreaming) {
                txtTimer.setVisibility(View.GONE);
            }
        }

        return disableControls;
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);

        switch (apiType) {
            case ApiCodes.POST_LIVE_VIDEO_ONE:
                customerVideoID = response.getString(Constants.CUSTOMERS_VIDEO_ID);
                break;
            case ApiCodes.POST_LIVE_VIDEO_ZERO:
                endBroadcast();
                break;
        }
    }

    public void onToggleBroadcast(View v) {
        if (getBroadcast() == null) return;

        if (getBroadcast().getStatus().isIdle()) {
            createVideoUrl();
            WZStreamingError configError = startBroadcast();
            if (configError != null) {
                if (mStatusView != null)
                    mStatusView.setErrorMessage(configError.getErrorDescription());
            }
        } else {
            if (isLive) {
                CallWebService.getInstance(this, false, ApiCodes.POST_LIVE_VIDEO_ZERO).hitJsonObjectRequestAPI(CallWebService.POST, API.POST_SINGLE_LIVE_VIDEOS, createJsonForPostLiveVideo("0"), this);
            }else
            endBroadcast(false);
        }
    }

    private void checkAndPayForAddVideoToPremium() {
        setUpAvailabilityPurchase(OTHER_CATEGORY_TO_PREMIUM);
        InAppLocalApis.getInstance().checkAddVideoInPremiumCatAvailability(this);
    }

    private void setUpAvailabilityPurchase(int purchaseType) {
        InAppLocalApis.getInstance().setCallback(this);
        InAppLocalApis.getInstance().setPurchaseType(purchaseType);
    }

    @Override
    public void available(int purchaseType) {
        configureGocoder();

    }

    @Override
    public void notAvailable(int purchaseType) {
        Intent intent = new Intent(this, InAppActivity.class);
        intent.putExtra(Constants.IN_APP_TYPE, purchaseType);
        startActivityForResult(intent, InAppActivity.PURCHASE_REQUEST);
    }

    @Override
    public void negativeButtonPressed() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == InAppActivity.PURCHASE_REQUEST) {

                if (data.getBooleanExtra(Constants.IS_PRCHASED, false)) {
                    String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
                    String productID = data.getStringExtra(Constants.PRODUCT_ID);
                    InAppLocalApis.getInstance().purchaseCategory(this, transactionID, productID);
                    configureGocoder();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onVideoStart() {
        if (isLive) {
            CallWebService.getInstance(this, false, ApiCodes.POST_LIVE_VIDEO_ONE).hitJsonObjectRequestAPI(CallWebService.POST, API.POST_SINGLE_LIVE_VIDEOS, createJsonForPostLiveVideo("1"), this);
        }
    }

    @Override
    public void onVideoStop() {
        Intent intent = new Intent(this, PostNewVideoActivity.class);
        intent.putExtra(Constants.VIDEO_NAME, videoName);
        intent.putExtra(Constants.POST_VIDEO_DATA, postVideoData);
        intent.putExtra(Constants.LIVE_STATUS,isLive);
        startActivity(intent);
        finish();
    }

    private JSONObject createJsonForPostLiveVideo(String liveStatus) {
        try {
            JSONObject jsonObject = new JSONObject(postVideoData);
            jsonObject.put(Constants.VIDEO_URL, streamVideoUrl);
            jsonObject.put(Constants.VIDEO_NAME, videoName);
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
            jsonObject.put(Constants.LIVE_STATUS, liveStatus);
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!getBroadcast().getStatus().isIdle())
            endBroadcast(false);
    }
}
