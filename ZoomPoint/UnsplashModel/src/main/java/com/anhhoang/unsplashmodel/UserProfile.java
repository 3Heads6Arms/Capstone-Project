package com.anhhoang.unsplashmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

public class UserProfile implements Parcelable {
    public static final String COL_ID = "id";
    public static final String COL_UPDATED = "updated_at";
    public static final String COL_USERNAME = "username";
    public static final String COL_NAME = "name";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_PORTFOLIO_URL = "portfolio_url";
    public static final String COL_BIO = "bio";
    public static final String COL_LOCATION = "location";
    public static final String COL_TWITTER ="twitter_username";

    @SerializedName(COL_ID)
    private String id;
    @SerializedName(COL_UPDATED)
    private Date updatedAt;
    @SerializedName(COL_USERNAME)
    private String username;
    @SerializedName(COL_NAME)
    private String name;
    @SerializedName(COL_FIRST_NAME)
    private String firstName;
    @SerializedName(COL_LAST_NAME)
    private String lastName;
    @SerializedName(COL_PORTFOLIO_URL)
    private String portfolioUrl;
    @SerializedName(COL_BIO)
    private String bio;
    @SerializedName(COL_LOCATION)
    private String location;
    @SerializedName("profile_image")
    private PhotoUrls profileImage;
    @SerializedName(COL_TWITTER)
    private String twitter;

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public PhotoUrls getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(PhotoUrls profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeString(this.username);
        dest.writeString(this.name);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.portfolioUrl);
        dest.writeString(this.bio);
        dest.writeString(this.location);
        dest.writeParcelable(this.profileImage, flags);
        dest.writeString(this.twitter);
    }

    public UserProfile() {
    }

    protected UserProfile(Parcel in) {
        this.id = in.readString();
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        this.username = in.readString();
        this.name = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.portfolioUrl = in.readString();
        this.bio = in.readString();
        this.location = in.readString();
        this.profileImage = in.readParcelable(PhotoUrls.class.getClassLoader());
        this.twitter = in.readString();
    }

    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel source) {
            return new UserProfile(source);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };
}
