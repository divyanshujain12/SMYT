package com.example.divyanshu.smyt.notificationFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Adapters.NotificationAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.OngoingChallengeDescriptionActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.AllNotificationModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 3/8/2017.
 */

public class NotificationFragment extends BaseFragment {
    @InjectView(R.id.notificationRV)
    RecyclerView notificationRV;
    @InjectView(R.id.noDataTV)
    TextView noDataTV;

    private NotificationAdapter notificationAdapter;
    private ArrayList<AllNotificationModel> allNotificationModels = new ArrayList<>();

    public static NotificationFragment getInstance() {
        return new NotificationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, null);

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
        notificationRV.setLayoutManager(new LinearLayoutManager(getContext()));
        CallWebService.getInstance(getContext(), true, ApiCodes.NOTIFICATION).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_NOTIFICATION, createJsonForGetNotification(), this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);

        allNotificationModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), AllNotificationModel.class);
        notificationAdapter = new NotificationAdapter(getContext(), allNotificationModels, this);
        notificationRV.setAdapter(notificationAdapter);
    }

    @Override
    public void onFailure(String str, int apiType) {
        super.onFailure(str, apiType);
        noDataTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        String status = allNotificationModels.get(position).getStatus();
        if (status.equalsIgnoreCase("Inactive")) {
            Intent intent = new Intent(getContext(), OngoingChallengeDescriptionActivity.class);
            intent.putExtra(Constants.ACCEPT_STATUS, 0);
            intent.putExtra(Constants.CHALLENGE_ID, allNotificationModels.get(position).getChallenge_id());
            getContext().startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), OngoingChallengeDescriptionActivity.class);
            intent.putExtra(Constants.CHALLENGE_ID, allNotificationModels.get(position).getChallenge_id());
            getContext().startActivity(intent);
        }

    }

    private JSONObject createJsonForGetNotification() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
