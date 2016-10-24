package com.player.divyanshu.customvideoplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
public class ResizeTextureView extends TextureView {
    protected static final String TAG = "JCResizeTextureView";
    protected static final boolean DEBUG = false;

    // x as width, y as height
    protected Point mVideoSize;
    protected boolean hasUpdated;

    public ResizeTextureView(Context context) {
        super(context);
        init();
    }

    public ResizeTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public Bitmap getBitmap() {
        if (hasUpdated) {
            return super.getBitmap();
        } else {
            return null;
        }
    }

    @Override
    public Bitmap getBitmap(int width, int height) {
        if (hasUpdated) {
            return super.getBitmap(width, height);
        } else {
            return null;
        }
    }

    @Override
    public Bitmap getBitmap(Bitmap bitmap) {
        if (hasUpdated) {
            return super.getBitmap(bitmap);
        } else {
            return null;
        }
    }

    private void init() {
        mVideoSize = new Point(0, 0);
    }

    public void setVideoSize(Point videoSize) {
        if (videoSize != null && !mVideoSize.equals(videoSize)) {
            this.mVideoSize = videoSize;
            requestLayout();
        }
    }

    @Override
    public void setRotation(float rotation) {
        if (rotation != getRotation()) {
            super.setRotation(rotation);
            requestLayout();
        }
    }

    public void setHasUpdated() {
        hasUpdated = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewRotation = (int) getRotation();
        if (viewRotation == 90 || viewRotation == 270) {
            int tempMeasureSpec = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = tempMeasureSpec;
        }

        if (DEBUG) {
            Log.i(TAG, "onMeasure " + " [" + this.hashCode() + "] ");
            Log.i(TAG, "viewRotation = " + viewRotation);
        }

        int videoWidth = mVideoSize.x;
        int videoHeight = mVideoSize.y;

        if (DEBUG) {
            Log.i(TAG, "videoWidth = " + videoWidth + ", " + "videoHeight = " + videoHeight);
            if (videoWidth > 0 && videoHeight > 0) {
                Log.i(TAG, "videoWidth / videoHeight = " + videoWidth / videoHeight);
            }
        }

        int width = getDefaultSize(videoWidth, widthMeasureSpec);
        int height = getDefaultSize(videoHeight, heightMeasureSpec);
        if (videoWidth > 0 && videoHeight > 0) {

            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (DEBUG) {
                Log.i(TAG, "widthMeasureSpec  [" + MeasureSpec.toString(widthMeasureSpec) + "]");
                Log.i(TAG, "heightMeasureSpec [" + MeasureSpec.toString(heightMeasureSpec) + "]");
            }

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                if (videoWidth * height < width * videoHeight) {
                    width = height * videoWidth / videoHeight;
                } else if (videoWidth * height > width * videoHeight) {
                    height = width * videoHeight / videoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * videoHeight / videoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                    width = height * videoWidth / videoHeight;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * videoWidth / videoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                    height = width * videoHeight / videoWidth;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = videoWidth;
                height = videoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * videoWidth / videoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * videoHeight / videoWidth;
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }
        if (DEBUG) {
            Log.i(TAG, "viewWidth = " + width + ", " + "viewHeight = " + height);
            Log.i(TAG, "viewWidth / viewHeight = " + width / height);
        }
        setMeasuredDimension(width, height);
    }
}
