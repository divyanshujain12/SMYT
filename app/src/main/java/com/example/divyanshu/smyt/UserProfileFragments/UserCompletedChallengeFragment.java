package com.example.divyanshu.smyt.UserProfileFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.divyanshu.smyt.Adapters.UserCompletedChallengesAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserCompletedChallengeFragment extends BaseFragment {
    @InjectView(R.id.challengesRV)
    RecyclerView challengesRV;
    private UserCompletedChallengesAdapter userCompletedChallengesAdapter;
    private ArrayList<ChallengeModel> challengeModels = new ArrayList<>();
    private TSnackbar tSnackbar;

    public UserCompletedChallengeFragment() {
        // Required empty public constructor
    }

    public static UserCompletedChallengeFragment newInstance() {
        UserCompletedChallengeFragment fragment = new UserCompletedChallengeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_completed_challenge, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitViews();
    }

    private void InitViews() {
        userCompletedChallengesAdapter = new UserCompletedChallengesAdapter(getContext(), challengeModels, this);
        challengesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        challengesRV.setAdapter(userCompletedChallengesAdapter);
        hitCompletedChallengeApi();
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        CommonFunctions.hideContinuousSB(tSnackbar);
        switch (apiType) {
            case ApiCodes.COMPLETED_CHALLENGES:
                challengeModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), ChallengeModel.class);
                userCompletedChallengesAdapter.addItems(challengeModels);
                break;
        }
    }

    @Override
    public void onFailure(String str, int apiType) {
        super.onFailure(str, apiType);
        tSnackbar.setText(str);
        CommonFunctions.hideContinuousSB(tSnackbar);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setUserVisibleHint(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setUserVisibleHint(false);
    }

    private void hitCompletedChallengeApi() {
        if (challengeModels.size() <= 0) {
            tSnackbar = CommonFunctions.getInstance().createLoadingSnackBarWithView(challengesRV);
            CommonFunctions.showContinuousSB(tSnackbar);
            CallWebService.getInstance(getContext(), false, ApiCodes.COMPLETED_CHALLENGES).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_COMPLETED_CHALLENGES, createJsonForGetChallenges(), this);
        }
    }

    private JSONObject createJsonForGetChallenges() {

        JSONObject jsonObject =CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
