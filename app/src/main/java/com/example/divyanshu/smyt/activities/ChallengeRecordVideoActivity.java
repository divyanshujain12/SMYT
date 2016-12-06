package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.UploadNewVideoActivity;
import com.example.divyanshu.smyt.GocoderConfigAndUi.CameraActivityBase;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.AutoFocusListener;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.MultiStateButton;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.TimerView;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;
import com.wowza.gocoder.sdk.api.devices.WZCamera;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;

import java.text.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 12/6/2016.
 */

public class ChallengeRecordVideoActivity extends CameraActivityBase {
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
    private CountDownTimerClass countDownTimerClass;
    private String challengeID;

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
        long roundDateAndTime = Long.parseLong(getIntent().getStringExtra(Constants.ROUND_TIME));
        long remainingTime = Utils.getTimeDifferenceFromCurrent(roundDateAndTime);
        CountDownTimerClass countDownTimerClass = new CountDownTimerClass(remainingTime, 1000);
        countDownTimerClass.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
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
            mWZBroadcast.endBroadcast();
            Intent intent = new Intent(this, UploadNewVideoActivity.class);
            intent.putExtra(Constants.VIDEO_NAME, videoName);
            startActivity(intent);
            finish();
        }
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
            if (mAutoFocusDetector == null)
                mAutoFocusDetector = new GestureDetectorCompat(ChallengeRecordVideoActivity.this, new AutoFocusListener(ChallengeRecordVideoActivity.this, mWZCameraView));
        }
    }
}

