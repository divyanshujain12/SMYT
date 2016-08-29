package com.example.divyanshu.smyt;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.divyanshu.smyt.Adapters.CategoryRvAdapter;
import com.example.divyanshu.smyt.Adapters.CategoryUserRvAdapter;
import com.example.divyanshu.smyt.CustomViews.ToolbarWithBackButton;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Utils.ItemOffsetDecoration;

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
    @InjectView(R.id.backButtonTB)
    ToolbarWithBackButton backButtonTB;

    String[] categoryNames = {"Rock", "DJ", "Rap", "Indie", "Electronic"};
    int[] categoriesImages = {R.drawable.genre_container_rock, R.drawable.genre_container_dj, R.drawable.genre_container_rap, R.drawable.genre_container_indie, R.drawable.genre_icon_electronics};
    private CategoryUserRvAdapter categoryUserRvAdapter;
    private CategoryRvAdapter categoryRvAdapter;
    private ArrayList<CategoryModel> categoryModels;
    private ArrayList<UserModel> userModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {
        backButtonTB.InitToolbar(this, "Categories");
        categoryModels = new ArrayList<>();
        userModels = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setIcon(categoriesImages[i]);
            categoryModel.setName(categoryNames[i]);

            UserModel userModel = new UserModel();
            userModel.setName("Neanda");
            userModel.setImageResource(R.drawable.user);
            userModel.setAgoTime("22 min ago");

            categoryModels.add(categoryModel);
            userModels.add(userModel);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.ten_dp);
        categoryRV.addItemDecoration(itemDecoration);

        categoryRV.setLayoutManager(gridLayoutManager);
        userRV.setLayoutManager(layoutManager);

        categoryRvAdapter = new CategoryRvAdapter(this, categoryModels);
        categoryUserRvAdapter = new CategoryUserRvAdapter(this, userModels);

        categoryRV.setAdapter(categoryRvAdapter);
        userRV.setAdapter(categoryUserRvAdapter);
    }
}
