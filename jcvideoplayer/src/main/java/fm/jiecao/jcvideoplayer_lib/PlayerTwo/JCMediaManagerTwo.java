package fm.jiecao.jcvideoplayer_lib.PlayerTwo;

import android.graphics.Point;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

import java.util.Map;


import fm.jiecao.jcvideoplayer_lib.JCResizeTextureView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by divyanshu on 9/25/2016.
 */

public class JCMediaManagerTwo implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnInfoListener {
    public static String TAG = "JieCaoVideoPlayer";

    private static JCMediaManagerTwo JCMediaManager;
    public IjkMediaPlayer mediaPlayer;
    public static JCResizeTextureView textureView;

    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;
    public int lastState;
    public int bufferPercent;
    public int backUpBufferState = -1;
    public int videoRotation;

    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_SETDISPLAY = 1;
    public static final int HANDLER_RELEASE = 2;
    HandlerThread mMediaHandlerThread;
    JCMediaManagerTwo.MediaHandler mMediaHandler;
    Handler mainThreadHandler;

    public static JCMediaManagerTwo instance() {
        if (JCMediaManager == null) {
            JCMediaManager = new JCMediaManagerTwo();
        }
        return JCMediaManager;
    }

    public JCMediaManagerTwo() {
        mediaPlayer = new IjkMediaPlayer();
        mMediaHandlerThread = new HandlerThread(TAG);
        mMediaHandlerThread.start();
        mMediaHandler = new JCMediaManagerTwo.MediaHandler((mMediaHandlerThread.getLooper()));
        mainThreadHandler = new Handler();
    }

    public Point getVideoSize() {
        if (currentVideoWidth != 0 && currentVideoHeight != 0) {
            return new Point(currentVideoWidth, currentVideoHeight);
        } else {
            return null;
        }
    }

    public class MediaHandler extends Handler {
        public MediaHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_PREPARE:
                    try {
                        currentVideoWidth = 0;
                        currentVideoHeight = 0;
                        mediaPlayer.release();
                        mediaPlayer = new IjkMediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(((JCMediaManagerTwo.FuckBean) msg.obj).url, ((JCMediaManagerTwo.FuckBean) msg.obj).mapHeadData);
                        mediaPlayer.setLooping(((JCMediaManagerTwo.FuckBean) msg.obj).looping);
                        mediaPlayer.setOnPreparedListener(JCMediaManagerTwo.this);
                        mediaPlayer.setOnCompletionListener(JCMediaManagerTwo.this);
                        mediaPlayer.setOnBufferingUpdateListener(JCMediaManagerTwo.this);
                        mediaPlayer.setScreenOnWhilePlaying(true);
                        mediaPlayer.setOnSeekCompleteListener(JCMediaManagerTwo.this);
                        mediaPlayer.setOnErrorListener(JCMediaManagerTwo.this);
                        mediaPlayer.setOnInfoListener(JCMediaManagerTwo.this);
                        mediaPlayer.setOnVideoSizeChangedListener(JCMediaManagerTwo.this);
                        mediaPlayer.prepareAsync();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case HANDLER_SETDISPLAY:
                    if (msg.obj == null) {
                        instance().mediaPlayer.setSurface(null);
                    } else {
                        Surface holder = (Surface) msg.obj;
                        if (holder.isValid()) {
                            Log.i(TAG, "set surface");
                            instance().mediaPlayer.setSurface(holder);
                            mainThreadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textureView.requestLayout();
                                }
                            });
                        }
                    }
                    break;
                case HANDLER_RELEASE:
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    break;
            }
        }
    }


    public void prepare(final String url, final Map<String, String> mapHeadData, boolean loop) {
        if (TextUtils.isEmpty(url)) return;
        Message msg = new Message();
        msg.what = HANDLER_PREPARE;
        JCMediaManagerTwo.FuckBean fb = new JCMediaManagerTwo.FuckBean(url, mapHeadData, loop);
        msg.obj = fb;
        mMediaHandler.sendMessage(msg);
    }

    public void releaseMediaPlayer() {
        Message msg = new Message();
        msg.what = HANDLER_RELEASE;
        mMediaHandler.sendMessage(msg);
    }

    public void setDisplay(Surface holder) {
        Message msg = new Message();
        msg.what = HANDLER_SETDISPLAY;
        msg.obj = holder;
        mMediaHandler.sendMessage(msg);
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManagerTwo.getFirst() != null) {
                    JCVideoPlayerManagerTwo.getFirst().onPrepared();
                }
            }
        });
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManagerTwo.getFirst() != null) {
                    JCVideoPlayerManagerTwo.getFirst().onAutoCompletion();
                }
            }
        });
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, final int percent) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManagerTwo.getFirst() != null) {
                    JCVideoPlayerManagerTwo.getFirst().onBufferingUpdate(percent);
                }
            }
        });
    }

    @Override
    public void onSeekComplete(IMediaPlayer mp) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManagerTwo.getFirst() != null) {
                    JCVideoPlayerManagerTwo.getFirst().onSeekComplete();
                }
            }
        });
    }

    @Override
    public boolean onError(IMediaPlayer mp, final int what, final int extra) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManagerTwo.getFirst() != null) {
                    JCVideoPlayerManagerTwo.getFirst().onError(what, extra);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, final int what, final int extra) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManagerTwo.getFirst() != null) {
                    JCVideoPlayerManagerTwo.getFirst().onInfo(what, extra);
                }
            }
        });
        return false;
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
        currentVideoWidth = mp.getVideoWidth();
        currentVideoHeight = mp.getVideoHeight();
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManagerTwo.getFirst() != null) {
                    JCVideoPlayerManagerTwo.getFirst().onVideoSizeChanged();
                }
            }
        });
    }


    private class FuckBean {
        String url;
        Map<String, String> mapHeadData;
        boolean looping;

        FuckBean(String url, Map<String, String> mapHeadData, boolean loop) {
            this.url = url;
            this.mapHeadData = mapHeadData;
            this.looping = loop;
        }
    }
}
