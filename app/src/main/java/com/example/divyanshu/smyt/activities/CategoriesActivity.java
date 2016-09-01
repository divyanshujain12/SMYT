package com.example.divyanshu.smyt.activities;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.divyanshu.smyt.Adapters.CategoryRvAdapter;
import com.example.divyanshu.smyt.Adapters.CategoryUserRvAdapter;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.GlobalClasses.SingletonClass;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.GenerateDummyData;
import com.example.divyanshu.smyt.Utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class CategoriesActivity extends BaseActivity {

    @InjectView(R.id.userRV)
    RecyclerView userRV;
    @InjectView(R.id.categoryRV)
    RecyclerView categoryRV;
    @InjectView(R.id.categoriesCV)
    CardView categoriesCV;
    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    private CategoryUserRvAdapter categoryUserRvAdapter;
    private CategoryRvAdapter categoryRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {

        Utils.configureToolbarWithBackButton(this, toolbarView, getString(R.string.categories));
        GenerateDummyData.createUserAndCategoryData(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        // ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.ten_dp);
        //categoryRV.addItemDecoration(itemDecoration);
        categoryRV.setLayoutManager(gridLayoutManager);
        userRV.setLayoutManager(layoutManager);

        categoryRvAdapter = new CategoryRvAdapter(this, SingletonClass.getInstance().categoriesModels);
        categoryUserRvAdapter = new CategoryUserRvAdapter(this, SingletonClass.getInstance().userModels);

        categoryRV.setAdapter(categoryRvAdapter);
        userRV.setAdapter(categoryUserRvAdapter);
    }


}
