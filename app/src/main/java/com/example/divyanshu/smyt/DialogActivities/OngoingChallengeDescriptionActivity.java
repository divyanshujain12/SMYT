package com.example.divyanshu.smyt.DialogActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Adapters.ChallengeRoundDescRvAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.ChallengeDescModel;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.divyanshu.smyt.Constants.ApiCodes.CHALLENGE_ACCEPT;
import static com.example.divyanshu.smyt.Constants.ApiCodes.CHALLENGE_REJECT;

/**
 * Created by divyanshu.jain on 9/2/2016.
 */
public class OngoingChallengeDescriptionActivity extends BaseActivity {
    @InjectView(R.id.challengeHeaderTV)
    TextView challengeHeaderTV;
    @InjectView(R.id.profileImage)
    ImageView profileImage;
    @InjectView(R.id.challengerNameTV)
    TextView challengerNameTV;
    @InjectView(R.id.totalRoundsTV)
    TextView totalRoundsTV;
    @InjectView(R.id.totalRoundLL)
    LinearLayout totalRoundLL;
    @InjectView(R.id.challengeTypeTV)
    TextView challengeTypeTV;
    @InjectView(R.id.challengeTypeLL)
    LinearLayout challengeTypeLL;
    @InjectView(R.id.challengesRoundRV)
    RecyclerView challengesRoundRV;
    @InjectView(R.id.acceptBT)
    Button acceptBT;
    @InjectView(R.id.declineBT)
    Button declineBT;
    String challengeID = "";
    int challengeAcceptStatus = -1;
    @InjectView(R.id.acceptAndDeclineLL)
    LinearLayout acceptAndDeclineLL;
    private ImageLoading imageLoading;
    private int userOneVoteCount = 0, userTwoVoteCount = 0;
    private boolean isChallengeCompleted = true;

    private ChallengeDescModel challengeDescModel;
    private ChallengeRoundDescRvAdapter challengeRoundDescRvAdapter;
    public static boolean isRoundPlayed = false;
    private String userOneName = "", userTwoName = "";
    private int customerNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ongoing_challange_desc_activity);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
        challengeID = getIntent().getStringExtra(Constants.CHALLENGE_ID);
        challengeAcceptStatus = getIntent().getIntExtra(Constants.ACCEPT_STATUS, -1);
        imageLoading = new ImageLoading(this, 5);
        challengeDescModel = new ChallengeDescModel();
        challengeRoundDescRvAdapter = new ChallengeRoundDescRvAdapter(this, challengeDescModel.getChallenge_rounds(), this, 0);
        challengesRoundRV.setLayoutManager(new LinearLayoutManager(this));
        challengesRoundRV.setAdapter(challengeRoundDescRvAdapter);
        if (challengeAcceptStatus != 0) {
            hideAcceptDeclineBar();
        }
        hitApiForGetChallengeInfo();
    }

    private void hitApiForGetChallengeInfo() {
        CallWebService.getInstance(this, true, ApiCodes.ONGOING_CHALLENGES).hitJsonObjectRequestAPI(CallWebService.POST, API.CHALLENGE_DESCRIPTION, createJsonForGetChallengeDesc(), this);
    }


    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case ApiCodes.ONGOING_CHALLENGES:
                challengeDescModel = UniversalParser.getInstance().parseJsonObject(response.getJSONObject(Constants.DATA), ChallengeDescModel.class);
                updateUI();
                break;
            case CHALLENGE_ACCEPT:
                hideAcceptDeclineBar();
                sendAcceptRejectBroadcastToFragment(1, challengeID);
                CommonFunctions.getInstance().showSuccessSnackBar(this, response.getString(Constants.MESSAGE));
                break;
            case CHALLENGE_REJECT:
                sendAcceptRejectBroadcastToFragment(2, challengeID);
                CommonFunctions.getInstance().showSuccessSnackBar(this, response.getString(Constants.MESSAGE));
                finish();
                break;
        }
    }

    @Override
    public void onFailure(String str, int apiType) {
        super.onFailure(str, apiType);
        CommonFunctions.getInstance().showErrorSnackBar(this, str);
    }


    private void updateUI() {
        String customerId = MySharedPereference.getInstance().getString(this, Constants.CUSTOM_ID);
        if (customerId.equals(challengeDescModel.getCustomer_id()))
            customerNumber = 1;
        else
            customerNumber = 2;

        if (profileImage != null) {
            imageLoading.LoadImage(challengeDescModel.getThumbnail(), profileImage, null);
            challengeHeaderTV.setText(challengeDescModel.getTitle());
            totalRoundsTV.setText(challengeDescModel.getTotal_round());
            challengeRoundDescRvAdapter.addItems(challengeDescModel.getChallenge_rounds());
            challengeRoundDescRvAdapter.setCustomerNumber(customerNumber);
            setUpCurrentStatus();
        }
    }

    private void setUpCurrentStatus() {

        if (challengeDescModel.getWho_won() != null && !challengeDescModel.getWho_won().equals("0")) {
            challengeTypeTV.setText(challengeDescModel.getWho_won_name() + " Win!");
        } else {
            switch (challengeDescModel.getCurrent_customer_video_status()) {
                case 0:
                    challengeTypeTV.setText(R.string.pending);
                    break;
                case 1:
                    challengeTypeTV.setText(R.string.accepted);
                    break;
                case 2:
                    challengeTypeTV.setText(R.string.rejected);
                    break;
                case 3:
                    if (challengeDescModel.getStatus().equals(getString(R.string.active)))
                        challengeTypeTV.setText(R.string.accepted);
                    else
                        challengeTypeTV.setText(R.string.pending);
                    break;
            }
        }
    }

    @OnClick({R.id.acceptBT, R.id.declineBT})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.acceptBT:
                CallWebService.getInstance(this, true, CHALLENGE_ACCEPT).hitJsonObjectRequestAPI(CallWebService.POST, API.ACCEPT_REJECT_CHALLENGE, createJsonForAcceptRejectChallenge(challengeID, "1"), this);
                break;
            case R.id.declineBT:
                CallWebService.getInstance(this, true, CHALLENGE_REJECT).hitJsonObjectRequestAPI(CallWebService.POST, API.ACCEPT_REJECT_CHALLENGE, createJsonForAcceptRejectChallenge(challengeID, "0"), this);
                break;
        }
    }

    private void sendAcceptRejectBroadcastToFragment(int status, String challengeID) {
        Intent intent = new Intent();
        intent.setAction(Constants.USER_ONGOING_CHALLENGE_FRAGMENT);
        intent.putExtra(Constants.ACCEPT_STATUS, status);
        intent.putExtra(Constants.CHALLENGE_ID, challengeID);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    private JSONObject createJsonForGetChallengeDesc() {

        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CHALLENGE_ID, challengeID);
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private JSONObject createJsonForAcceptRejectChallenge(String challengeID, String s) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CHALLENGE_ID, challengeID);
            jsonObject.put(Constants.STATUS, s);
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void hideAcceptDeclineBar() {
        acceptAndDeclineLL.setVisibility(View.GONE);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        Intent intent = new Intent(this, UploadedBattleRoundDescActivity.class);
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, challengeDescModel.getChallenge_rounds().get(position).getCustomers_videos_id());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        if (isRoundPlayed) {
            hitApiForGetChallengeInfo();
        } else if (challengeDescModel != null)
            challengeRoundDescRvAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

}


