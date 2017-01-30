package com.example.divyanshu.smyt.Models;

import java.util.ArrayList;

/**
 * Created by divyanshu on 9/24/2016.
 */

public class VideoDetailModel {
    String customer_id;
    String first_name;
    String last_name;
    String profileimage;
    String customers_videos_id;
    String title;
    String thumbnail;
    String video_url;
    String genre;
    String share_status;
    int video_comment_count;
    String likes;
    int likestatus;
    String views;
    int  favourite_status;
    ArrayList<CommentModel> commentArray;
    ArrayList<LikeModel> likesArray;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
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

    public String getCustomers_videos_id() {
        return customers_videos_id;
    }

    public void setCustomers_videos_id(String customers_videos_id) {
        this.customers_videos_id = customers_videos_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getShare_status() {
        return share_status;
    }

    public void setShare_status(String share_status) {
        this.share_status = share_status;
    }

    public int getVideo_comment_count() {
        return video_comment_count;
    }

    public void setVideo_comment_count(int video_comment_count) {
        this.video_comment_count = video_comment_count;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public ArrayList<CommentModel> getCommentArray() {
        return commentArray;
    }

    public void setCommentArray(ArrayList<CommentModel> commentArray) {
        this.commentArray = commentArray;
    }

    public ArrayList<LikeModel> getLikesArray() {
        return likesArray;
    }

    public void setLikesArray(ArrayList<LikeModel> likesArray) {
        this.likesArray = likesArray;
    }
    public int getLikestatus() {
        return likestatus;
    }

    public void setLikestatus(int likestatus) {
        this.likestatus = likestatus;
    }
    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public int getFavourite_status() {
        return favourite_status;
    }

    public void setFavourite_status(int favourite_status) {
        this.favourite_status = favourite_status;
    }
}
