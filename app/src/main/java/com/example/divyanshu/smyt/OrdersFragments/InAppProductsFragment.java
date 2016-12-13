package com.example.divyanshu.smyt.OrdersFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.InAppLocalApis;
import com.example.divyanshu.smyt.activities.InAppActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_BANNER;
import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_TO_PREMIUM;
import static com.example.divyanshu.smyt.activities.InAppActivity.PREMIUM_CATEGORY_BANNER;

/**
 * Created by divyanshu on 13/12/16.
 */

public class InAppProductsFragment extends BaseFragment {
    @InjectView(R.id.bannerPackagesCV)
    CardView bannerPackagesCV;
    @InjectView(R.id.premiumPackagesCV)
    CardView premiumPackagesCV;
    @InjectView(R.id.premiumBannerPackagesCV)
    CardView premiumBannerPackagesCV;

    public static InAppProductsFragment getInstance() {
        InAppProductsFragment fragment = new InAppProductsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.in_app_products_fragment, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
    }

    private void initViews() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.bannerPackagesCV, R.id.premiumPackagesCV, R.id.premiumBannerPackagesCV})
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), InAppActivity.class);


        switch (view.getId()) {
            case R.id.bannerPackagesCV:
                intent.putExtra(Constants.IN_APP_TYPE, InAppActivity.OTHER_CATEGORY_BANNER);
                break;
            case R.id.premiumPackagesCV:
                intent.putExtra(Constants.IN_APP_TYPE, InAppActivity.OTHER_CATEGORY_TO_PREMIUM);
                break;
            case R.id.premiumBannerPackagesCV:
                intent.putExtra(Constants.IN_APP_TYPE, InAppActivity.PREMIUM_CATEGORY_BANNER);
                break;
        }
        startActivityForResult(intent, InAppActivity.PURCHASE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == InAppActivity.PURCHASE_REQUEST) {

                if (data.getBooleanExtra(Constants.IS_PRCHASED, false)) {

                    int type = data.getIntExtra(Constants.TYPE, 0);
                    String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
                    String productID = data.getStringExtra(Constants.PRODUCT_ID);
                    switch (type) {
                        case OTHER_CATEGORY_BANNER:
                            InAppLocalApis.getInstance().purchaseBanner(getContext(), transactionID, productID);
                            break;
                        case OTHER_CATEGORY_TO_PREMIUM:
                            InAppLocalApis.getInstance().purchaseCategory(getContext(), transactionID, productID);
                            break;
                        case PREMIUM_CATEGORY_BANNER:
                            InAppLocalApis.getInstance().purchaseBanner(getContext(), transactionID, productID);
                            break;
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

