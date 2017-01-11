package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by divyanshu.jain on 10/3/2016.
 */
public class CustomViewsHandler {
    private int pos;
    public CustomViewsHandler() {
    }

    PopupWindow popupWindow;
PopupItemClicked popupItemClicked;
    public PopupWindow createUserVideosPopupWindow(Context context, PopupItemClicked clicked, int position) {
        this.pos = position;
        this.popupItemClicked = clicked;
        View view = LayoutInflater.from(context).inflate(R.layout.videos_popup_window, null);
        TextView addVideoToBannerTV = (TextView) view.findViewById(R.id.addVideoToBannerTV);
        TextView addVideoToPremiumTV = (TextView) view.findViewById(R.id.addVideoToPremiumTV);
        TextView deleteVideoTV = (TextView) view.findViewById(R.id.deleteVideoTV);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        addVideoToBannerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
                popupItemClicked.onPopupMenuClicked(v, pos);
            }
        });
        addVideoToPremiumTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
                popupItemClicked.onPopupMenuClicked(v, pos);
            }
        });

        deleteVideoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
                popupItemClicked.onPopupMenuClicked(v, pos);
            }
        });

        return popupWindow;
    }

    public PopupWindow createUserVideosPopupWindowForNormalBannerVideo(Context context, PopupItemClicked clicked, int position) {
        this.pos = position;
        this.popupItemClicked = clicked;
        View view = LayoutInflater.from(context).inflate(R.layout.banner_videos_popup_window, null);
        TextView addVideoToBannerTV = (TextView) view.findViewById(R.id.addVideoToBannerTV);
        TextView addVideoToPremiumTV = (TextView) view.findViewById(R.id.addVideoToPremiumTV);
        TextView deleteVideoTV = (TextView) view.findViewById(R.id.deleteVideoTV);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        addVideoToPremiumTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
                popupItemClicked.onPopupMenuClicked(v, pos);
            }
        });

        deleteVideoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
                popupItemClicked.onPopupMenuClicked(v, pos);
            }
        });

        return popupWindow;
    }

    public PopupWindow createUserVideosPopupWindowForPremiumBannerVideo(Context context, PopupItemClicked clicked, int position) {
        this.popupItemClicked = clicked;
        this.pos = position;
        View view = LayoutInflater.from(context).inflate(R.layout.banner_videos_premium_popup_window, null);
        TextView addVideoToBannerTV = (TextView) view.findViewById(R.id.addVideoToBannerTV);
        TextView addVideoToPremiumTV = (TextView) view.findViewById(R.id.addVideoToPremiumTV);
        TextView deleteVideoTV = (TextView) view.findViewById(R.id.deleteVideoTV);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        deleteVideoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
                popupItemClicked.onPopupMenuClicked(v, pos);
            }
        });

        return popupWindow;
    }

    public PopupWindow createUserPremiumPopupWindow(Context context, PopupItemClicked clicked, int position) {
        this.popupItemClicked = clicked;
        this.pos = position;
        View view = LayoutInflater.from(context).inflate(R.layout.premium_popup_window, null);
        TextView addVideoToBannerTV = (TextView) view.findViewById(R.id.addVideoToBannerTV);
        TextView deleteVideoTV = (TextView) view.findViewById(R.id.deleteVideoTV);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        addVideoToBannerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
                popupItemClicked.onPopupMenuClicked(v, pos);
            }
        });

        deleteVideoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
                popupItemClicked.onPopupMenuClicked(v, pos);
            }
        });

        return popupWindow;
    }


    public PopupWindow createChallengesPopupWindow(Context context, PopupItemClicked clicked,final int position) {
        this.popupItemClicked = clicked;
        this.pos = position;
        View view = LayoutInflater.from(context).inflate(R.layout.banner_videos_premium_popup_window, null);
        TextView deleteVideoTV = (TextView) view.findViewById(R.id.deleteVideoTV);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        deleteVideoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
                popupItemClicked.onPopupMenuClicked(v, position);
            }
        });

        return popupWindow;
    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }
}
