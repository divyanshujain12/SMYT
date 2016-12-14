package com.example.divyanshu.smyt.GocoderConfigAndUi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.UploadNewVideoActivity;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.StatusView;
import com.example.divyanshu.smyt.GocoderConfigAndUi.UI.MultiStateButton;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.devices.WZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WZCamera;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZError;
import com.wowza.gocoder.sdk.api.geometry.WZSize;
import com.wowza.gocoder.sdk.api.graphics.WZColor;
import com.wowza.gocoder.sdk.api.status.WZStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.divyanshu.smyt.Constants.Constants.CUSTOMER_ID;
import static com.example.divyanshu.smyt.Constants.Constants.WOWZA_APPLICATION_NAME;
import static com.example.divyanshu.smyt.Constants.Constants.WOWZA_MYSTREAM_PREFIX;
import static com.example.divyanshu.smyt.Constants.Constants.WOWZA_PASSWORD;
import static com.example.divyanshu.smyt.Constants.Constants.WOWZA_STREAM_URL;
import static com.example.divyanshu.smyt.Constants.Constants.WOWZA_USERNAME;

abstract public class CameraActivityBase extends GoCoderSDKActivityBase
        implements WZCameraView.PreviewStatusListener {

    private final static String TAG = CameraActivityBase.class.getSimpleName();

    // UI controls
    protected MultiStateButton mBtnBroadcast = null;
    protected StatusView mStatusView = null;

    // The GoCoder SDK camera preview display view
    protected WZCameraView mWZCameraView = null;
    protected WZAudioDevice mWZAudioDevice = null;

    private boolean mDevicesInitialized = false;
    private boolean mUIInitialized = false;
    private Pattern uri;
    protected String videoName = "";
    protected String streamVideoUrl = "";
    private String userID = "";
    private WZStatus goCoderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Android Activity lifecycle methods
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (!mUIInitialized)
            initUIControls();
        if (!mDevicesInitialized)
            initGoCoderDevices();

        if (sGoCoderSDK != null && mPermissionsGranted) {
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

    @Override
    protected void onPause() {
        super.onPause();

        if (mWZCameraView != null) {
            mWZCameraView.onPause();
        }
    }

    /**
     * WZStatusCallback interface methods
     */
    @Override
    public void onWZStatus(final WZStatus goCoderStatus) {
        this.goCoderStatus = goCoderStatus;
       // handler.sendEmptyMessage(0);
        //Toast.makeText(this,goCoderStatus.toString(),Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (goCoderStatus.isRunning()) {
                    // Keep the screen on while we are broadcasting
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else if (goCoderStatus.isIdle()) {
                    // Clear the "keep screen on" flag
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }

                if (mStatusView != null) mStatusView.setStatus(goCoderStatus);
                syncUIControlState();
            }
        });
        System.out.println("in");
    }

    @Override
    public void onWZError(final WZStatus goCoderStatus) {
        this.goCoderStatus = goCoderStatus;
        //handler.sendEmptyMessage(0);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mStatusView != null) mStatusView.setStatus(goCoderStatus);
                syncUIControlState();
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (goCoderStatus.isRunning()) {
                // Keep the screen on while we are broadcasting
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else if (goCoderStatus.isIdle()) {
                // Clear the "keep screen on" flag
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                Intent intent = new Intent(CameraActivityBase.this, UploadNewVideoActivity.class);
                intent.putExtra(Constants.VIDEO_NAME, videoName);
                startActivity(intent);
                finish();
            }

            if (mStatusView != null) mStatusView.setStatus(goCoderStatus);
            syncUIControlState();
            return false;
        }
    });

    /**
     * Click handler for the broadcast button
     */


    protected void createVideoUrl() {
        uri = Pattern.compile("rtsp://(.+):(\\d+)/(.+)");
        userID = MySharedPereference.getInstance().getString(this, CUSTOMER_ID);
        videoName = WOWZA_MYSTREAM_PREFIX + userID + "_" + Utils.getCurrentTimeInMillisecond();
        streamVideoUrl = WOWZA_STREAM_URL + videoName;
        Matcher m = uri.matcher(streamVideoUrl);
        m.find();
        String ip = m.group(1);
        String port = m.group(2);
        // String path = m.group(3);

        mWZBroadcastConfig.setHostAddress(ip);
        mWZBroadcastConfig.setPortNumber(Integer.parseInt(port));
        mWZBroadcastConfig.setUsername(WOWZA_USERNAME);
        mWZBroadcastConfig.setPassword(WOWZA_PASSWORD);
        mWZBroadcastConfig.setApplicationName(WOWZA_APPLICATION_NAME);
        mWZBroadcastConfig.setStreamName(/*"mp4:" +*/ videoName);
        mWZBroadcastConfig.setVideoSourceConfig(WZMediaConfig.FRAME_SIZE_176x144);

        mWZBroadcastConfig.setVideoFramerate(15);
    }

    /**
     * Click handler for the settings button
     */

    protected void initGoCoderDevices() {
        if (sGoCoderSDK != null && mPermissionsGranted) {

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
    public void onWZCameraPreviewStarted(WZCamera wzCamera, WZSize wzSize, int i) {
    }

    @Override
    public void onWZCameraPreviewStopped(int cameraId) {
    }

    @Override
    public void onWZCameraPreviewError(WZCamera wzCamera, WZError wzError) {
    }

    protected void initUIControls() {
        // Initialize the UI controls
        mBtnBroadcast = (MultiStateButton) findViewById(R.id.ic_broadcast);
        mStatusView = (StatusView) findViewById(R.id.statusView);

        // The GoCoder SDK camera view
        mWZCameraView = (WZCameraView) findViewById(R.id.cameraPreview);
        mWZCameraView.setPreviewReadyListener(this);

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
}