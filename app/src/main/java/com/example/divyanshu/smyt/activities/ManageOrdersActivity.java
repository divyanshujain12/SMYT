package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.OrdersFragments.InAppProductsFragment;
import com.example.divyanshu.smyt.OrdersFragments.OrderListFragment;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ManageOrdersActivity extends BaseActivity {

    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.tabs)
    CustomTabLayout tabs;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;
    @InjectView(R.id.activity_orders)
    LinearLayout activityOrders;

    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
        Utils.configureToolbarWithBackButton(this,toolbarView,getString(R.string.orders));
        configViewPager();

    }
    private void configViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(OrderListFragment.getInstance(), getString(R.string.your_orders));
        viewPagerAdapter.addFragment(InAppProductsFragment.getInstance(), getString(R.string.in_app_packages));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem());
        page.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
