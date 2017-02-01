package com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.R;

import butterknife.ButterKnife;

/**
 * Created by divyanshuPC on 2/1/2017.
 */

public class UserAudioFeeds extends BaseFragment {
    private static String customerID = "";

    public static UserAudioFeeds getInstance(String customerID) {
        UserAudioFeeds userFavoriteFeeds = new UserAudioFeeds();
        UserAudioFeeds.customerID = customerID;
        return userFavoriteFeeds;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_videos_fragment, null);
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

    }
}
