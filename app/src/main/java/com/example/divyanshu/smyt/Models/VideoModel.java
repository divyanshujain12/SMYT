package com.example.divyanshu.smyt.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class VideoModel implements Parcelable {
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
    long edate;
    String views;
    int favourite_status;
    public VideoModel() {
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    protected VideoModel(Parcel in) {
        customer_id = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        profileimage = in.readString();
        customers_videos_id = in.readString();
        title = in.readString();
        thumbnail = in.readString();
        video_url = in.readString();
        genre = in.readString();
        share_status = in.readString();
        video_comment_count = in.readInt();
        edate = in.readLong();
        views = in.readString();
        favourite_status = in.readInt();
    }

    public static final Creator<VideoModel> CREATOR = new Creator<VideoModel>() {
        @Override
        public VideoModel createFromParcel(Parcel in) {
            return new VideoModel(in);
        }

        @Override
        public VideoModel[] newArray(int size) {
            return new VideoModel[size];
        }
    };

    public long getEdate() {
        return edate;
    }

    public void setEdate(long edate) {
        this.edate = edate;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customer_id);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(profileimage);
        dest.writeString(customers_videos_id);
        dest.writeString(title);
        dest.writeString(thumbnail);
        dest.writeString(video_url);
        dest.writeString(genre);
        dest.writeString(share_status);
        dest.writeInt(video_comment_count);
        dest.writeLong(edate);
        dest.writeString(views);
        dest.writeInt(favourite_status);
    }

    @Override
    public boolean equals(Object o) {
        return this.getCustomers_videos_id().equals(((VideoModel) o).getCustomers_videos_id());
    }
}
