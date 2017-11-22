package com.anhhoang.unsplashmodel;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

class UserProfile {
    public static final String COL_ID = "id";
    public static final String COL_UPDATED = "updated_at";
    public static final String COL_USERNAME = "username";
    public static final String COL_NAME = "name";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_PORTFOLIO_URL = "portfolio_url";
    public static final String COL_BIO = "bio";
    public static final String COL_LOCATION = "location";

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
}
