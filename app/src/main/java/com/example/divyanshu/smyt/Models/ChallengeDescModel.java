package com.example.divyanshu.smyt.Models;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 10/17/2016.
 */

public class ChallengeDescModel {
    String title;
    String total_round;
    String thumbnail;
    ArrayList<ChallengeModel> challenge_rounds;

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
}
