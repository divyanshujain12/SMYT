package com.example.divyanshu.smyt.Models;

/**
 * Created by divyanshu on 9/24/2016.
 */

public class CommentModel {
    String customer_id;
    String first_name;
    String last_name;
    String profileimage;
    String customers_videos_comment_id;
    String comment;
    long edate;

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

    public String getCustomers_videos_comment_id() {
        return customers_videos_comment_id;
    }

    public void setCustomers_videos_comment_id(String customers_videos_comment_id) {
        this.customers_videos_comment_id = customers_videos_comment_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getEdate() {
        return edate;
    }

    public void setEdate(long edate) {
        this.edate = edate;
    }
}
