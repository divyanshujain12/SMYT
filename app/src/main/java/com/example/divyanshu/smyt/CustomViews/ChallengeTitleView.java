package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by divyanshu.jain on 11/22/2016.
 */

public class ChallengeTitleView extends LinearLayout implements View.OnClickListener {
    private TextView titleTV;
    private ImageView moreIV;
    private static PopupWindow popupWindow = null;
    private int position;
    private String title;
    private PopupItemClicked popupItemClicked;

    public ChallengeTitleView(Context context) {
        super(context);
    }

    public ChallengeTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitViews();
    }

    private void InitViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.title_view, this);
        titleTV = (TextView) findViewById(R.id.titleTV);
        moreIV = (ImageView) findViewById(R.id.moreIV);
        moreIV.setOnClickListener(this);
    }

    public void setUp(String title, PopupItemClicked popupItemClicked, int position) {
        this.title = title;
        this.popupItemClicked = popupItemClicked;
        this.position = position;
        titleTV.setText(title);
        setUpPopupWindow(popupItemClicked);
    }

    private void setUpPopupWindow(PopupItemClicked popupItemClicked) {
        popupWindow = CustomViewsHandler.getInstance().createUserChallengePopupWindow(getContext(), popupItemClicked, position);
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

    public static PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public void showHideMoreIvButton(boolean show) {
        moreIV.setVisibility(show ? VISIBLE : GONE);
    }
}
