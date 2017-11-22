package com.anhhoang.unsplashmodel;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

class PhotoCollection {
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DESC = "description";
    public static final String COL_PUBLISHED = "published_at";
    public static final String COL_UPDATED = "updated_at";
    public static final String COL_CURATED = "curated";
    public static final String COL_FEATURED = "featured";

    public static final String COL_COVER_PHOTO_ID = "photo_id";
    public static final String COL_USER_ID = "user_id";

    @SerializedName(COL_ID)
    private int id;
    @SerializedName(COL_TITLE)
    private String title;
    @SerializedName(COL_DESC)
    private String description;
    @SerializedName(COL_PUBLISHED)
    private Date publishedAt;
    @SerializedName(COL_UPDATED)
    private Date updatedAt;
    @SerializedName(COL_CURATED)
    private boolean isCurated;
    @SerializedName(COL_FEATURED)
    private boolean isFeatured;
    @SerializedName("cover_photo")
    private Photo photo;
    @SerializedName("user")
    private UserProfile user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isCurated() {
        return isCurated;
    }

    public void setCurated(boolean curated) {
        isCurated = curated;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }
}
