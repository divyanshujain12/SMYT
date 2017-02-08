package com.example.divyanshu.smyt.GocoderConfigAndUi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;

import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.configuration.WowzaConfig;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.logging.WZLog;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;

import java.util.Arrays;

public abstract class GoCoderSDKActivityBase extends BaseActivity
        implements WZStatusCallback {

    private final static String TAG = GoCoderSDKActivityBase.class.getSimpleName();

    private static final String SDK_SAMPLE_APP_LICENSE_KEY = "GOSK-9342-0100-3204-B878-FE0F";
    private static final int PERMISSIONS_REQUEST_CODE = 0x1;

    protected String[] mRequiredPermissions = {};

    private static Object sBroadcastLock = new Object();
    private static boolean sBroadcastEnded = true;

    // indicates whether this is a full screen activity or note
    protected static boolean sFullScreenActivity = true;

    // GoCoder SDK top level interface
    protected static WowzaGoCoder sGoCoderSDK = null;
    private WZStatus goCoderStatus;

    /**
     * Build an array of WZMediaConfigs from the frame sizes supported by the active camera
     *
     * @param goCoderCameraView the camera view
     * @return an array of WZMediaConfigs from the frame sizes supported by the active camera
     */
    protected static WZMediaConfig[] getVideoConfigs(WZCameraView goCoderCameraView) {
        WZMediaConfig configs[] = WowzaConfig.PRESET_CONFIGS;

        if (goCoderCameraView != null && goCoderCameraView.getCamera() != null) {
            WZMediaConfig cameraConfigs[] = goCoderCameraView.getCamera().getSupportedConfigs();
            Arrays.sort(cameraConfigs);
            configs = cameraConfigs;
        }

        return configs;
    }

    protected boolean mPermissionsGranted = false;

    protected WZBroadcast mWZBroadcast = null;

    public WZBroadcast getBroadcast() {
        return mWZBroadcast;
    }

    protected WZBroadcastConfig mWZBroadcastConfig = null;

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

        if (sGoCoderSDK == null) {
            // Enable detailed logging from the GoCoder SDK
            WZLog.LOGGING_ENABLED = true;

            // Initialize the GoCoder SDK
            sGoCoderSDK = WowzaGoCoder.init(this, SDK_SAMPLE_APP_LICENSE_KEY);

            if (sGoCoderSDK == null) {
                WZLog.error(TAG, WowzaGoCoder.getLastError());
            }
        }

        if (sGoCoderSDK != null) {
            // Create a GoCoder broadcaster and an associated broadcast configuration
            mWZBroadcast = new WZBroadcast();
            mWZBroadcastConfig = new WZBroadcastConfig(sGoCoderSDK.getConfig());
            mWZBroadcastConfig.setLogLevel(WZLog.LOG_LEVEL_DEBUG);
            mWZBroadcastConfig.set(WZMediaConfig.FRAME_SIZE_320x240);
            mWZBroadcastConfig.setVideoFramerate(15);
            mWZBroadcastConfig.setVideoKeyFrameInterval(15);
        }

    }

    /**
     * Android Activity lifecycle methods
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (mWZBroadcast != null) {
            mPermissionsGranted = true;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mPermissionsGranted = (mRequiredPermissions.length > 0 ? WowzaGoCoder.hasPermissions(this, mRequiredPermissions) : true);
                if (!mPermissionsGranted)
                    ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE);
            }
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mPermissionsGranted = true;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mPermissionsGranted = false;
                    }
                }
            }
        }
    }

    /**
     * Enable Android's sticky immersive full-screen mode
     * See http://developer.android.com/training/system-ui/immersive.html#sticky
     */
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

    /**
     * WZStatusCallback interface methods
     */


    @Override
    public void onWZStatus(final WZStatus goCoderStatus) {
        this.goCoderStatus = goCoderStatus;
        handler.sendEmptyMessage(0);
        //Toast.makeText(this,goCoderStatus.toString(),Toast.LENGTH_SHORT).show();
      /*  new Handler(Looper.getMainLooper()).post(new Runnable() {
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
        });*/
        System.out.println("in");
    }

    @Override
    public void onWZError(final WZStatus goCoderStatus) {
        this.goCoderStatus = goCoderStatus;
        handler.sendEmptyMessage(0);
     /*   new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mStatusView != null) mStatusView.setStatus(goCoderStatus);
                syncUIControlState();
            }
        });*/
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
            }

            return false;
        }
    });
   /* @Override
    public void onWZStatus(final WZStatus goCoderStatus) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (goCoderStatus.isReady()) {
                    // Keep the screen on while the broadcast is active
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else if (goCoderStatus.isIdle())
                    // Clear the "keep screen on" flag
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                WZLog.debug(TAG, goCoderStatus.toString());
            }
        });
    }

    @Override
    public void onWZError(final WZStatus goCoderStatus) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                WZLog.error(TAG, goCoderStatus.getLastError());
            }
        });
    }*/

    protected synchronized WZStreamingError startBroadcast() {
        WZStreamingError configValidationError = null;

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

    protected synchronized void endBroadcast() {
        new StopBroadcast().execute();
    }

    private class StopBroadcast extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            endBroadcast(false);
            return null;
        }
    }
}
