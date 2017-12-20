package com.anhhoang.unsplashmodel;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Photo {
    public static final String COL_IDENTIFIER = "id";
    public static final String COL_CREATED_AT = "created_at";
    public static final String COL_UPDATED_AT = "updated_at";
    public static final String COL_WIDTH = "width";
    public static final String COL_HEIGHT = "height";
    public static final String COL_COLOR = "color";
    public static final String COL_LIKES = "likes";
    public static final String COL_LIKED_BY_USER = "liked_by_user";
    public static final String COL_DESC = "description";

    public static final String COL_TYPE = "type";
    public static final String COL_COLLECTION_ID = "collection_id";
    public static final String COL_USER_ID = "user_id";

    @SerializedName(COL_IDENTIFIER)
    private String id;
    @SerializedName(COL_CREATED_AT)
    private Date createdAt;
    @SerializedName(COL_UPDATED_AT)
    private Date updatedAt;
    @SerializedName(COL_WIDTH)
    private int width;
    @SerializedName(COL_HEIGHT)
    private int height;
    @SerializedName(COL_COLOR)
    private String color;
    @SerializedName(COL_LIKES)
    private int likes;
    @SerializedName(COL_LIKED_BY_USER)
    private boolean likedByUser;
    @SerializedName(COL_DESC)
    private String description;
    @SerializedName("exif")
    private Exif exif;
    @SerializedName("location")
    private PhotoLocation location;
    @SerializedName("current_user_collections")
    private List<PhotoCollection> collections;
    @SerializedName("urls")
    private PhotoUrls urls;
    @SerializedName("user")
    private UserProfile user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Exif getExif() {
        return exif;
    }

    public void setExif(Exif exif) {
        this.exif = exif;
    }

    public PhotoLocation getLocation() {
        return location;
    }

    public void setLocation(PhotoLocation location) {
        this.location = location;
    }

    public List<PhotoCollection> getCollections() {
        return collections;
    }

    public void setCollections(List<PhotoCollection> collections) {
        this.collections = collections;
    }

    public PhotoUrls getUrls() {
        return urls;
    }

    public void setUrls(PhotoUrls urls) {
        this.urls = urls;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }


}
