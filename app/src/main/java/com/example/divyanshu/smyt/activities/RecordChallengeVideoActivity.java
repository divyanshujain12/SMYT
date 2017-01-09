package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.OngoingChallengeDescriptionActivity;
import com.example.divyanshu.smyt.GocoderConfigAndUi.CameraActivityBase;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.AutoFocusListener;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.MultiStateButton;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.TimerView;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.ServicesAndNotifications.OtherUserAvailabilityService;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;
import com.neopixl.pixlui.components.checkbox.CheckBox;
import com.neopixl.pixlui.components.textview.TextView;
import com.player.divyanshu.customvideoplayer.MediaPlayerHelper;
import com.player.divyanshu.customvideoplayer.SingleVideoPlayer;
import com.wowza.gocoder.sdk.api.devices.WZCamera;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;

import org.json.JSONObject;

import java.text.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 12/6/2016.
 */

public class RecordChallengeVideoActivity extends CameraActivityBase
        implements OtherUserAvailabilityService.UserAvailabilityInterface, CameraActivityBase.GoCoderCallBack, CompoundButton.OnCheckedChangeListener {
    @InjectView(R.id.ic_switch_camera)
    MultiStateButton icSwitchCamera;
    @InjectView(R.id.ic_torch)
    MultiStateButton icTorch;
    @InjectView(R.id.ic_broadcast)
    MultiStateButton icBroadcast;
    @InjectView(R.id.txtTimer)
    TimerView txtTimer;

    protected GestureDetectorCompat mAutoFocusDetector = null;
    @InjectView(R.id.recordVideoFL)
    FrameLayout recordVideoFL;
    @InjectView(R.id.countDownTV)
    TextView countDownTV;
    @InjectView(R.id.timerLL)
    LinearLayout timerLL;
    @InjectView(R.id.otherUserWaitingTV)
    TextView otherUserWaitingTV;
    @InjectView(R.id.otherUserVideoPlayer)
    SingleVideoPlayer otherUserVideoPlayer;
    @InjectView(R.id.autoStartCB)
    CheckBox autoStartCB;
    private CountDownTimerClass countDownTimerClass;
    private String challengeID;
    private String customerVideoID = "";
    boolean serviceStarted = false;
    boolean autoStartRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.challenge_record_video_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {

        challengeID = getIntent().getStringExtra(Constants.CHALLENGE_ID);
        customerVideoID = getIntent().getStringExtra(Constants.CUSTOMERS_VIDEO_ID);
        long roundDateAndTime = Long.parseLong(getIntent().getStringExtra(Constants.ROUND_TIME));
        long remainingTime = Utils.getTimeDifferenceFromCurrent(roundDateAndTime);
        CountDownTimerClass countDownTimerClass = new CountDownTimerClass(remainingTime, 1000);
        countDownTimerClass.start();
        otherUserVideoPlayer.setHideControls(true);
        txtTimer.setTenMinutesCallback(this);
        autoStartCB.setOnCheckedChangeListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
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

    public void onToggleBroadcast(View v) {
        Broadcast();
    }

    private void Broadcast() {
        if (getBroadcast() == null) return;

        if (getBroadcast().getStatus().isIdle()) {
            createVideoUrl();
            WZStreamingError configError = startBroadcast();
            if (configError != null) {
                if (mStatusView != null)
                    mStatusView.setErrorMessage(configError.getErrorDescription());
            }
        } else {
            mWZBroadcast.endBroadcast();
            //CallWebService.getInstance(this, false, ApiCodes.END_CHALLENGE).hitJsonObjectRequestAPI(CallWebService.POST, API.CHALLENGE_END, createJsonForStartEndChallengeVideo("0"), this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        autoStartRecording = b;
    }

    private class CountDownTimerClass extends CountDownTimer {

        private CountDownTimerClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            try {
                countDownTV.setText(Utils.getTimeInHMS(millisUntilFinished));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFinish() {
            timerLL.setVisibility(View.GONE);
            recordVideoFL.setVisibility(View.VISIBLE);
            initialize();
        }

    }

    private void initialize() {
        if (mAutoFocusDetector == null)
            mAutoFocusDetector = new GestureDetectorCompat(this, new AutoFocusListener(this, mWZCameraView));

        Intent intent = new Intent(this, OtherUserAvailabilityService.class);
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
        startService(intent);
        OtherUserAvailabilityService.getInstance().setUserAvailabilityInterface(this);
        serviceStarted = true;
        if (autoStartRecording)
            Broadcast();
    }

    @Override
    public void onAvailable(String videoUrl) {
        otherUserWaitingTV.setVisibility(View.GONE);
        otherUserVideoPlayer.setVideoUrl(videoUrl);
        otherUserVideoPlayer.playButtonClicked();
        OtherUserAvailabilityService.getInstance().setUserAvailabilityInterface(null);
        stopService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceStarted) {
            stopService();
        }
    }

    private void stopService() {
        Intent intent = new Intent(this, OtherUserAvailabilityService.class);
        stopService(intent);
    }

    private JSONObject createJsonForStartEndChallengeVideo(String status) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.VIDEO_NAME, videoName);
            jsonObject.put(Constants.VIDEO_URL, streamVideoUrl);
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
            jsonObject.put(Constants.LIVE_STATUS, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!OngoingChallengeDescriptionActivity.isRoundPlayed)
            videoStopped();
        mWZBroadcast.endBroadcast();
        MediaPlayerHelper.getInstance().releaseAllVideos();
    }

    @Override
    public void onVideoStart() {
        CallWebService.getInstance(this, false, ApiCodes.START_CHALLENGE).hitJsonObjectRequestAPI(CallWebService.POST, API.CHALLENGE_START, createJsonForStartEndChallengeVideo("1"), this);
        OngoingChallengeDescriptionActivity.isRoundPlayed = true;
    }

    @Override
    public void onVideoStop() {
        videoStopped();
    }

    private void videoStopped() {
        CallWebService.getInstance(this, false, ApiCodes.START_CHALLENGE).hitJsonObjectRequestAPI(CallWebService.POST, API.CHALLENGE_START, createJsonForStartEndChallengeVideo("0"), this);
        BroadcastSenderClass.getInstance().reloadAllVideoData(this);
        finish();
    }
}

