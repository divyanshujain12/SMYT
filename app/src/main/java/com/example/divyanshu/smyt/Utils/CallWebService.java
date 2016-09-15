package com.example.divyanshu.smyt.Utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.activities.MyApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by Divyanshu jain on 30-10-2015.
 */
public class CallWebService implements Response.ErrorListener, Response.Listener {

    private static Context context = null;

    private static CallWebService instance = null;

    private static CustomProgressDialog progressDialog = null;

    public static int GET = Request.Method.GET;
    public static int POST = Request.Method.POST;
    public static int PUT = Request.Method.PUT;
    public static int DELETE = Request.Method.DELETE;
    private ObjectResponseCallBack objectCallBackInterface;
    private ArrayResponseCallback arrayCallBackInterface;
    private static int apiCode = 0;
    private String url;

    public static CallWebService getInstance(Context context, boolean showProgressBar, int apiCode) {
        instance.context = context;
        if (context != null && showProgressBar)
            progressDialog = new CustomProgressDialog(context);
        else
            progressDialog = null;

        if (instance == null) {
            instance = new CallWebService();
        }
        CallWebService.apiCode = apiCode;
        return instance;
    }

    public void hitJsonObjectRequestAPI(int requestType, final String url, JSONObject json, final ObjectResponseCallBack callBackInterface) {
        objectCallBackInterface = callBackInterface;
        this.url = url;
        if (progressDialog != null)
            progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(requestType, url, json == null ? null : (json), this, this);
        addRequestToVolleyQueue(url, request);
    }

    public void hitJsonArrayRequestAPI(int requestType, final String url, JSONArray json, final ArrayResponseCallback callBackinerface) {
        arrayCallBackInterface = callBackinerface;
        this.url = url;
        if (progressDialog != null)
            progressDialog.show();

        JsonArrayRequest request = new JsonArrayRequest(requestType, url, json == null ? null : (json), this, this);
        addRequestToVolleyQueue(url, request);
    }

    private void addRequestToVolleyQueue(String url, Request request) {
        RetryPolicy policy = new DefaultRetryPolicy(Constants.REQUEST_TIMEOUT_TIME, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MyApp.getInstance().addToRequestQueue(request, url);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        MyApp.getInstance().getRequestQueue().getCache().invalidate(url, true);
        error = configureErrorMessage(error);
        onError(error.getMessage());
    }


    @Override
    public void onResponse(Object response) {
        MyApp.getInstance().getRequestQueue().getCache().invalidate(url, true);
        if (progressDialog != null)
            progressDialog.dismiss();

        if (response instanceof JSONObject) {
            onJsonObjectResponse((JSONObject) response);
        } else if (response instanceof JSONArray) {
            onJsonArrayResponse((JSONArray) response);
        }

    }


    private void onJsonObjectResponse(JSONObject response) {
        try {
            objectCallBackInterface.onJsonObjectSuccess(response, apiCode);
        } catch (final JSONException e) {
            onError(e.getMessage());
            e.printStackTrace();
        }
    }

    private void onJsonArrayResponse(JSONArray response) {
        try {
            arrayCallBackInterface.onJsonArraySuccess(response, apiCode);
        } catch (final JSONException e) {
            onError(e.getMessage());
            e.printStackTrace();
        }
    }

    public interface ObjectResponseCallBack {
        void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException;

        void onFailure(String str, int apiType);
    }

    public interface ArrayResponseCallback {
        void onJsonArraySuccess(JSONArray array, int apiType) throws JSONException;

        void onFailure(String str, int apiType);
    }

    private void onError(String error) {
        if (progressDialog != null)
            progressDialog.dismiss();
        objectCallBackInterface.onFailure(error, apiCode);
    }
   /*  public interface StringResponseCallback {
        void onStringSuccess(String array) throws JSONException;

        void onFailure(String str);
    }*/

    private VolleyError configureErrorMessage(VolleyError volleyError) {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }
        return volleyError;
    }
}