package com.example.divyanshu.smyt.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.Interfaces.AlertDialogInterface;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.activities.InAppActivity;
import com.example.divyanshu.smyt.activities.MyApp;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.divyanshu.smyt.Constants.ApiCodes.ADD_BANNER;
import static com.example.divyanshu.smyt.Constants.ApiCodes.ADD_VIDEO_TO_PREMIUM;
import static com.example.divyanshu.smyt.Constants.ApiCodes.CHECK_BANNER_SUBSCRIPTION;
import static com.example.divyanshu.smyt.Constants.ApiCodes.CHECK_CATEGORY_SUBSCRIPTION_FOR_NEW_VIDEO;
import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_BANNER;
import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_TO_PREMIUM;
import static com.example.divyanshu.smyt.activities.InAppActivity.PREMIUM_CATEGORY_BANNER;

/**
 * Created by divyanshu on 3/12/16.
 */
public class InAppLocalApis implements CallWebService.ObjectResponseCallBack {
    private Context context;
    private static InAppAvailabilityCalBack inAppAvailabilityCalBack;
    private static InAppLocalApis ourInstance = new InAppLocalApis();
    private int purchaseType = 0;

    public static InAppLocalApis getInstance() {
        return ourInstance;
    }

    private InAppLocalApis() {
    }

    public void setCallback(InAppAvailabilityCalBack inAppAvailabilityCalBack) {
        InAppLocalApis.inAppAvailabilityCalBack = inAppAvailabilityCalBack;
    }

    public void setPurchaseType(int purchaseType) {
        this.purchaseType = purchaseType;
    }


    public void checkBannerAvailability(Context context, int categoryType) {
        this.context = context;
        if (categoryType == InAppActivity.PREMIUM_CATEGORY_BANNER)
            CallWebService.getInstance(context, true, CHECK_BANNER_SUBSCRIPTION).hitJsonObjectRequestAPI(CallWebService.POST, API.CHECK_BANNER_SUBSCRIPTION, createJsonForCheckBannerAvailability(Constants.CAT_PREMIUM), this);
        else
            CallWebService.getInstance(context, true, CHECK_BANNER_SUBSCRIPTION).hitJsonObjectRequestAPI(CallWebService.POST, API.CHECK_BANNER_SUBSCRIPTION, createJsonForCheckBannerAvailability(Constants.CAT_NORMAL), this);

    }

    public void checkAddVideoInPremiumCatAvailability(Context context) {
        this.context = context;
        CallWebService.getInstance(context, true, CHECK_CATEGORY_SUBSCRIPTION_FOR_NEW_VIDEO).hitJsonObjectRequestAPI(CallWebService.POST, API.CHECK_CATEGORY_SUBSCRIPTION_FOR_NEW_VIDEO, createJsonForCheckAddVideoToPremiumCat(), this);
    }

    public void purchaseBanner(Context context, String transactionID, String productID) {
        this.context = context;
        CallWebService.getInstance(context, true, ApiCodes.PURCHASE_BANNER).hitJsonObjectRequestAPI(CallWebService.POST, API.PURCHASE_BANNER, createJsonForPurchase(transactionID, productID), this);
    }

    public void purchaseCategory(Context context, String transactionID, String productID) {
        this.context = context;
        CallWebService.getInstance(context, true, ApiCodes.PURCHASE_CATEGORY).hitJsonObjectRequestAPI(CallWebService.POST, API.PURCHASE_CATEGORY, createJsonForPurchase(transactionID, productID), this);
    }

    public void addBannerToCategory(Context context, String customerVideoID) {
        this.context = context;
        CallWebService.getInstance(context, true, ApiCodes.ADD_BANNER).hitJsonObjectRequestAPI(CallWebService.POST, API.ADD_BANNER, createJsonForAddBanner(customerVideoID), this);
    }

    public void addVideoToPremiumCategory(Context context, String customerVideoID) {
        this.context = context;
        CallWebService.getInstance(context, true, ApiCodes.ADD_VIDEO_TO_PREMIUM).hitJsonObjectRequestAPI(CallWebService.POST, API.ADD_VIDEO_OTHER_TO_PREMIUM_CATEGORY, createJsonForAddVideoToPremium(customerVideoID), this);
    }


    public void sendPurchasedDataToBackend(Context context, int requestCode, Intent data) {
        if (data != null) {
            if (requestCode == InAppActivity.PURCHASE_REQUEST) {

                if (data.getBooleanExtra(Constants.IS_PRCHASED, false)) {

                    int type = data.getIntExtra(Constants.TYPE, 0);
                    String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
                    String productID = data.getStringExtra(Constants.PRODUCT_ID);
                    switch (type) {
                        case OTHER_CATEGORY_BANNER:
                            purchaseBanner(context, transactionID, productID);
                            break;
                        case OTHER_CATEGORY_TO_PREMIUM:
                            purchaseCategory(context, transactionID, productID);
                            break;
                        case PREMIUM_CATEGORY_BANNER:
                            purchaseBanner(context, transactionID, productID);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        switch (apiType) {
            case CHECK_BANNER_SUBSCRIPTION:
                inAppAvailabilityCalBack.available(purchaseType);
                break;
            case CHECK_CATEGORY_SUBSCRIPTION_FOR_NEW_VIDEO:
                inAppAvailabilityCalBack.available(purchaseType);
                break;
            case ADD_BANNER:
                CommonFunctions.getInstance().showSuccessSnackBar(((Activity) context), context.getString(R.string.banner_added_success));
                BroadcastSenderClass.getInstance().sendBannerVideoAddedBroadcast(context);
                break;
            case ADD_VIDEO_TO_PREMIUM:
                CommonFunctions.getInstance().showSuccessSnackBar(((Activity) context), context.getString(R.string.video_added_successfully));
                break;
        }

    }

    @Override
    public void onFailure(String str, int apiType) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            String error_code = jsonObject.getString("error_code");
            String productID = jsonObject.optString(Constants.CUSTOM_ID);
            if (error_code.equalsIgnoreCase("2")) {
                if (productID != null && !productID.equals(""))
                    MyApp.getInstance().consumePurchase(productID);
                CustomAlertDialogs.showAlertDialogWithCallBacks(context, context.getString(R.string.alert), "Your package has been expired! Do you want to renew it!", new AlertDialogInterface() {
                    @Override
                    public void Yes() {
                        inAppAvailabilityCalBack.notAvailable(purchaseType);
                    }

                    @Override
                    public void No() {
                        inAppAvailabilityCalBack.negativeButtonPressed();
                    }
                });
            } else {
                CustomAlertDialogs.showAlertDialogWithCallBacks(context, context.getString(R.string.alert), "You do not have package! Would you like to purchase it!", new AlertDialogInterface() {
                    @Override
                    public void Yes() {
                        inAppAvailabilityCalBack.notAvailable(purchaseType);
                    }

                    @Override
                    public void No() {
                        inAppAvailabilityCalBack.negativeButtonPressed();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /***
     * Creating JSONS
     ****/
    private JSONObject createJsonForCheckBannerAvailability(String type) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(context);
        try {
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
            jsonObject.put(Constants.PACKAGE_BANNER_TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForCheckAddVideoToPremiumCat() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(context);
        try {
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForPurchase(String transactionID, String productID) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(context);
        try {
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
            jsonObject.put(Constants.TRANSACTION_ID, transactionID);
            jsonObject.put(Constants.STATUS, Constants.SUCCESS);
            jsonObject.put(Constants.CUSTOM_ID, productID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForAddBanner(String customerVideoID) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(context);
        try {
            jsonObject.put(Constants.CATEGORY_ID, MySharedPereference.getInstance().getString(context, Constants.CATEGORY_ID));
            long currentTime = Utils.getCurrentTimeInMillisecond();
            jsonObject.put(Constants.E_DATE, currentTime);
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
            jsonObject.put(Constants.VALID_TILL, Utils.getNextSeventyTwoHoursInMS(currentTime));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForAddVideoToPremium(String customerVideoID) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(context);
        try {
            jsonObject.put(Constants.CATEGORY_ID, MySharedPereference.getInstance().getString(context, Constants.CATEGORY_ID));
            long currentTime = Utils.getCurrentTimeInMillisecond();
            jsonObject.put(Constants.E_DATE, currentTime);
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public interface InAppAvailabilityCalBack {
        void available(int purchaseType);
        void notAvailable(int purchaseType);

        void negativeButtonPressed();
    }
}
