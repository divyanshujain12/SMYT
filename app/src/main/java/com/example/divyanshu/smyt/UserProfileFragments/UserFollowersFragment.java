package com.example.divyanshu.smyt.UserProfileFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Adapters.UserFollowerAdapter;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Activities.OtherUserProfileActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */
public class UserFollowersFragment extends BaseFragment {

    @InjectView(R.id.followersRV)
    RecyclerView followersRV;
    private UserFollowerAdapter userFollowerAdapter;

    public static UserFollowersFragment getInstance() {
        UserFollowersFragment userFollowersFragment = new UserFollowersFragment();
        return userFollowersFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_follower_fragment, null);
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

        userFollowerAdapter = new UserFollowerAdapter(getActivity(), this);
        followersRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        followersRV.setAdapter(userFollowerAdapter);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        Intent intent = new Intent(getActivity(), OtherUserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
