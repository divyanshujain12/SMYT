package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.view.View;

import com.example.divyanshu.smyt.Adapters.CategoryRvAdapter;
import com.example.divyanshu.smyt.Adapters.CategoryUserRvAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.GlobalClasses.SingletonClass;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.GenerateDummyData;
import com.example.divyanshu.smyt.Utils.ItemOffsetDecoration;
import com.example.divyanshu.smyt.Utils.UniversalParser;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

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
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.eight_dp);
        categoryRV.addItemDecoration(itemDecoration);
        categoryRV.setLayoutManager(gridLayoutManager);
        userRV.setLayoutManager(layoutManager);
        setCategoryAdapter();
        categoryUserRvAdapter = new CategoryUserRvAdapter(this, SingletonClass.getInstance().userModels, this);
        userRV.setAdapter(categoryUserRvAdapter);


    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        Intent intent = new Intent(this, OtherUserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case ApiCodes.CATEGORIES:
                SingletonClass.getInstance().categoriesModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONArray(Constants.DATA), CategoryModel.class);
                setCategoryAdapter();
                break;
        }

    }

    private void setCategoryAdapter() {
        categoryRvAdapter = new CategoryRvAdapter(this, SingletonClass.getInstance().getCategoriesModels());
        categoryRV.setAdapter(categoryRvAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SingletonClass.getInstance().getCategoriesModels() == null || SingletonClass.getInstance().getCategoriesModels().isEmpty())
            CallWebService.getInstance(this, true, ApiCodes.CATEGORIES).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CATEGORIES, null, this);
    }
}
