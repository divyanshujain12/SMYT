package com.example.divyanshu.smyt.Models;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class CategoryModel {
    String category_name;
    String category_id;
    String thumbnail;
    int icon;
    String description;
    String usersCount;
    String status;
    String edate;


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
}
