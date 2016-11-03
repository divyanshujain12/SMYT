package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Adapters.CategoryDescUsersRvAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class CategoryDescriptionActivity extends BaseActivity {
    @InjectView(R.id.categoryIV)
    ImageView categoryIV;
    @InjectView(R.id.genreNameTV)
    TextView categoryNameTV;
    @InjectView(R.id.joinTV)
    TextView joinTV;
    @InjectView(R.id.categoryDescTV)
    TextView categoryDescTV;
    @InjectView(R.id.usersRV)
    RecyclerView usersRV;
    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    private CategoryDescUsersRvAdapter categoryDescUsersRvAdapter;
    private CategoryModel categoriesModel;
    private ImageLoading imageLoading;
    private ArrayList<UserModel> userModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_description);
        ButterKnife.inject(this);
        InitViews();
    }

    private void InitViews() {

        Utils.configureToolbarWithBackButton(this, toolbarView, getString(R.string.description));

        categoriesModel = getIntent().getExtras().getParcelable(Constants.DATA);
        imageLoading = new ImageLoading(this);
        imageLoading.LoadImage(categoriesModel.getThumbnail(), categoryIV, null);
        categoryNameTV.setText(categoriesModel.getcategory_name());
        categoryDescTV.setText(categoriesModel.getDescription());

        usersRV.setLayoutManager(new LinearLayoutManager(this));
        categoryDescUsersRvAdapter = new CategoryDescUsersRvAdapter(this, this);
        usersRV.setAdapter(categoryDescUsersRvAdapter);

        CallWebService.getInstance(this, true, ApiCodes.CATEGORY_DESC_USERS).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CATEGORY_USER, createJsonForGetUser(), this);
    }


    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);

        switch (apiType) {
            case ApiCodes.CATEGORY_DESC_USERS:
                userModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), UserModel.class);
                categoryDescUsersRvAdapter.addData(userModels);
                break;
            case ApiCodes.JOIN_CATEGORY:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(Constants.DATA, categoriesModel);
                MySharedPereference.getInstance().setString(this, Constants.CATEGORY_ID, categoriesModel.getId());
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }

    @OnClick(R.id.joinTV)
    public void onClick() {
        CallWebService.getInstance(this, true, ApiCodes.JOIN_CATEGORY).hitJsonObjectRequestAPI(CallWebService.POST, API.JOIN_CATEGORY, createJsonForJoinCategory(), this);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        Intent intent = new Intent(this, OtherUserProfileActivity.class);
        intent.putExtra(Constants.USER_DATA, userModels.get(position));
        startActivity(intent);
    }

    private JSONObject createJsonForJoinCategory() {
        //String customerID = MySharedPereference.getInstance().getString(this, Constants.CUSTOMER_ID);
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CATEGORY_ID, categoriesModel.getId());
           // jsonObject.put(Constants.CUSTOMER_ID, customerID == "" ? "1" : customerID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForGetUser() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CATEGORY_ID, categoriesModel.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_activity, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_user_profile:
                Intent intent = new Intent(this, UserProfileActivity.class);
                //intent.putExtra(Constants.CATEGORY_ID, categoryModel.getId());
                startActivity(intent);
                return true;
        }
        return true;
    }
}
