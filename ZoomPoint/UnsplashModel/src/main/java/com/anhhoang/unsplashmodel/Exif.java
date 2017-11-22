package com.anhhoang.unsplashmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

public class Exif {
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
    private double exposureTime;
    @SerializedName(COL_APERTURE)
    private double aperture;
    @SerializedName(COL_FOCAL_LENGTH)
    private int focalLength;
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

    public double getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(double exposureTime) {
        this.exposureTime = exposureTime;
    }

    public double getAperture() {
        return aperture;
    }

    public void setAperture(double aperture) {
        this.aperture = aperture;
    }

    public int getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(int focalLength) {
        this.focalLength = focalLength;
    }

    public int getIso() {
        return iso;
    }

    public void setIso(int iso) {
        this.iso = iso;
    }
}
