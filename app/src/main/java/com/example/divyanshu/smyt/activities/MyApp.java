package com.example.divyanshu.smyt.activities;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.SingletonClass;
import com.example.divyanshu.smyt.Interfaces.InAppCallback;
import com.example.divyanshu.smyt.Utils.LruBitmapCache;

/**
 * Created by divyanshu.jain on 9/15/2016.
 */
public class MyApp extends Application implements BillingProcessor.IBillingHandler {
    private static final String TAG = MyApp.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static MyApp mInstance;
    private InAppCallback inAppCallback;

    public BillingProcessor getBillingProcessor(InAppCallback inAppCallback) {
        this.inAppCallback = inAppCallback;
        return billingProcessor;
    }

    private BillingProcessor billingProcessor;

    @Override
    public void onCreate() {

        super.onCreate();
        SingletonClass.initInstance();
        mInstance = this;
        billingProcessor = new BillingProcessor(getApplicationContext(), Constants.LICENSE_KEY, Constants.MERCHANT_ID, this);
    }

    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
        Log.w(TAG, "Low Memory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if (inAppCallback != null)
            inAppCallback.onPurchasedSuccess(productId, details.purchaseInfo.purchaseData.purchaseToken);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        if (inAppCallback != null)
            inAppCallback.onFailure();
    }

    @Override
    public void onBillingInitialized() {

    }

    public void consumePurchase(String productID) {
        billingProcessor.consumePurchase(productID);
    }
}
