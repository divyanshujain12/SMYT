package com.example.divyanshu.smyt.Models;

/**
 * Created by divyanshu on 9/12/16.
 */

public class UpcomingRoundInfoModel {
    private String customers_videos_id;
    private String challenge_id;
    private String edate;
    private int minutes;

    public UpcomingRoundInfoModel(){

    }
    public String getCustomers_videos_id() {
        return customers_videos_id;
    }

    public void setCustomers_videos_id(String customers_videos_id) {
        this.customers_videos_id = customers_videos_id;
    }

    public String getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(String challenge_id) {
        this.challenge_id = challenge_id;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
