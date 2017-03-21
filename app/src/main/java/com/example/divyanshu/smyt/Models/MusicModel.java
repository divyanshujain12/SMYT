package com.example.divyanshu.smyt.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by divyanshuPC on 3/21/2017.
 */

public class MusicModel implements Parcelable{
    private String fileName;
    private String filePath;
public MusicModel(){}

    protected MusicModel(Parcel in) {
        fileName = in.readString();
        filePath = in.readString();
    }

    public static final Creator<MusicModel> CREATOR = new Creator<MusicModel>() {
        @Override
        public MusicModel createFromParcel(Parcel in) {
            return new MusicModel(in);
        }

        @Override
        public MusicModel[] newArray(int size) {
            return new MusicModel[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fileName);
        parcel.writeString(filePath);
    }
}
