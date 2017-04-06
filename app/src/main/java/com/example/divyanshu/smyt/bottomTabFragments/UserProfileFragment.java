package com.example.divyanshu.smyt.bottomTabFragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Adapters.UserOptionRvAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomToasts;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.activities.LoginActivity;
import com.example.divyanshu.smyt.activities.ManageOrdersActivity;
import com.example.divyanshu.smyt.activities.ProfileImageFullScreen;
import com.example.divyanshu.smyt.activities.UserSettingActivity;
import com.example.divyanshu.smyt.myProfileActivities.MyChallengesActivity;
import com.example.divyanshu.smyt.myProfileActivities.MyFeedsActivity;
import com.example.divyanshu.smyt.myProfileActivities.MyFollowingFollowersActivity;
import com.example.divyanshu.smyt.myProfileActivities.UserMusicActivity;
import com.example.divyanshu.smyt.myProfileActivities.UserNotificationActivity;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.divyanshu.smyt.Constants.ApiCodes.GET_USER_INFO;

/**
 * Created by divyanshuPC on 2/24/2017.
 */

public class UserProfileFragment extends BaseFragment {

    @InjectView(R.id.userIV)
    RoundedImageView userIV;
    @InjectView(R.id.userStatusTV)
    TextView userStatusTV;
    @InjectView(R.id.userOptionRV)
    RecyclerView userOptionRV;
    @InjectView(R.id.userNameTV)
    TextView userNameTV;

    UserModel userModel;
    ImageLoading imageLoading;

    public static UserProfileFragment getInstance() {
        UserProfileFragment feedsParentFragment = new UserProfileFragment();

        return feedsParentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_fragment, null);
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
        userOptionRV.setLayoutManager(new LinearLayoutManager(getContext()));
        userOptionRV.setAdapter(new UserOptionRvAdapter(getContext(), this));
        imageLoading = new ImageLoading(getContext());

    }

    private void hitGetUserInfoApi() {
        CallWebService.getInstance(getContext(), true, GET_USER_INFO).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_DETAIL, CommonFunctions.customerIdJsonObject(getContext()), this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case GET_USER_INFO:
                userModel = UniversalParser.getInstance().parseJsonObject(response.getJSONArray(Constants.DATA).getJSONObject(0), UserModel.class);
                updateUI();
                break;
        }
    }

    private void updateUI() {
        imageLoading.LoadImage(userModel.getProfileimage(), userIV, null);
        userNameTV.setText(userModel.getFirst_name());
        userStatusTV.setText(userModel.getTimeline_msg());
        View view = userOptionRV.getChildAt(4);
        TextView textView = (TextView) view.findViewById(R.id.optionNameTV);
        textView.setText("Followings/Followers (" + userModel.getFollowing() + "/" + userModel.getFollowers() + ")");
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (userModel == null)
                hitGetUserInfoApi();
        }

    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(getActivity(), MyFeedsActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(getActivity(), MyChallengesActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(getActivity(), UserNotificationActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(getActivity(), UserMusicActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(getActivity(), MyFollowingFollowersActivity.class);
                startActivity(intent);
                break;

            case 5:
                intent = new Intent(getActivity(), ManageOrdersActivity.class);
                startActivity(intent);
                break;
            case 6:
                CustomToasts.getInstance(getContext()).showErrorToast("Coming Soon...");
                break;
            case 7:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://smytex.com"));
                startActivity(browserIntent);
                break;
            case 8:
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://info@smytex.com"));
                startActivity(intent1);
                break;
            case 9:
                logout();
                break;

        }
    }

    private void logout() {
        MySharedPereference.getInstance().clearSharedPreference(getContext());
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_profile_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_user_setting:
                intent = new Intent(getActivity(), UserSettingActivity.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
        return true;
    }

    private BroadcastReceiver updateUserInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hitGetUserInfoApi();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(updateUserInfo, new IntentFilter(Constants.UPDATE_USER_INFO));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(updateUserInfo);
    }

    @OnClick(R.id.userIV)
    public void onClick() {
        Intent intent = new Intent(getActivity(), ProfileImageFullScreen.class);
        intent.putExtra(Constants.PROFILE_IMAGE, userModel.getProfileimage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            userIV.setTransitionName(Constants.PROFILE_IMAGE);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), userIV, Constants.PROFILE_IMAGE);
            startActivity(intent, activityOptionsCompat.toBundle());
        } else
            startActivity(intent);
    }
}
