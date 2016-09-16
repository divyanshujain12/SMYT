package com.example.divyanshu.smyt.GlobalClasses;

import android.app.Activity;
import android.content.Context;

import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Utils.GenerateDummyData;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class SingletonClass {

    public ArrayList<CategoryModel> categoriesModels = new ArrayList<>();

    public ArrayList<UserModel> userModels = new ArrayList<>();

    private static SingletonClass ourInstance;

    private int selectedCategoryPos = 0;

    public static void initInstance(){
        if(ourInstance == null)
            ourInstance = new SingletonClass();
    }

    public static SingletonClass getInstance() {
        return ourInstance;
    }

    private SingletonClass() {
    }

    public int getSelectedCategoryPos() {
        return selectedCategoryPos;
    }

    public void setSelectedCategoryPos(int selectedCategoryPos) {
        this.selectedCategoryPos = selectedCategoryPos;
    }

    public CategoryModel getSelectedCategoryData(Context context) {
        if (categoriesModels != null && categoriesModels.size() > 0)
            return categoriesModels.get(selectedCategoryPos);
        else
            return null;
    }

    public ArrayList<CategoryModel> getCategoriesModels() {
        return categoriesModels;
    }

    public void setCategoriesModels(ArrayList<CategoryModel> categoriesModels) {
        this.categoriesModels = categoriesModels;
    }
}
