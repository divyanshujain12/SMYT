package com.example.divyanshu.smyt.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 10/26/2016.
 */

public class ChallengeVideoDescModel implements Parcelable {
    String customer_id;
    String customer_id1;
    String first_name;
    String last_name;
    String profileimage;
    String first_name1;
    String last_name1;
    String profileimage1;
    String customers_videos_id;
    String title;
    String thumbnail;
    String video_url;
    String thumbnail1;
    String video_url1;
    String genre;
    String share_status;
    long round_date;
    String total_round;
    String round_no;
    int video_comment_count;
    String status;
    String challenge_id;
    long edate;
    String complete_status;
    String vote;
    String vote1;
    int current_customer_video_status;
    int vote_status;
    int vote_status_customer_id;
    String views;
    String is_copy;
    int favourite_status;

    ArrayList<CommentModel> commentArray;
    ArrayList<LikeModel> likesArray;

    public ChallengeVideoDescModel() {
    }

    protected ChallengeVideoDescModel(Parcel in) {
        customer_id = in.readString();
        customer_id1 = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        profileimage = in.readString();
        first_name1 = in.readString();
        last_name1 = in.readString();
        profileimage1 = in.readString();
        customers_videos_id = in.readString();
        title = in.readString();
        thumbnail = in.readString();
        video_url = in.readString();
        thumbnail1 = in.readString();
        video_url1 = in.readString();
        genre = in.readString();
        share_status = in.readString();
        round_date = in.readLong();
        total_round = in.readString();
        round_no = in.readString();
        video_comment_count = in.readInt();
        status = in.readString();
        challenge_id = in.readString();
        edate = in.readLong();
        complete_status = in.readString();
        vote = in.readString();
        vote1 = in.readString();
        current_customer_video_status = in.readInt();
        vote_status = in.readInt();
        vote_status_customer_id = in.readInt();
        views = in.readString();
        is_copy = in.readString();
        favourite_status = in.readInt();
    }

    public static final Creator<ChallengeVideoDescModel> CREATOR = new Creator<ChallengeVideoDescModel>() {
        @Override
        public ChallengeVideoDescModel createFromParcel(Parcel in) {
            return new ChallengeVideoDescModel(in);
        }

        @Override
        public ChallengeVideoDescModel[] newArray(int size) {
            return new ChallengeVideoDescModel[size];
        }
    };

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_id1() {
        return customer_id1;
    }

    public void setCustomer_id1(String customer_id1) {
        this.customer_id1 = customer_id1;
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

    public String getFirst_name1() {
        return first_name1;
    }

    public void setFirst_name1(String first_name1) {
        this.first_name1 = first_name1;
    }

    public String getLast_name1() {
        return last_name1;
    }

    public void setLast_name1(String last_name1) {
        this.last_name1 = last_name1;
    }

    public String getProfileimage1() {
        return profileimage1;
    }

    public void setProfileimage1(String profileimage1) {
        this.profileimage1 = profileimage1;
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

    public String getThumbnail1() {
        return thumbnail1;
    }

    public void setThumbnail1(String thumbnail1) {
        this.thumbnail1 = thumbnail1;
    }

    public String getVideo_url1() {
        return video_url1;
    }

    public void setVideo_url1(String video_url1) {
        this.video_url1 = video_url1;
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

    public long getRound_date() {
        return round_date;
    }

    public void setRound_date(long round_date) {
        this.round_date = round_date;
    }

    public String getTotal_round() {
        return total_round;
    }

    public void setTotal_round(String total_round) {
        this.total_round = total_round;
    }

    public String getRound_no() {
        return round_no;
    }

    public void setRound_no(String round_no) {
        this.round_no = round_no;
    }

    public int getVideo_comment_count() {
        return video_comment_count;
    }

    public void setVideo_comment_count(int video_comment_count) {
        this.video_comment_count = video_comment_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(String challenge_id) {
        this.challenge_id = challenge_id;
    }

    public long getEdate() {
        return edate;
    }

    public void setEdate(long edate) {
        this.edate = edate;
    }

    public String getComplete_status() {
        return complete_status;
    }

    public void setComplete_status(String complete_status) {
        this.complete_status = complete_status;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getVote1() {
        return vote1;
    }

    public void setVote1(String vote1) {
        this.vote1 = vote1;
    }

    public int getCurrent_customer_video_status() {
        return current_customer_video_status;
    }

    public void setCurrent_customer_video_status(int current_customer_video_status) {
        this.current_customer_video_status = current_customer_video_status;
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

    public int getVote_status() {
        return vote_status;
    }

    public void setVote_status(int vote_status) {
        this.vote_status = vote_status;
    }

    public int getVote_status_customer_id() {
        return vote_status_customer_id;
    }

    public void setVote_status_customer_id(int vote_status_customer_id) {
        this.vote_status_customer_id = vote_status_customer_id;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getIs_copy() {
        return is_copy;
    }

    public void setIs_copy(String is_copy) {
        this.is_copy = is_copy;
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
        dest.writeString(customer_id1);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(profileimage);
        dest.writeString(first_name1);
        dest.writeString(last_name1);
        dest.writeString(profileimage1);
        dest.writeString(customers_videos_id);
        dest.writeString(title);
        dest.writeString(thumbnail);
        dest.writeString(video_url);
        dest.writeString(thumbnail1);
        dest.writeString(video_url1);
        dest.writeString(genre);
        dest.writeString(share_status);
        dest.writeLong(round_date);
        dest.writeString(total_round);
        dest.writeString(round_no);
        dest.writeInt(video_comment_count);
        dest.writeString(status);
        dest.writeString(challenge_id);
        dest.writeLong(edate);
        dest.writeString(complete_status);
        dest.writeString(vote);
        dest.writeString(vote1);
        dest.writeInt(current_customer_video_status);
        dest.writeInt(vote_status);
        dest.writeInt(vote_status_customer_id);
        dest.writeString(views);
        dest.writeString(is_copy);
        dest.writeInt(favourite_status);
    }
}
