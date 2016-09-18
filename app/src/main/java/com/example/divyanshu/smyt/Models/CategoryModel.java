package com.example.divyanshu.smyt.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class CategoryModel implements Parcelable {
    String category_name;
    String category_id;
    String thumbnail;
    int icon;
    String description;
    String usersCount;
    String status;
    String edate;

    public CategoryModel() {
    }

    protected CategoryModel(Parcel in) {
        category_name = in.readString();
        category_id = in.readString();
        thumbnail = in.readString();
        icon = in.readInt();
        description = in.readString();
        usersCount = in.readString();
        status = in.readString();
        edate = in.readString();
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(String usersCount) {
        this.usersCount = usersCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
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
        dest.writeInt(icon);
        dest.writeString(description);
        dest.writeString(usersCount);
        dest.writeString(status);
        dest.writeString(edate);
    }
}
