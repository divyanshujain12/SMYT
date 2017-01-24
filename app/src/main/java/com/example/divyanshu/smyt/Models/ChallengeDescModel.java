package com.example.divyanshu.smyt.Models;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 10/17/2016.
 */

public class ChallengeDescModel {
    String title;
    String total_round;
    String thumbnail;
    String who_won;
    String who_won_name;
    ArrayList<ChallengeModel> challenge_rounds;// 0 - can accept, 1 - already accepted by him, 2 - decline by him, 3 - his video

    public int getCurrent_customer_video_status() {
        return current_customer_video_status;
    }

    public void setCurrent_customer_video_status(int current_customer_video_status) {
        this.current_customer_video_status = current_customer_video_status;
    }

    int current_customer_video_status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal_round() {
        return total_round;
    }

    public void setTotal_round(String total_round) {
        this.total_round = total_round;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ArrayList<ChallengeModel> getChallenge_rounds() {
        return challenge_rounds;
    }

    public void setChallenge_rounds(ArrayList<ChallengeModel> challenge_rounds) {
        this.challenge_rounds = challenge_rounds;
    }

    public String getWho_won() {
        return who_won;
    }

    public void setWho_won(String who_won) {
        this.who_won = who_won;
    }

    public String getWho_won_name() {
        return who_won_name;
    }

    public void setWho_won_name(String who_won_name) {
        this.who_won_name = who_won_name;
    }
}
