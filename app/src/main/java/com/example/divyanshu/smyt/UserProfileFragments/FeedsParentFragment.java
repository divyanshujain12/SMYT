package com.example.divyanshu.smyt.UserProfileFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments.UserAudioFeeds;
import com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments.UserFavoriteFeeds;
import com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments.UserVideosFragment;
import com.neopixl.pixlui.components.textview.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshuPC on 2/1/2017.
 */

public class FeedsParentFragment extends BaseFragment {

    @InjectView(R.id.fragmentContainerFL)
    FrameLayout fragmentContainerFL;
    @InjectView(R.id.videosTV)
    TextView videosTV;
    @InjectView(R.id.audiosTV)
    TextView audiosTV;
    @InjectView(R.id.favoriteTV)
    TextView favoriteTV;
    private FragmentManager fragmentManager;

    private static String CustomerID = "";

    public static FeedsParentFragment getInstance(String customerID) {
        FeedsParentFragment feedsParentFragment = new FeedsParentFragment();
        CustomerID = customerID;
        return feedsParentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds_parent, null);
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
        fragmentManager = getChildFragmentManager();
        updateFragment(UserVideosFragment.getInstance(CustomerID));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void updateFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        boolean fragShowing = fragmentManager.popBackStackImmediate(fragment.getClass().getName(), 0);

        if (!fragShowing) {
            if (!(fragment instanceof UserVideosFragment))
                fragmentTransaction.addToBackStack(fragment.getClass().getName());
            fragmentTransaction.replace(R.id.fragmentContainerFL, fragment);
            fragmentTransaction.commit();

        }
    }

    @OnClick({R.id.videosTV, R.id.audiosTV, R.id.favoriteTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.videosTV:
                updateFragment(UserVideosFragment.getInstance(CustomerID));
                break;
            case R.id.audiosTV:
                updateFragment(UserAudioFeeds.getInstance(CustomerID));
                break;
            case R.id.favoriteTV:
                updateFragment(UserFavoriteFeeds.getInstance(CustomerID));
                break;
        }
    }
}
