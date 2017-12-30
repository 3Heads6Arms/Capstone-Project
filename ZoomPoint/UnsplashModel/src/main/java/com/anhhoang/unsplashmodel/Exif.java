package com.anhhoang.unsplashmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

public class Exif implements android.os.Parcelable {
    public static final String COL_MAKE = "make";
    public static final String COL_MODEL = "model";
    public static final String COL_EXPOSURE_TIME = "exposure_time";
    public static final String COL_APERTURE = "aperture";
    public static final String COL_FOCAL_LENGTH = "focal_length";
    public static final String COL_ISO = "iso";

    @SerializedName(COL_MAKE)
    private String make;
    @SerializedName(COL_MODEL)
    private String model;
    @SerializedName(COL_EXPOSURE_TIME)
    private String exposureTime;
    @SerializedName(COL_APERTURE)
    private String aperture;
    @SerializedName(COL_FOCAL_LENGTH)
    private String focalLength;
    @SerializedName(COL_ISO)
    private int iso;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(String exposureTime) {
        this.exposureTime = exposureTime;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    public int getIso() {
        return iso;
    }

    public void setIso(int iso) {
        this.iso = iso;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.make);
        dest.writeString(this.model);
        dest.writeString(this.exposureTime);
        dest.writeString(this.aperture);
        dest.writeString(this.focalLength);
        dest.writeInt(this.iso);
    }

    public Exif() {
    }

    protected Exif(android.os.Parcel in) {
        this.make = in.readString();
        this.model = in.readString();
        this.exposureTime = in.readString();
        this.aperture = in.readString();
        this.focalLength = in.readString();
        this.iso = in.readInt();
    }

    public static final android.os.Parcelable.Creator<Exif> CREATOR = new android.os.Parcelable.Creator<Exif>() {
        @Override
        public Exif createFromParcel(android.os.Parcel source) {
            return new Exif(source);
        }

        @Override
        public Exif[] newArray(int size) {
            return new Exif[size];
        }
    };
}
