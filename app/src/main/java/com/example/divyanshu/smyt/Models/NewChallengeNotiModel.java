package com.example.divyanshu.smyt.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by divyanshuPC on 2/7/2017.
 */

public class NewChallengeNotiModel implements Parcelable {
    String category_id;
    String category_name;
    String description;
    String thumbnail;
    int usercount;
    int join_status;
    String challenge_id;

   public NewChallengeNotiModel() {
    }

    protected NewChallengeNotiModel(Parcel in) {
        category_id = in.readString();
        category_name = in.readString();
        description = in.readString();
        thumbnail = in.readString();
        usercount = in.readInt();
        join_status = in.readInt();
        challenge_id = in.readString();
    }

    public static final Creator<NewChallengeNotiModel> CREATOR = new Creator<NewChallengeNotiModel>() {
        @Override
        public NewChallengeNotiModel createFromParcel(Parcel in) {
            return new NewChallengeNotiModel(in);
        }

        @Override
        public NewChallengeNotiModel[] newArray(int size) {
            return new NewChallengeNotiModel[size];
        }
    };

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getUsercount() {
        return usercount;
    }

    public void setUsercount(int usercount) {
        this.usercount = usercount;
    }

    public int getJoin_status() {
        return join_status;
    }

    public void setJoin_status(int join_status) {
        this.join_status = join_status;
    }

    public String getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(String challenge_id) {
        this.challenge_id = challenge_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category_id);
        dest.writeString(category_name);
        dest.writeString(description);
        dest.writeString(thumbnail);
        dest.writeInt(usercount);
        dest.writeInt(join_status);
        dest.writeString(challenge_id);
    }
}
