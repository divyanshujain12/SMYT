package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * Created by divyanshu on 1/11/2017.
 */

public class ChallengeTitleView extends LinearLayout implements View.OnClickListener, PopupItemClicked {
    private TextView titleTV;
    private ImageView moreIV, favIV;
    private PopupWindow popupWindow = null;
    private int position;
    private String categoryID = "";

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
        favIV = (ImageView) findViewById(R.id.favIV);
        favIV.setVisibility(GONE);
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

        popupWindow = new CustomViewsHandler().createChallengesPopupWindow(getContext(), popupItemClicked, position);

    }

    private void setUpPopupWindow(PopupItemClicked popupItemClicked) {
        popupWindow = new CustomViewsHandler().createChallengesPopupWindow(getContext(), popupItemClicked, position);
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

    @Override
    public void onPopupMenuClicked(View view, int position) {

    }
}
