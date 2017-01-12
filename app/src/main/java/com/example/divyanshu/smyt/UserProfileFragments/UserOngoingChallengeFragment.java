package com.example.divyanshu.smyt.UserProfileFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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
import com.example.divyanshu.smyt.DialogActivities.OngoingChallengeDescriptionActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Interfaces.DeleteVideoInterface;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.divyanshu.smyt.Constants.ApiCodes.CHALLENGE_ACCEPT;
import static com.example.divyanshu.smyt.Constants.ApiCodes.CHALLENGE_REJECT;
import static com.example.divyanshu.smyt.Constants.ApiCodes.DELETE_CHALLENGE;

public class UserOngoingChallengeFragment extends BaseFragment {

    @InjectView(R.id.challengesRV)
    RecyclerView challengesRV;
    private UserOngoingChallengesAdapter userOngoingChallengesAdapter;
    private ArrayList<ChallengeModel> challengeModels = new ArrayList<>();
    private TSnackbar tSnackbar;
    int acceptRejectPos;

    public UserOngoingChallengeFragment() {
    }

    public static UserOngoingChallengeFragment newInstance(boolean newChallenge) {
        UserOngoingChallengeFragment fragment = new UserOngoingChallengeFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.NEW_CHALLENGE, newChallenge);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateAcceptRejectReceiver, new IntentFilter(Constants.USER_ONGOING_CHALLENGE_FRAGMENT));
        setUserVisibleHint(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateAcceptRejectReceiver);
        setUserVisibleHint(false);
    }

    @Override
    public void onResume() {
        if (challengeModels != null)
            userOngoingChallengesAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onClickItem(final int position, View view) {
        super.onClickItem(position, view);
        switch (view.getId()) {
            case R.id.acceptTV:
                acceptRejectPos = position;
                CallWebService.getInstance(getContext(), true, CHALLENGE_ACCEPT).hitJsonObjectRequestAPI(CallWebService.POST, API.ACCEPT_REJECT_CHALLENGE, createJsonForAcceptRejectChallenge(challengeModels.get(position).getChallenge_id(), "1"), this);
                break;
            case R.id.declineTV:
                acceptRejectPos = position;
                CallWebService.getInstance(getContext(), true, CHALLENGE_REJECT).hitJsonObjectRequestAPI(CallWebService.POST, API.ACCEPT_REJECT_CHALLENGE, createJsonForAcceptRejectChallenge(challengeModels.get(position).getChallenge_id(), "0"), this);
                break;
            case R.id.deleteVideoTV:
                CommonFunctions.getInstance().deleteChallenge(getContext(), challengeModels.get(position).getChallenge_id(), new DeleteVideoInterface() {
                    @Override
                    public void onDeleteVideo() {
                        userOngoingChallengesAdapter.removeItem(position);
                    }
                });


                break;
            default:
                Intent intent = new Intent(getActivity(), OngoingChallengeDescriptionActivity.class);
                intent.putExtra(Constants.CHALLENGE_ID, challengeModels.get(position).getChallenge_id());
                intent.putExtra(Constants.ACCEPT_STATUS, challengeModels.get(position).getCurrent_customer_video_status());
                startActivity(intent);
                break;
        }


    }

    private JSONObject createJsonForDeleteChallenge(String challengeID) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.CHALLENGE_ID, challengeID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void hitOnGoingChallengeApi() {
        tSnackbar = CommonFunctions.getInstance().createLoadingSnackBarWithView(challengesRV);
        CommonFunctions.showContinuousSB(tSnackbar);
        String apiUrl;
        if (getArguments().getBoolean(Constants.NEW_CHALLENGE))
            apiUrl = API.UPCOMING_NOT_ACCEPTED_CHALLENGES;
        else
            apiUrl = API.GET_ONGOING_CHALLENGES;
        CallWebService.getInstance(getContext(), false, ApiCodes.ONGOING_CHALLENGES).hitJsonObjectRequestAPI(CallWebService.POST, apiUrl, createJsonForGetChallenges(), this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        CommonFunctions.hideContinuousSB(tSnackbar);
        if (getUserVisibleHint()) {
            switch (apiType) {
                case ApiCodes.ONGOING_CHALLENGES:
                    challengeModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), ChallengeModel.class);
                    if (getUserVisibleHint())
                        setAdapter();
                    break;
                case CHALLENGE_ACCEPT:
                    if (getArguments().getBoolean(Constants.NEW_CHALLENGE))
                        userOngoingChallengesAdapter.removeItem(acceptRejectPos);
                    else
                        userOngoingChallengesAdapter.updateAcceptStatusIntoList(acceptRejectPos);
                    CommonFunctions.getInstance().showSuccessSnackBar(getActivity(), response.getString(Constants.MESSAGE));
                    break;
                case CHALLENGE_REJECT:
                    userOngoingChallengesAdapter.removeItem(acceptRejectPos);
                    CommonFunctions.getInstance().showSuccessSnackBar(getActivity(), response.getString(Constants.MESSAGE));
                    break;
            }
        }
    }

    private void setAdapter() {
        if (userOngoingChallengesAdapter != null)
            userOngoingChallengesAdapter.addItems(challengeModels);
    }

    @Override
    public void onFailure(String str, int apiType) {
        super.onFailure(str, apiType);
        tSnackbar.setText(str);
        CommonFunctions.hideContinuousSB(tSnackbar);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && challengeModels != null) {
            setAdapter();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    private JSONObject createJsonForGetChallenges() {

        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.CATEGORY_ID, MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID));
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForAcceptRejectChallenge(String challengeID, String s) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.CHALLENGE_ID, challengeID);
            jsonObject.put(Constants.STATUS, s);
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private BroadcastReceiver updateAcceptRejectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra(Constants.TYPE, 0);
            if (type == 1) {
                userOngoingChallengesAdapter.clear();
                hitOnGoingChallengeApi();
            } else {
                handleReceiver(intent);
            }
        }
    };

    private void handleReceiver(Intent intent) {
        int acceptStatus = intent.getIntExtra(Constants.ACCEPT_STATUS, -1);
        String challengeID = intent.getStringExtra(Constants.CHALLENGE_ID);
        ChallengeModel challengeModel = new ChallengeModel();
        challengeModel.setChallenge_id(challengeID);
        int pos = challengeModels.indexOf(challengeModel);

        if (getArguments().getBoolean(Constants.NEW_CHALLENGE))
            userOngoingChallengesAdapter.removeItem(pos);
        else if (acceptStatus == 2)
            userOngoingChallengesAdapter.removeItem(pos);
        else
            userOngoingChallengesAdapter.updateAcceptStatusIntoList(pos);
    }

}
