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
    private CustomViewsHandler() {
    }

    PopupWindow popupWindow;
    static CustomViewsHandler customViewsHandler = new CustomViewsHandler();

    public static CustomViewsHandler getInstance() {
        if (customViewsHandler == null)
            customViewsHandler = new CustomViewsHandler();

        return customViewsHandler;
    }

    public PopupWindow createUserVideosPopupWindow(Context context, final PopupItemClicked popupItemClicked, final int position) {
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
                popupItemClicked.onPopupMenuClicked(v, position);
            }
        });
        addVideoToPremiumTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
                popupItemClicked.onPopupMenuClicked(v, position);
            }
        });

        deleteVideoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
                popupItemClicked.onPopupMenuClicked(v, position);
            }
        });

        return popupWindow;
    }

    public PopupWindow createUserChallengePopupWindow(Context context, final PopupItemClicked popupItemClicked, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.challenges_popup_window, null);
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
                popupItemClicked.onPopupMenuClicked(v, position);
            }
        });

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
