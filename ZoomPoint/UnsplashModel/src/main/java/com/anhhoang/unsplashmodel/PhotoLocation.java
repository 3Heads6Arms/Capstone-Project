package com.anhhoang.unsplashmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

public class PhotoLocation {
    public static final String COL_CITY = "city";
    public static final String COL_COUNTRY = "country";
    public static final String COL_LAT = "latitude";
    public static final String COL_LON = "longitude";

    @SerializedName(COL_CITY)
    private String city;
    @SerializedName(COL_COUNTRY)
    private String country;
    @SerializedName("position")
    private Position position;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public static class Position {
        @SerializedName(COL_LAT)
        private double latitude;
        @SerializedName(COL_LON)
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}
