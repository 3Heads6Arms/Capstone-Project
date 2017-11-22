package com.anhhoang.unsplashmodel;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Photo {

    @SerializedName("id")
    private String identifer;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    @SerializedName("width")
    private float width;
    @SerializedName("height")
    private float height;
    @SerializedName("color")
    private String color;
    @SerializedName("likes")
    private int likes;
    @SerializedName("liked_by_user")
    private boolean likedByUser;
    @SerializedName("description")
    private String description;
    @SerializedName("exif")
    private Exif exif;
    @SerializedName("location")
    private PhotoLocation location;
    @SerializedName("current_user_collections")
    private PhotoCollection collection;
    @SerializedName("urls")
    private PhotoUrls urls;
    @SerializedName("user")
    private UserProfile user;
}
