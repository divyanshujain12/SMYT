package com.example.divyanshu.smyt.CustomViews;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.example.divyanshu.smyt.Adapters.InAppPurchaseRVAdapter;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.divyanshu.smyt.Constants.Constants.OTHER_CATEGORY_BANNER_FIVE_VIDEOS_PACK;
import static com.example.divyanshu.smyt.Constants.Constants.OTHER_CATEGORY_BANNER_SINGLE_VIDEOS_PACK;
import static com.example.divyanshu.smyt.Constants.Constants.OTHER_CATEGORY_BANNER_THREE_VIDEOS_PACK;
import static com.example.divyanshu.smyt.Constants.Constants.PREMIUM_NEW_MONTHLY_VIDEO_PACK;
import static com.example.divyanshu.smyt.Constants.Constants.PREMIUM_NEW_SINGLE_VIDEO_PACK;

/**
 * Created by divyanshu.jain on 11/17/2016.
 */
public class InAppDialogs {
    private static InAppDialogs ourInstance = new InAppDialogs();

    public static InAppDialogs getInstance() {
        return ourInstance;
    }

    private AlertDialog alertDialog;
    private TextView purchaseTitle;
    private RecyclerView productsRV;

    private InAppDialogs() {
    }

    public void showOtherCategoryBannerDialog(final Activity activity, final BillingProcessor billingProcessor) {
        alertDialog = new AlertDialog.Builder(activity).create();
        setupFullWidthDialog();
        final List<SkuDetails> skuDetailsList = billingProcessor.getPurchaseListingDetails(createOtherCategoryBannerArrayList());
        View view = getView(activity, billingProcessor, skuDetailsList);
        alertDialog.setView(view);
        alertDialog.show();

    }

    public void showOtherCategoryToPremiumDialog(final Activity activity, final BillingProcessor billingProcessor) {
        alertDialog = new AlertDialog.Builder(activity).create();
        setupFullWidthDialog();
        final List<SkuDetails> skuDetailsList = billingProcessor.getPurchaseListingDetails(createOtherCategoryToPremium());
        View view = getView(activity, billingProcessor, skuDetailsList);
        alertDialog.setView(view);
        alertDialog.show();

    }

    @NonNull
    private View getView(final Activity activity, final BillingProcessor billingProcessor, final List<SkuDetails> skuDetailsList) {
        View view = View.inflate(activity, R.layout.in_app_purchase_layout, null);
        purchaseTitle = (TextView) view.findViewById(R.id.purchaseTitle);
        productsRV = (RecyclerView) view.findViewById(R.id.productsRV);
        productsRV.setLayoutManager(new LinearLayoutManager(activity));
        productsRV.setAdapter(new InAppPurchaseRVAdapter(skuDetailsList, new RecyclerViewClick() {
            @Override
            public void onClickItem(int position, View view) {
                billingProcessor.purchase(activity, skuDetailsList.get(position).productId);
                alertDialog.dismiss();
            }
        }));
        return view;
    }


    private ArrayList<String> createOtherCategoryBannerArrayList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(OTHER_CATEGORY_BANNER_SINGLE_VIDEOS_PACK);
        arrayList.add(OTHER_CATEGORY_BANNER_THREE_VIDEOS_PACK);
        arrayList.add(OTHER_CATEGORY_BANNER_FIVE_VIDEOS_PACK);
        return arrayList;
    }

    private ArrayList<String> createOtherCategoryToPremium() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(PREMIUM_NEW_SINGLE_VIDEO_PACK);
        arrayList.add(PREMIUM_NEW_MONTHLY_VIDEO_PACK);
        return arrayList;
    }

    private void setupFullWidthDialog() {
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
       /*  android:debuggable="true"
        tools:ignore="HardcodedDebugMode"*/
    }


}
