package com.example.divyanshu.smyt.staticPages;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class StaticPagesActivity extends BaseActivity {

    @InjectView(R.id.webView)
    WebView webView;
    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_pages);
        ButterKnife.inject(this);
        String url = getIntent().getStringExtra(Constants.API);
        String headerName = getIntent().getStringExtra(Constants.NAME);
        Utils.configureToolbarWithBackButton(this, toolbarView, headerName);

        CallWebService.getInstance(this, true, 0).hitJsonObjectRequestAPI(CallWebService.POST, url, null, this);

    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        String data = response.getString(Constants.DATA);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        webView.loadDataWithBaseURL("", data, mimeType, encoding, "");
    }
}
