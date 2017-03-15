package com.example.divyanshu.smyt.CustomViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Interfaces.TitleBarButtonClickCallback;
import com.example.divyanshu.smyt.Interfaces.PopupItemClicked;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.InAppLocalApis;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.activities.InAppActivity;
import com.neopixl.pixlui.components.textview.TextView;

import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_BANNER;
import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_TO_PREMIUM;
import static com.example.divyanshu.smyt.activities.InAppActivity.PREMIUM_CATEGORY_BANNER;

/**
 * Created by divyanshu.jain on 11/22/2016.
 */

public class ChallengeRoundTitleView extends LinearLayout implements View.OnClickListener, PopupItemClicked, InAppLocalApis.InAppAvailabilityCalBack {
    private TextView titleTV;
    private ImageView moreIV, favIV;
    private PopupWindow popupWindow = null;
    private int position;
    private String categoryID = "";
    private String customerVideoID = "";
    private TitleBarButtonClickCallback titleBarButtonClickCallback;

    public ChallengeRoundTitleView(Context context) {
        super(context);
    }

    public ChallengeRoundTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitViews();
    }

    private void InitViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.title_view, this);
        titleTV = (TextView) findViewById(R.id.titleTV);
        moreIV = (ImageView) findViewById(R.id.moreIV);
        favIV = (ImageView) findViewById(R.id.favIV);

        favIV.setOnClickListener(this);
        moreIV.setOnClickListener(this);
        categoryID = MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID);
    }

    public void setUpForBannerVideo(String title, int position, String customerVideoID, TitleBarButtonClickCallback titleBarButtonClickCallback) {
        this.position = position;
        titleTV.setText(title);
        this.customerVideoID = customerVideoID;
        this.titleBarButtonClickCallback = titleBarButtonClickCallback;
        if (categoryID.equals(getContext().getString(R.string.premium_category)))
            popupWindow = new CustomViewsHandler().createUserVideosPopupWindowForPremiumBannerVideo(getContext(), this, position);
        else
            popupWindow = new CustomViewsHandler().createUserVideosPopupWindowForNormalBannerVideo(getContext(), this, position);
    }

    public void setUpForSingleVideo(String title, int position, String customerVideoID, TitleBarButtonClickCallback titleBarButtonClickCallback) {
        this.position = position;
        titleTV.setText(title);
        this.customerVideoID = customerVideoID;
        this.titleBarButtonClickCallback = titleBarButtonClickCallback;
        setUpPopupWindow();
    }

    public void setUpViewsForListing(String title, int position, String customerVideoID, TitleBarButtonClickCallback titleBarButtonClickCallback) {
        this.position = position;
        titleTV.setText(title);
        this.customerVideoID = customerVideoID;
        this.titleBarButtonClickCallback = titleBarButtonClickCallback;
        setUpPopupWindow();
    }

    public void setUpViewsForFavVideosListing(String title, int position, String customerVideoID, TitleBarButtonClickCallback titleBarButtonClickCallback) {
        this.position = position;
        titleTV.setText(title);
        this.customerVideoID = customerVideoID;
        this.titleBarButtonClickCallback = titleBarButtonClickCallback;

        popupWindow = new CustomViewsHandler().createFavVideosPopupWindow(getContext(), this, position);
       // setUpPopupWindow();
    }

    private void setUpPopupWindow() {
        if (categoryID.equals(getContext().getString(R.string.premium_category)))
            popupWindow = new CustomViewsHandler().createUserPremiumPopupWindow(getContext(), this, position);
        else
            popupWindow = new CustomViewsHandler().createUserVideosPopupWindow(getContext(), this, position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moreIV:
                onMoreButtonClick(v);
                break;
            case R.id.favIV:
                titleBarButtonClickCallback.onTitleBarButtonClicked(v, position);
                break;
        }

    }

    private void onMoreButtonClick(View v) {
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
        switch (view.getId()) {
            case R.id.addVideoToBannerTV:
                if (MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID).equals(getContext().getString(R.string.premium_category)))
                    checkAndPayForBannerVideo(PREMIUM_CATEGORY_BANNER);
                else
                    checkAndPayForBannerVideo(OTHER_CATEGORY_BANNER);
                break;
            case R.id.addVideoToPremiumTV:
                checkAndPayForAddVideoToPremium(OTHER_CATEGORY_TO_PREMIUM);
                break;
            case R.id.deleteVideoTV:
                titleBarButtonClickCallback.onTitleBarButtonClicked(view, position);
                break;
        }
    }

    private void checkAndPayForBannerVideo(int purchaseType) {
        setUpAvailabilityPurchase(purchaseType);
        InAppLocalApis.getInstance().checkBannerAvailability(getContext(), purchaseType);
    }

    private void checkAndPayForAddVideoToPremium(int purchaseType) {
        setUpAvailabilityPurchase(purchaseType);
        InAppLocalApis.getInstance().checkAddVideoInPremiumCatAvailability(getContext());
    }

    private void setUpAvailabilityPurchase(int purchaseType) {
        InAppLocalApis.getInstance().setCallback(this);
        InAppLocalApis.getInstance().setPurchaseType(purchaseType);

    }

    @Override
    public void available(int purchaseType) {
        switch (purchaseType) {
            case OTHER_CATEGORY_BANNER:
                InAppLocalApis.getInstance().addBannerToCategory(getContext(), customerVideoID);
                break;
            case OTHER_CATEGORY_TO_PREMIUM:
                InAppLocalApis.getInstance().addVideoToPremiumCategory(getContext(), customerVideoID);
                break;
            case PREMIUM_CATEGORY_BANNER:
                InAppLocalApis.getInstance().addBannerToCategory(getContext(), customerVideoID);
                break;
        }
    }

    @Override
    public void notAvailable(int purchaseType) {
        Intent intent = new Intent(getContext(), InAppActivity.class);
        intent.putExtra(Constants.IN_APP_TYPE, purchaseType);
        ((Activity) getContext()).startActivityForResult(intent, InAppActivity.PURCHASE_REQUEST);
    }

    @Override
    public void negativeButtonPressed() {

    }

    public void hideFavButton() {
        favIV.setVisibility(GONE);
    }

    public void setUpFavIVButton(int favStatus) {
        if (favStatus == 0)
            favIV.setImageResource(R.drawable.ic_fav_un_select);
        else
            favIV.setImageResource(R.drawable.ic_fav_select);
    }
}
