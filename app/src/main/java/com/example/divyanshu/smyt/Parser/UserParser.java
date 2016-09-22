package com.example.divyanshu.smyt.Parser;

import com.example.divyanshu.smyt.Models.UserModel;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by divyanshu.jain on 9/22/2016.
 */
public class UserParser {
    static String HOURS = "hr";
    static String MINUTES = "min";
    static String SECONDS = "sec";

    public static ArrayList<UserModel> getParsedUserData(JSONArray jsonArray) {

        ArrayList<UserModel> userModels = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                UserModel userModel = new UserModel();
                String customer_id = jsonObject.optString("customer_id", "");
                String email = jsonObject.optString("email", "");
                String first_name = jsonObject.optString("first_name", "");
                String last_name = jsonObject.optString("last_name", "");
                String profileimage = jsonObject.optString("profileimage", "");
                String username = jsonObject.optString("username", "");
                String gender = jsonObject.optString("gender", "");
                String date_of_birth = jsonObject.optString("date_of_birth", "");
                String phonenumber = jsonObject.optString("phonenumber", "");
                long active_before = jsonObject.optLong("active_before", 0);

                userModel.setCustomer_id(customer_id);
                userModel.setEmail(email);
                userModel.setFirst_name(first_name);
                userModel.setLast_name(last_name);
                userModel.setProfileimage(profileimage);
                userModel.setUsername(username);
                userModel.setGender(gender);
                userModel.setDate_of_birth(date_of_birth);
                userModel.setPhonenumber(phonenumber);
                userModel.setActive_before(getActiveTime(active_before));

                userModels.add(userModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return userModels;

    }

    private static String getActiveTime(long active_before) {
        long tempMillis = active_before;

        String timePostFix = HOURS;
        active_before = TimeUnit.MILLISECONDS.toHours(tempMillis);
        if (active_before <= 0) {
            active_before = TimeUnit.MILLISECONDS.toMinutes(tempMillis);
            timePostFix = MINUTES;
        }
        if (active_before <= 0) {
            active_before = TimeUnit.MILLISECONDS.toSeconds(tempMillis);
            timePostFix = SECONDS;
        }
        return String.valueOf(active_before) + " " + timePostFix;
    }
}
