package com.example.divyanshu.smyt.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;

import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.DialogActivities.UploadNewVideoActivity;
import com.example.divyanshu.smyt.Fragments.RuntimePermissionHeadlessFragment;
import com.example.divyanshu.smyt.GocoderConfigAndUi.CameraActivityBase;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.AutoFocusListener;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.MultiStateButton;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.TimerView;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.wowza.gocoder.sdk.api.devices.WZCamera;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 9/2/2016.
 */
public class RecordVideoActivity extends CameraActivityBase implements RuntimePermissionHeadlessFragment.PermissionCallback {


    @InjectView(R.id.ic_switch_camera)
    MultiStateButton icSwitchCamera;
    @InjectView(R.id.ic_torch)
    MultiStateButton icTorch;
    @InjectView(R.id.ic_broadcast)
    MultiStateButton icBroadcast;

    protected String[] mRequiredPermissions = {};
    @InjectView(R.id.txtTimer)
    TimerView txtTimer;
 //   private RuntimePermissionHeadlessFragment runtimePermissionHeadlessFragment;
//    private static final int CAMERA_REQUEST = 101;
    protected GestureDetectorCompat mAutoFocusDetector = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_video_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {
  /*      mRequiredPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };*/
       // runtimePermissionHeadlessFragment = CommonFunctions.getInstance().addRuntimePermissionFragment(this, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
      //  runtimePermissionHeadlessFragment.addAndCheckPermission(mRequiredPermissions, CAMERA_REQUEST);

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
            endBroadcast();
            Intent intent = new Intent(this, UploadNewVideoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPermissionGranted(int permissionType) {
        initializeGocoder();
    }

    @Override
    public void onPermissionDenied(int permissionType) {
        CustomAlertDialogs.showAlertDialog(this, getString(R.string.permission), getString(R.string.permission_denied_by_user), this);
    }

    private void initializeGocoder() {
        if (sGoCoderSDK != null && mWZCameraView != null) {
            if (mAutoFocusDetector == null)
                mAutoFocusDetector = new GestureDetectorCompat(this, new AutoFocusListener(this, mWZCameraView));

            WZCamera activeCamera = mWZCameraView.getCamera();
            if (activeCamera != null && activeCamera.hasCapability(WZCamera.FOCUS_MODE_CONTINUOUS))
                activeCamera.setFocusMode(WZCamera.FOCUS_MODE_CONTINUOUS);

        }
    }

    @Override
    public void onAlertButtonPressed() {
        super.onAlertButtonPressed();
        finish();
    }
}
