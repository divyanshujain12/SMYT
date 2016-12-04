package com.example.divyanshu.smyt.Utils;

import android.app.Activity;
import android.content.Context;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.divyanshu.smyt.Constants.ApiCodes.ADD_BANNER;
import static com.example.divyanshu.smyt.Constants.ApiCodes.ADD_VIDEO_TO_PREMIUM;
import static com.example.divyanshu.smyt.Constants.ApiCodes.CHECK_BANNER_SUBSCRIPTION;
import static com.example.divyanshu.smyt.Constants.ApiCodes.CHECK_CATEGORY_SUBSCRIPTION_FOR_NEW_VIDEO;

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


    public void checkBannerAvailability(Context context, String categoryType) {
        this.context = context;
        CallWebService.getInstance(context, true, CHECK_BANNER_SUBSCRIPTION).hitJsonObjectRequestAPI(CallWebService.POST, API.CHECK_BANNER_SUBSCRIPTION, createJsonForCheckBannerAvailability(categoryType), this);
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
                break;
            case ADD_VIDEO_TO_PREMIUM:
                CommonFunctions.getInstance().showSuccessSnackBar(((Activity) context), context.getString(R.string.video_added_successfully));
                break;
        }

    }

    @Override
    public void onFailure(String str, int apiType) {

        switch (apiType) {
            case CHECK_BANNER_SUBSCRIPTION:
                inAppAvailabilityCalBack.notAvailable(purchaseType);
                break;
            case CHECK_CATEGORY_SUBSCRIPTION_FOR_NEW_VIDEO:
                inAppAvailabilityCalBack.notAvailable(purchaseType);
                break;
            default:
                CommonFunctions.getInstance().showSuccessSnackBar(((Activity) context), str);
                break;
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
            jsonObject.put(Constants.VALID_TILL, Utils.getNextTwentyFourHoursInMS(currentTime));
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
    }
}
