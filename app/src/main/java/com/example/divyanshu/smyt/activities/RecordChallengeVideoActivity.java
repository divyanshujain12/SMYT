package com.example.divyanshu.smyt.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
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
import com.example.divyanshu.smyt.CustomViews.CustomToasts;
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
import com.example.divyanshu.smyt.Utils.VideoSetupUtils;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;
import com.neopixl.pixlui.components.checkbox.CheckBox;
import com.neopixl.pixlui.components.textview.TextView;
import com.wowza.gocoder.sdk.api.devices.WZCamera;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerStandardTwo;

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
    @InjectView(R.id.autoStartCB)
    CheckBox autoStartCB;
    @InjectView(R.id.otherUserVideoPlayer)
    JCVideoPlayerStandard otherUserVideoPlayer;
    private CountDownTimerClass countDownTimerClass;
    private String challengeID;
    private String customerVideoID = "";
    boolean serviceStarted = false;
    boolean autoStartRecording = false;
    int delay = 5000; //milliseconds
    protected PowerManager.WakeLock mWakeLock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.challenge_record_video_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {

        challengeID = getIntent().getStringExtra(Constants.CHALLENGE_ID);
        customerVideoID = getIntent().getStringExtra(Constants.CUSTOMERS_VIDEO_ID);
        long roundDateAndTime = Long.parseLong(getIntent().getStringExtra(Constants.ROUND_TIME));
        if (Utils.isTimeGone(roundDateAndTime)) {
            CustomToasts.getInstance(this).showErrorToast(getString(R.string.err_round_time_gone));
            Intent intent = new Intent(this, OngoingChallengeDescriptionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        long remainingTime = Utils.getTimeDifferenceFromCurrent(roundDateAndTime);
        CountDownTimerClass countDownTimerClass = new CountDownTimerClass(remainingTime, 1000);
        countDownTimerClass.start();
        txtTimer.setTenMinutesCallback(this);
        autoStartCB.setOnCheckedChangeListener(this);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

    }


    @Override
    protected void onResume() {
        super.onResume();
        setGoCoderCallBack(this);
        mWZCameraView.setKeepScreenOn(true);
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
            videoStopped();
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
        h.postDelayed(runnable, delay);
        if (autoStartRecording)
            Broadcast();
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            CallWebService.getInstance(getBaseContext(), false, ApiCodes.OTHER_USER_VIDEO_URL).hitJsonObjectRequestAPI(CallWebService.POST, API.OTHER_CUSTOMER_VIDEO_URL, createJsonForGetUserAvailability(customerVideoID), RecordChallengeVideoActivity.this);
            h.postDelayed(this, delay);
        }
    };

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case ApiCodes.OTHER_USER_VIDEO_URL:
                if (!response.get(Constants.VIDEO_URL).equals("")) {
                    onAvailable(response.getString(Constants.VIDEO_URL));
                    h.removeCallbacks(runnable);
                }
                break;
            case ApiCodes.END_CHALLENGE:
                mWZBroadcast.endBroadcast();
                break;
        }
    }

    @Override
    public void onAvailable(String videoUrl) {
        otherUserWaitingTV.setVisibility(View.GONE);
        VideoSetupUtils.getInstance(this).setUpFirstVideoPlayer(otherUserVideoPlayer, videoUrl, "");
        otherUserVideoPlayer.startPlayLogic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        h.removeCallbacks(runnable);
        if (OngoingChallengeDescriptionActivity.isRoundPlayed)
            videoStopped();
        mWakeLock.release();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

    private JSONObject createJsonForGetUserAvailability(String customerVideoID) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getBaseContext());
        try {
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayerStandard.backPress())
            return;
        else if (JCVideoPlayerStandardTwo.backPress())
            return;
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayerStandard.releaseAllVideos();
        JCVideoPlayerStandardTwo.releaseAllVideos();
        if (OngoingChallengeDescriptionActivity.isRoundPlayed)
            videoStopped();
        h.removeCallbacks(runnable);
        //MediaPlayerHelper.getInstance().releaseAllVideos();
    }

    @Override
    public void onVideoStart() {
        CallWebService.getInstance(this, false, ApiCodes.START_CHALLENGE).hitJsonObjectRequestAPI(CallWebService.POST, API.CHALLENGE_START, createJsonForStartEndChallengeVideo("1"), this);
        OngoingChallengeDescriptionActivity.isRoundPlayed = true;
    }

    @Override
    public void onVideoStop() {
        finish();
        // videoStopped();
    }

    private void videoStopped() {
        CallWebService.getInstance(this, false, ApiCodes.END_CHALLENGE).hitJsonObjectRequestAPI(CallWebService.POST, API.CHALLENGE_START, createJsonForStartEndChallengeVideo("0"), this);
        BroadcastSenderClass.getInstance().reloadAllVideoData(this);
    }

    Handler h = new Handler();
}