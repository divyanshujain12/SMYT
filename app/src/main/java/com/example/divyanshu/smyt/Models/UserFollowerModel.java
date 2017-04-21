package com.example.divyanshu.smyt.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by divyanshu on 3/28/2017.
 */

public class UserFollowerModel implements Parcelable{

    String customer_id;
    String email;
    String first_name;
    String last_name;
    String profileimage;
    String username;
    String gender;
    String date_of_birth;
    String phonenumber;
    String timeline_msg;
    int total_wins;
    String followers;
    String following;
    String active_before;
    String followStatus;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getTimeline_msg() {
        return timeline_msg;
    }

    public void setTimeline_msg(String timeline_msg) {
        this.timeline_msg = timeline_msg;
    }

    public int getTotal_wins() {
        return total_wins;
    }

    public void setTotal_wins(int total_wins) {
        this.total_wins = total_wins;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getActive_before() {
        return active_before;
    }

    public void setActive_before(String active_before) {
        this.active_before = active_before;
    }

    public String getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(String followStatus) {
        this.followStatus = followStatus;
    }

    public UserFollowerModel(){}

    protected UserFollowerModel(Parcel in) {
        customer_id = in.readString();
        email = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        profileimage = in.readString();
        username = in.readString();
        gender = in.readString();
        date_of_birth = in.readString();
        phonenumber = in.readString();
        timeline_msg = in.readString();
        total_wins = in.readInt();
        followers = in.readString();
        following = in.readString();
        active_before = in.readString();
        followStatus = in.readString();
    }

    public static final Creator<UserFollowerModel> CREATOR = new Creator<UserFollowerModel>() {
        @Override
        public UserFollowerModel createFromParcel(Parcel in) {
            return new UserFollowerModel(in);
        }

        @Override
        public UserFollowerModel[] newArray(int size) {
            return new UserFollowerModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customer_id);
        dest.writeString(email);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(profileimage);
        dest.writeString(username);
        dest.writeString(gender);
        dest.writeString(date_of_birth);
        dest.writeString(phonenumber);
        dest.writeString(timeline_msg);
        dest.writeInt(total_wins);
        dest.writeString(followers);
        dest.writeString(following);
        dest.writeString(active_before);
        dest.writeString(followStatus);
    }
}
