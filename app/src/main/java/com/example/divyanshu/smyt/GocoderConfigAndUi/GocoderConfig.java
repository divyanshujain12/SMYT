package com.example.divyanshu.smyt.GocoderConfigAndUi;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.UploadNewVideoActivity;
import com.example.divyanshu.smyt.Fragments.RuntimePermissionHeadlessFragment;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.MultiStateButton;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.StatusView;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.PermissionUtil;
import com.example.divyanshu.smyt.Utils.Utils;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.devices.WZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WZCamera;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.graphics.WZColor;
import com.wowza.gocoder.sdk.api.logging.WZLog;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;

import static com.example.divyanshu.smyt.Constants.Constants.CUSTOMER_ID;
import static com.example.divyanshu.smyt.Constants.Constants.IP_ADDRESS;
import static com.example.divyanshu.smyt.Constants.Constants.PORT_NUMBER;
import static com.example.divyanshu.smyt.Constants.Constants.WOWZA_APPLICATION_NAME;
import static com.example.divyanshu.smyt.Constants.Constants.WOWZA_MYSTREAM_PREFIX;
import static com.example.divyanshu.smyt.Constants.Constants.WOWZA_PASSWORD;
import static com.example.divyanshu.smyt.Constants.Constants.WOWZA_STREAM_URL;
import static com.example.divyanshu.smyt.Constants.Constants.WOWZA_USERNAME;

/**
 * Created by divyanshu on 11/12/16.
 */

public class GocoderConfig extends BaseActivity implements WZStatusCallback, RuntimePermissionHeadlessFragment.PermissionCallback {
    private final static String TAG = GocoderConfig.class.getSimpleName();
    private static final String SDK_APP_LICENSE_KEY = "GOSK-9342-0100-3204-B878-FE0F";
    protected static WowzaGoCoder sGoCoderSDK = null;
    private static boolean sBroadcastEnded = true;
    // indicates whether this is a full screen activity or note
    protected static boolean sFullScreenActivity = true;
    private static Object sBroadcastLock = new Object();
    protected WZBroadcast mWZBroadcast = null;
    protected MultiStateButton mBtnBroadcast = null;
    protected StatusView mStatusView = null;

    // The GoCoder SDK camera preview display view
    protected WZCameraView mWZCameraView = null;
    protected WZAudioDevice mWZAudioDevice = null;

    public WZBroadcast getBroadcast() {
        return mWZBroadcast;
    }

    protected WZBroadcastConfig mWZBroadcastConfig = null;
    private boolean mUIInitialized = false;
    private boolean mDevicesInitialized = false;
    protected String videoName = "";
    protected String streamVideoUrl = "";
    private String userID = "";
    private RuntimePermissionHeadlessFragment runtimePermissionHeadlessFragment;
    private static final int CAMERA_REQUEST = 101;
    protected String[] mRequiredPermissions = {};

    public WZBroadcastConfig getBroadcastConfig() {
        return mWZBroadcastConfig;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequiredPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };
        runtimePermissionHeadlessFragment = CommonFunctions.getInstance().addRuntimePermissionFragment(this, this);

        if (sGoCoderSDK == null) {
            WZLog.LOGGING_ENABLED = true;
            sGoCoderSDK = WowzaGoCoder.init(this, SDK_APP_LICENSE_KEY);
            if (sGoCoderSDK == null) {
                WZLog.error(TAG, WowzaGoCoder.getLastError());
            }
        }
        if (sGoCoderSDK != null) {
            mWZBroadcast = new WZBroadcast();
            mWZBroadcastConfig = new WZBroadcastConfig();
            configureBroadcast();
            mWZBroadcastConfig.setLogLevel(WZLog.LOG_LEVEL_DEBUG);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtil.isMNC())
            runtimePermissionHeadlessFragment.addAndCheckPermission(mRequiredPermissions, CAMERA_REQUEST);
        else
            configureGocoderCamera();

    }

    private void configureGocoderCamera() {
        if (!mUIInitialized)
            initUIControls();
        if (!mDevicesInitialized)
            initGoCoderDevices();

        if (sGoCoderSDK != null) {
            mWZCameraView.setCameraConfig(getBroadcastConfig());
            mWZCameraView.setScaleMode(WZMediaConfig.FILL_VIEW);
            mWZCameraView.setVideoBackgroundColor(WZColor.DARKGREY);

            if (mWZBroadcastConfig.isVideoEnabled()) {
                if (mWZCameraView.isPreviewPaused())
                    mWZCameraView.onResume();
                else
                    mWZCameraView.startPreview();
            }

            // Briefly display the video frame size from config
            Toast.makeText(this, getBroadcastConfig().getLabel(true, true, false, true), Toast.LENGTH_LONG).show();
        }

        syncUIControlState();
    }

    protected void initGoCoderDevices() {
        if (sGoCoderSDK != null) {

            // Initialize the camera preview
            if (mWZCameraView != null) {
                WZCamera availableCameras[] = mWZCameraView.getCameras();
                // Ensure we can access to at least one camera
                if (availableCameras.length > 0) {
                    // Set the video broadcaster in the broadcast config
                    getBroadcastConfig().setVideoBroadcaster(mWZCameraView);
                } else {
                    mStatusView.setErrorMessage("Could not detect or gain access to any cameras on this device");
                    getBroadcastConfig().setVideoEnabled(false);
                }
            } else {
                getBroadcastConfig().setVideoEnabled(false);
            }

            // Initialize the audio input device interface
            mWZAudioDevice = new WZAudioDevice();

            // Set the audio broadcaster in the broadcast config
            getBroadcastConfig().setAudioBroadcaster(mWZAudioDevice);

            mDevicesInitialized = true;
        }
    }

    @Override
    protected void onPause() {
        // Stop any active live stream
        if (mWZBroadcast != null && mWZBroadcast.getStatus().isRunning()) {
            endBroadcast(true);
        }

        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (sFullScreenActivity && hasFocus) {
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            if (rootView != null)
                rootView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onWZStatus(final WZStatus goCoderStatus) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (goCoderStatus.isRunning()) {
                    // Keep the screen on while we are broadcasting
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else if (goCoderStatus.isIdle()) {
                    // Clear the "keep screen on" flag
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    Intent intent = new Intent(GocoderConfig.this, UploadNewVideoActivity.class);
                    intent.putExtra(Constants.VIDEO_NAME, videoName);
                    startActivity(intent);
                    finish();
                }
                syncUIControlState();
            }
        });
    }

    @Override
    public void onWZError(WZStatus wzStatus) {

    }

    protected synchronized WZStreamingError startBroadcast() {
        WZStreamingError configValidationError = null;
        createVideoUrl();
        if (mWZBroadcast.getStatus().isIdle()) {
            WZLog.info(TAG, "=============== Broadcast Configuration ===============\n"
                    + mWZBroadcastConfig.toString()
                    + "\n=======================================================");

            configValidationError = mWZBroadcastConfig.validateForBroadcast();
            if (configValidationError == null) {
                mWZBroadcast.startBroadcast(mWZBroadcastConfig, this);
            }
        } else {
            WZLog.error(TAG, "startBroadcast() called while another broadcast is active");
        }
        return configValidationError;
    }

    protected synchronized void endBroadcast(boolean appPausing) {
        if (!mWZBroadcast.getStatus().isIdle()) {
            if (appPausing) {
                // Stop any active live stream
                sBroadcastEnded = false;
                mWZBroadcast.endBroadcast(new WZStatusCallback() {
                    @Override
                    public void onWZStatus(WZStatus wzStatus) {
                        synchronized (sBroadcastLock) {
                            sBroadcastEnded = true;
                            sBroadcastLock.notifyAll();
                        }
                    }

                    @Override
                    public void onWZError(WZStatus wzStatus) {
                        WZLog.error(TAG, wzStatus.getLastError());
                        synchronized (sBroadcastLock) {
                            sBroadcastEnded = true;
                            sBroadcastLock.notifyAll();
                        }
                    }
                });

                while (!sBroadcastEnded) {
                    try {
                        sBroadcastLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            } else {
                mWZBroadcast.endBroadcast(this);
            }
        } else {
            WZLog.error(TAG, "endBroadcast() called without an active broadcast");
        }
    }

    protected void createVideoUrl() {
        userID = MySharedPereference.getInstance().getString(this, CUSTOMER_ID);
        videoName = WOWZA_MYSTREAM_PREFIX + userID + "_" + Utils.getCurrentTimeInMillisecond();
        streamVideoUrl = WOWZA_STREAM_URL + videoName;
        mWZBroadcastConfig.setStreamName(videoName);

    }

    private void configureBroadcast() {

        mWZBroadcastConfig.setHostAddress(IP_ADDRESS);
        mWZBroadcastConfig.setPortNumber(Integer.parseInt(PORT_NUMBER));
        mWZBroadcastConfig.setUsername(WOWZA_USERNAME);
        mWZBroadcastConfig.setPassword(WOWZA_PASSWORD);
        mWZBroadcastConfig.setApplicationName(WOWZA_APPLICATION_NAME);
        mWZBroadcastConfig.setVideoFramerate(30);
    }

    protected void initUIControls() {
        // Initialize the UI controls
        mBtnBroadcast = (MultiStateButton) findViewById(R.id.ic_broadcast);
        mStatusView = (StatusView) findViewById(R.id.statusView);

        // The GoCoder SDK camera view
        mWZCameraView = (WZCameraView) findViewById(R.id.cameraPreview);
        //mWZCameraView.setPreviewReadyListener(this);

        mUIInitialized = true;

        if (sGoCoderSDK == null && mStatusView != null)
            mStatusView.setErrorMessage(WowzaGoCoder.getLastError().getErrorDescription());
    }

    protected boolean syncUIControlState() {
        boolean disableControls = (getBroadcast() == null ||
                !(getBroadcast().getStatus().isIdle() ||
                        getBroadcast().getStatus().isRunning()));
        boolean isStreaming = (getBroadcast() != null && getBroadcast().getStatus().isRunning());

        if (disableControls) {
            if (mBtnBroadcast != null) mBtnBroadcast.setEnabled(false);

        } else {
            if (mBtnBroadcast != null) {
                mBtnBroadcast.setState(isStreaming);
                mBtnBroadcast.setEnabled(true);
            }
        }

        return disableControls;
    }

    @Override
    public void onPermissionGranted(int permissionType) {
        configureGocoderCamera();
    }

    @Override
    public void onPermissionDenied(int permissionType) {
        finish();
    }
}
