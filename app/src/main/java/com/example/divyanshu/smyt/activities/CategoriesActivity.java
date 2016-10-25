package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.divyanshu.smyt.Adapters.CategoryRvAdapter;
import com.example.divyanshu.smyt.Adapters.CategoryUserRvAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Parser.UserParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ItemOffsetDecoration;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private ArrayList<CategoryModel> categoriesModels = new ArrayList<>();
    private ArrayList<UserModel> userModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {

        Utils.configureToolbarWithOutBackButton(this, toolbarView, getString(R.string.categories));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(gridLayoutManager);
        userRV.setLayoutManager(layoutManager);
        setUserAndCategoryAdapter();
        categoryUserRvAdapter = new CategoryUserRvAdapter(this, userModels, this);
        userRV.setAdapter(categoryUserRvAdapter);


    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
      /*  Intent intent = new Intent(this, OtherUserProfileActivity.class);
        intent.putExtra(Constants.USER_DATA, userModels.get(position));
        startActivity(intent);*/
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case ApiCodes.CATEGORIES:
                JSONObject data = response.getJSONObject(Constants.DATA);
                categoriesModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(data.optJSONArray(Constants.CATEGORY), CategoryModel.class);
                userModels = UserParser.getParsedUserData(data.optJSONArray(Constants.CUSTOMERS));
                setUserAndCategoryAdapter();
                break;
        }

    }

    private void setUserAndCategoryAdapter() {
        categoryRvAdapter = new CategoryRvAdapter(this, categoriesModels);
        categoryUserRvAdapter = new CategoryUserRvAdapter(this, userModels, this);
        categoryRV.setAdapter(categoryRvAdapter);
        userRV.setAdapter(categoryUserRvAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (categoriesModels == null || categoriesModels.isEmpty())
            CallWebService.getInstance(this, true, ApiCodes.CATEGORIES).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CATEGORIES, CommonFunctions.customerIdJsonObject(this), this);
    }
}
