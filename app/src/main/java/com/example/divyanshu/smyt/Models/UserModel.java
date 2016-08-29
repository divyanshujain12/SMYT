package com.example.divyanshu.smyt.Models;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class UserModel {
    String id;
    String imageUrl;
    String name;
    String agoTime;
    int imageResource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgoTime() {
        return agoTime;
    }

    public void setAgoTime(String agoTime) {
        this.agoTime = agoTime;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
