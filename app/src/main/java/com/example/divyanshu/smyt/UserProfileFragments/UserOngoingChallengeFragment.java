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
import com.example.divyanshu.smyt.Adapters.UserOngoingChallengesAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserOngoingChallengeFragment extends BaseFragment {

    @InjectView(R.id.challengesRV)
    RecyclerView challengesRV;
    private UserOngoingChallengesAdapter userOngoingChallengesAdapter;
    private ArrayList<ChallengeModel> challengeModels = new ArrayList<>();
    private TSnackbar tSnackbar;

    public UserOngoingChallengeFragment() {
    }

    public static UserOngoingChallengeFragment newInstance() {
        UserOngoingChallengeFragment fragment = new UserOngoingChallengeFragment();
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
        View view = inflater.inflate(R.layout.fragment_ongoing_challenge, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitViews();
    }

    private void InitViews() {
        challengesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        userOngoingChallengesAdapter = new UserOngoingChallengesAdapter(getContext(), challengeModels, this);
        challengesRV.setAdapter(userOngoingChallengesAdapter);
        hitOnGoingChallengeApi();
    }



    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        CommonFunctions.hideContinuousSB(tSnackbar);
        switch (apiType) {
            case ApiCodes.ONGOING_CHALLENGES:
                challengeModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), ChallengeModel.class);
                userOngoingChallengesAdapter.addItems(challengeModels);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        setUserVisibleHint(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setUserVisibleHint(false);
    }

    private void hitOnGoingChallengeApi() {
        if (challengeModels.size() <= 0) {
            tSnackbar = CommonFunctions.getInstance().createLoadingSnackBarWithView(challengesRV);
            CommonFunctions.showContinuousSB(tSnackbar);
            CallWebService.getInstance(getContext(), false, ApiCodes.ONGOING_CHALLENGES).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_ONGOING_CHALLENGES, CommonFunctions.customerIdJsonObject(getContext()), this);
        }
    }
}
