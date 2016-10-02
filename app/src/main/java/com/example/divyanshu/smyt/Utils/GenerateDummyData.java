package com.example.divyanshu.smyt.Utils;

import android.content.Context;

import com.example.divyanshu.smyt.GlobalClasses.SingletonClass;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.R;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class GenerateDummyData {

    public static void createUserAndCategoryData(Context context) {


        SingletonClass.getInstance().userModels.clear();

      /*  String[] categoryNames = context.getResources().getStringArray(R.array.genre_type);
        int[] categoriesImages = {R.drawable.genre_container_rock, R.drawable.genre_container_dj, R.drawable.genre_container_rap, R.drawable.genre_container_indie, R.drawable.genre_icon_electronics};
        for (int i = 0; i < 5; i++) {
            UserModel userModel = new UserModel();
            userModel.setName(context.getString(R.string.dummy_name));
            userModel.setImageResource(R.drawable.user);
            userModel.setAgoTime(context.getString(R.string.dummy_time_ago));
            userModel.setFollowers(context.getString(R.string.dummy_followers_count));
            userModel.setGender(context.getString(R.string.gender_male));
            userModel.setAge(context.getString(R.string.age));
            userModel.setAbout(context.getString(R.string.about_dummy));
            userModel.setprofileimage(context.getString(R.string.dummy_image));

           // SingletonClass.getInstance().categoriesModels.add(categoryModel);
            //SingletonClass.getInstance().userModels.add(userModel);
        }*/
    }
}
