package com.anhhoang.unsplashmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

public class PhotoLocation implements Parcelable {
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

    public static class Position implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.latitude);
            dest.writeDouble(this.longitude);
        }

        public Position() {
        }

        protected Position(Parcel in) {
            this.latitude = in.readDouble();
            this.longitude = in.readDouble();
        }

        public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
            @Override
            public Position createFromParcel(Parcel source) {
                return new Position(source);
            }

            @Override
            public Position[] newArray(int size) {
                return new Position[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeParcelable(this.position, flags);
    }

    public PhotoLocation() {
    }

    protected PhotoLocation(Parcel in) {
        this.city = in.readString();
        this.country = in.readString();
        this.position = in.readParcelable(Position.class.getClassLoader());
    }

    public static final Parcelable.Creator<PhotoLocation> CREATOR = new Parcelable.Creator<PhotoLocation>() {
        @Override
        public PhotoLocation createFromParcel(Parcel source) {
            return new PhotoLocation(source);
        }

        @Override
        public PhotoLocation[] newArray(int size) {
            return new PhotoLocation[size];
        }
    };
}
