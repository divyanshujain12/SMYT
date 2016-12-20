package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by divyanshu.jain on 11/22/2016.
 */

public class VideoTitleView extends LinearLayout implements View.OnClickListener {
    private TextView titleTV;
    private ImageView moreIV;
    private PopupWindow popupWindow = null;
    private int position;
    private String categoryID = "";

    public VideoTitleView(Context context) {
        super(context);
    }

    public VideoTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitViews();
    }

    private void InitViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.title_view, this);
        titleTV = (TextView) findViewById(R.id.titleTV);
        moreIV = (ImageView) findViewById(R.id.moreIV);
        moreIV.setOnClickListener(this);
        categoryID = MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID);
    }

    public void setUp(String title, PopupItemClicked popupItemClicked, int position) {
        this.position = position;
        titleTV.setText(title);
        setUpPopupWindow(popupItemClicked);
    }
    public void setUpForBannerVideo(String title, PopupItemClicked popupItemClicked, int position) {
        this.position = position;
        titleTV.setText(title);
        if (categoryID.equals(getContext().getString(R.string.premium_category)))
            popupWindow = new CustomViewsHandler().createUserVideosPopupWindowForPremiumBannerVideo(getContext(), popupItemClicked, position);
        else
            popupWindow = new CustomViewsHandler().createUserVideosPopupWindowForNormalBannerVideo(getContext(), popupItemClicked, position);
    }

    private void setUpPopupWindow(PopupItemClicked popupItemClicked) {
        if (categoryID.equals(getContext().getString(R.string.premium_category)))
            popupWindow = new CustomViewsHandler().createUserPremiumPopupWindow(getContext(), popupItemClicked, position);
        else
            popupWindow = new CustomViewsHandler().createUserVideosPopupWindow(getContext(), popupItemClicked, position);
    }

    @Override
    public void onClick(View v) {
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
        else
            popupWindow.showAsDropDown(v);
    }

    public TextView getTitleTV() {
        return titleTV;
    }

    public ImageView getMoreIV() {
        return moreIV;
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public void showHideMoreIvButton(boolean show) {
        moreIV.setVisibility(show ? VISIBLE : GONE);
    }

}
