package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Adapters.CategoryDescUsersRvAdapter;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.GlobalClasses.SingletonClass;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class CategoryDescriptionActivity extends BaseActivity {
    @InjectView(R.id.categoryIV)
    ImageView categoryIV;
    @InjectView(R.id.categoryNameTV)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_description);
        ButterKnife.inject(this);
        InitViews();
    }

    private void InitViews() {

        Utils.configureToolbarWithBackButton(this, toolbarView, getString(R.string.description));

        CategoryModel categoriesModel = SingletonClass.getInstance().getSelectedCategoryData(this);

        categoryIV.setImageResource(categoriesModel.getIcon());
        categoryNameTV.setText(categoriesModel.getcategory_name());
        categoryDescTV.setText(categoriesModel.getDescription());

        usersRV.setLayoutManager(new LinearLayoutManager(this));
        categoryDescUsersRvAdapter = new CategoryDescUsersRvAdapter(this,this);

        usersRV.setAdapter(categoryDescUsersRvAdapter);
    }

    @OnClick(R.id.joinTV)
    public void onClick() {

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        Intent intent = new Intent(this, OtherUserProfileActivity.class);
        startActivity(intent);
    }
}
