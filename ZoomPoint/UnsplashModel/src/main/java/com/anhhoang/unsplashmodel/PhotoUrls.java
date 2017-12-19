package com.anhhoang.unsplashmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

public class PhotoUrls {
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
}
