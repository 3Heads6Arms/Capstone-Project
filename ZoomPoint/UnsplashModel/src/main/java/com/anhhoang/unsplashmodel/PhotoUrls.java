package com.anhhoang.unsplashmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

public class PhotoUrls implements Parcelable {
    // Photo sizes
    public static final String COL_RAW = "raw";
    public static final String COL_FULL = "full";
    public static final String COL_REGULAR = "regular";
    public static final String COL_THUMB = "thumb";

    // Profile Image sizes
    public static final String COL_MEDIUM = "medium";
    public static final String COL_LARGE = "large";

    // Common sizes
    public static final String COL_SMALL = "small";

    @SerializedName(COL_RAW)
    private String raw;
    @SerializedName(COL_FULL)
    private String full;
    @SerializedName(COL_REGULAR)
    private String regular;
    @SerializedName(COL_SMALL)
    private String small;
    @SerializedName(COL_THUMB)
    private String thumb;

    @SerializedName(COL_MEDIUM)
    private String medium;
    @SerializedName(COL_LARGE)
    private String large;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.raw);
        dest.writeString(this.full);
        dest.writeString(this.regular);
        dest.writeString(this.small);
        dest.writeString(this.thumb);
        dest.writeString(this.medium);
        dest.writeString(this.large);
    }

    public PhotoUrls() {
    }

    protected PhotoUrls(Parcel in) {
        this.raw = in.readString();
        this.full = in.readString();
        this.regular = in.readString();
        this.small = in.readString();
        this.thumb = in.readString();
        this.medium = in.readString();
        this.large = in.readString();
    }

    public static final Parcelable.Creator<PhotoUrls> CREATOR = new Parcelable.Creator<PhotoUrls>() {
        @Override
        public PhotoUrls createFromParcel(Parcel source) {
            return new PhotoUrls(source);
        }

        @Override
        public PhotoUrls[] newArray(int size) {
            return new PhotoUrls[size];
        }
    };
}
