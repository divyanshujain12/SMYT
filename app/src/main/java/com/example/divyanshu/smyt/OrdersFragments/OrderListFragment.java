package com.example.divyanshu.smyt.OrdersFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Adapters.OrderListAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.OrdersModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu on 13/12/16.
 */

public class OrderListFragment extends BaseFragment {

    @InjectView(R.id.orderListRV)
    RecyclerView orderListRV;
    ArrayList<OrdersModel> ordersModels;
    OrderListAdapter orderListAdapter;

    public static OrderListFragment getInstance() {
        return new OrderListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, null);
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
        ordersModels = new ArrayList<>();
        orderListRV.setLayoutManager(new LinearLayoutManager(getContext()));
        orderListAdapter = new OrderListAdapter(getContext(), ordersModels, this);
        orderListRV.setAdapter(orderListAdapter);
        CallWebService.getInstance(getContext(), true, ApiCodes.MANAGE_ORDERS).hitJsonObjectRequestAPI(CallWebService.POST, API.MANAGE_ORDERS, createJsonForGetOrders(), this);
    }

    private JSONObject createJsonForGetOrders() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        parserJson(response);


    }

    private void parserJson(JSONObject response) {
        try {
            JSONObject data = response.getJSONObject(Constants.DATA);
            if (data.has("banner_subscription_normal")) {
                JSONObject jsonObject = data.getJSONObject("banner_subscription_normal");
                parserOrdersByKey(jsonObject);
            }
            if (data.has("banner_subscription_premium")) {
                JSONObject jsonObject = data.getJSONObject("banner_subscription_premium");
                parserOrdersByKey(jsonObject);
            }
            if (data.has("category_subscription_premium")) {
                JSONObject jsonObject = data.getJSONObject("category_subscription_premium");
                parserOrdersByKey(jsonObject);
            }
            orderListAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parserOrdersByKey(JSONObject jsonObject) throws JSONException {
        String validTill = jsonObject.getString("valid_till");
        validTill = Utils.formatDateAndTime(Long.parseLong(validTill), Utils.DATE_FORMAT);
        String title = jsonObject.getString(Constants.TITLE);
        String desc = jsonObject.getString(Constants.DESC);
        String videoCount = "Unlimited";
        if (jsonObject.has("video_count")) {
            videoCount = jsonObject.getString("video_count");
            videoCount = videoCount + " " + "videos";
        }

        OrdersModel ordersModel = new OrdersModel();
        ordersModel.setTitle(title);
        ordersModel.setDescription(desc);
        ordersModel.setValid_till(validTill);
        ordersModel.setVideo_count(videoCount);

        ordersModels.add(ordersModel);
    }

    @Override
    public void onFailure(String str, int apiType) {
        super.onFailure(str, apiType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
