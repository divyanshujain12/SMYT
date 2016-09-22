package com.example.divyanshu.smyt.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class CategoryModel implements Parcelable {
    String category_id;
    String category_name;
    String description;
    String thumbnail;
    int usercount;
    int join_status;

    public CategoryModel() {
    }

    protected CategoryModel(Parcel in) {
        category_name = in.readString();
        category_id = in.readString();
        thumbnail = in.readString();
        description = in.readString();
        usercount = in.readInt();
        join_status = in.readInt();
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    public String getcategory_name() {
        return category_name;
    }

    public void setcategory_name(String name) {
        this.category_name = name;
    }

    public String getId() {
        return category_id;
    }

    public void setId(String id) {
        this.category_id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUsercount() {
        return usercount;
    }

    public void setUsercount(int usercount) {
        this.usercount = usercount;
    }

    public int getJoin_status() {
        return join_status;
    }

    public void setJoin_status(int join_status) {
        this.join_status = join_status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category_name);
        dest.writeString(category_id);
        dest.writeString(thumbnail);
        dest.writeString(description);
        dest.writeInt(usercount);
        dest.writeInt(join_status);
    }
}
