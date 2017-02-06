package com.example.divyanshu.smyt.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.divyanshu.smyt.ServicesAndNotifications.NewChallengeNotificationService;
import com.example.divyanshu.smyt.ServicesAndNotifications.UpcomingRoundNotificationService;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
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
    private int selectedCategoryPos = -1;

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(gridLayoutManager);
        userRV.setLayoutManager(layoutManager);
        setUserAndCategoryAdapter();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_activity_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_user_profile:
                intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);

                break;
            case R.id.action_user_info:
                intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        selectedCategoryPos = position;
        checkAndHitJoinAPI(categoriesModels.get(position));
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
            case ApiCodes.JOIN_CATEGORY:
                categoryRvAdapter.setJoinStatus(selectedCategoryPos);
                categoriesModels.get(selectedCategoryPos).setJoin_status(1);
                checkAndHitJoinAPI(categoriesModels.get(selectedCategoryPos));
                break;
        }

    }

    private void checkAndHitJoinAPI(CategoryModel categoryModel) {
        if (categoryModel.getJoin_status() == 0) {
            CallWebService.getInstance(this, true, ApiCodes.JOIN_CATEGORY).hitJsonObjectRequestAPI(CallWebService.POST, API.JOIN_CATEGORY, createJsonForJoinCategory(categoryModel.getId()), this);
        } else {
            MySharedPereference.getInstance().setString(this, Constants.CATEGORY_ID, categoryModel.getId());
            MySharedPereference.getInstance().setString(this, Constants.CATEGORY_NAME, categoryModel.getcategory_name());
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constants.DATA, categoryModel);
            startActivity(intent);

        }
    }

    private void setUserAndCategoryAdapter() {
        categoryRvAdapter = new CategoryRvAdapter(this, categoriesModels, this);
        categoryUserRvAdapter = new CategoryUserRvAdapter(this, userModels, this);
        categoryRV.setAdapter(categoryRvAdapter);
        userRV.setAdapter(categoryUserRvAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startServices();
        MySharedPereference.getInstance().setString(this, Constants.CATEGORY_ID, "");
        if (categoriesModels == null || categoriesModels.isEmpty())
            CallWebService.getInstance(this, true, ApiCodes.CATEGORIES).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CATEGORIES, CommonFunctions.customerIdJsonObject(this), this);
    }

    private JSONObject createJsonForJoinCategory(String categoryID) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CATEGORY_ID, categoryID);
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void startServices() {
        Intent intent = new Intent(this, UpcomingRoundNotificationService.class);
        startService(intent);
        intent = new Intent(this, NewChallengeNotificationService.class);
        startService(intent);
    }

}
