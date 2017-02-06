package com.example.divyanshu.smyt.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class InfoActivity extends BaseActivity {


    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.webView)
    WebView webView;
    @InjectView(R.id.activity_info)
    LinearLayout activityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.inject(this);

        initViews();
    }

    private void initViews() {
        Utils.configureToolbarWithBackButton(this, toolbarView, getString(R.string.info));
        CallWebService.getInstance(this, true, ApiCodes.GET_RULES).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_RULES, null, this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String data = response.getString(Constants.DATA);
        webView.loadDataWithBaseURL("", data, mimeType, encoding, "");
    }
}
