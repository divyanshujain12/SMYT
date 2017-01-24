package com.example.divyanshu.smyt.Models;

/**
 * Created by divyanshu on 20/11/16.
 */

public class ThumbnailGenerateModel {
    String video_url;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    String thumbnail;

    public String getCustomer_video_id() {
        return customer_video_id;
    }

    public void setCustomer_video_id(String customer_video_id) {
        this.customer_video_id = customer_video_id;
    }

    String customer_video_id;
}
