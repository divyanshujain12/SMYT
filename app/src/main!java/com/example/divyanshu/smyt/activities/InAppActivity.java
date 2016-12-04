package com.example.divyanshu.smyt.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.example.divyanshu.smyt.Adapters.InAppPurchaseRVAdapter;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Interfaces.InAppCallback;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.divyanshu.smyt.Constants.Constants.*;

public class InAppActivity extends AppCompatActivity implements RecyclerViewClick, InAppCallback {

    public final static int OTHER_CATEGORY_BANNER = 1;
    public final static int OTHER_CATEGORY_TO_PREMIUM = 2;
    public final static int PREMIUM_CATEGORY_BANNER = 3;
    public static int PURCHASE_REQUEST = 111;

    public InAppActivity() {

    }

    @InjectView(R.id.purchaseTitle)
    TextView purchaseTitle;
    @InjectView(R.id.productsRV)
    RecyclerView productsRV;
    private List<SkuDetails> skuDetailsList;
    private BillingProcessor billingProcessor;
    private int type = 0;

    public static InAppActivity getInstance() {
        return new InAppActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_app_purchase_layout);
        ButterKnife.inject(this);

        type = getIntent().getIntExtra(Constants.IN_APP_TYPE, 0);

        billingProcessor = MyApp.getInstance().getBillingProcessor(this);

        switch (type) {
            case OTHER_CATEGORY_BANNER:
                purchaseTitle.setText("Add Videos To Banner Packages");
                skuDetailsList = billingProcessor.getPurchaseListingDetails(createOtherCategoryBannerArrayList());

                break;
            case OTHER_CATEGORY_TO_PREMIUM:
                purchaseTitle.setText("Add Videos On Premium Categories Packages");
                skuDetailsList = billingProcessor.getPurchaseListingDetails(createOtherCategoryToPremium());

                break;
            case PREMIUM_CATEGORY_BANNER:
                purchaseTitle.setText("Add Videos On Premium Category Banner Packages");
                skuDetailsList = billingProcessor.getPurchaseListingDetails(createPremiumCategoryBannerArrayList());
                break;
        }
        productsRV.setLayoutManager(new LinearLayoutManager(this));
        productsRV.setAdapter(new InAppPurchaseRVAdapter(skuDetailsList, this));

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

    private ArrayList<String> createPremiumCategoryBannerArrayList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(PREMIUM_CATEGORY_BANNER_SINGLE_VIDEO_PACK);
        arrayList.add(PREMIUM_CATEGORY_BANNER_THREE_VIDEOS_PACK);
        arrayList.add(PREMIUM_CATEGORY_BANNER_FIVE_VIDEOS_PACK);
        return arrayList;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClickItem(int position, View view) {
        billingProcessor.purchase(this, skuDetailsList.get(position).productId);
    }

    public void consumePurchase(String productID) {
        billingProcessor.consumePurchase(productID);
    }

    @Override
    public void onPurchasedSuccess(String productID, String transactionID) {
        Intent intent = new Intent();
        intent.putExtra(Constants.IS_PRCHASED, true);
        intent.putExtra(Constants.TYPE, type);
        intent.putExtra(Constants.TRANSACTION_ID, transactionID);
        intent.putExtra(Constants.PRODUCT_ID,productID);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onFailure() {
        Intent intent = new Intent();
        intent.putExtra(Constants.IS_PRCHASED, false);
        intent.putExtra(Constants.TYPE, type);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
