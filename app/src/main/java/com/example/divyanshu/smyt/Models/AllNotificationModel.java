package com.example.divyanshu.smyt.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by divyanshu.jain on 3/8/2017.
 */

public class AllNotificationModel implements Parcelable {
    private String customer_id;
    private String customer_id1;
    private String customers_videos_id;
    private String title;
    private String genre;
    private String share_status;
    private String round_date;
    private String total_round;
    private String round_no;
    private String status;
    private String challenge_id;
    private long edate;

    public AllNotificationModel() {
    }

    protected AllNotificationModel(Parcel in) {
        customer_id = in.readString();
        customer_id1 = in.readString();
        customers_videos_id = in.readString();
        title = in.readString();
        genre = in.readString();
        share_status = in.readString();
        round_date = in.readString();
        total_round = in.readString();
        round_no = in.readString();
        status = in.readString();
        challenge_id = in.readString();
        edate = in.readLong();
    }

    public static final Creator<AllNotificationModel> CREATOR = new Creator<AllNotificationModel>() {
        @Override
        public AllNotificationModel createFromParcel(Parcel in) {
            return new AllNotificationModel(in);
        }

        @Override
        public AllNotificationModel[] newArray(int size) {
            return new AllNotificationModel[size];
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

    public String getRound_date() {
        return round_date;
    }

    public void setRound_date(String round_date) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(customer_id);
        parcel.writeString(customer_id1);
        parcel.writeString(customers_videos_id);
        parcel.writeString(title);
        parcel.writeString(genre);
        parcel.writeString(share_status);
        parcel.writeString(round_date);
        parcel.writeString(total_round);
        parcel.writeString(round_no);
        parcel.writeString(status);
        parcel.writeString(challenge_id);
        parcel.writeLong(edate);
    }
}
