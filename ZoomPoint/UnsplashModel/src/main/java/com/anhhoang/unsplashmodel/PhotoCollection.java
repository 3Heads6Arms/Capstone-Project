package com.anhhoang.unsplashmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

public class PhotoCollection implements Parcelable {
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DESC = "description";
    public static final String COL_PUBLISHED = "published_at";
    public static final String COL_UPDATED = "updated_at";
    public static final String COL_CURATED = "curated";
    public static final String COL_FEATURED = "featured";
    public static final String COL_TYPE = "type";

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
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeLong(this.publishedAt != null ? this.publishedAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeByte(this.isCurated ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFeatured ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.photo, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.type);
    }

    public PhotoCollection() {
    }

    protected PhotoCollection(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        long tmpPublishedAt = in.readLong();
        this.publishedAt = tmpPublishedAt == -1 ? null : new Date(tmpPublishedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        this.isCurated = in.readByte() != 0;
        this.isFeatured = in.readByte() != 0;
        this.photo = in.readParcelable(Photo.class.getClassLoader());
        this.user = in.readParcelable(UserProfile.class.getClassLoader());
        this.type = in.readString();
    }

    public static final Parcelable.Creator<PhotoCollection> CREATOR = new Parcelable.Creator<PhotoCollection>() {
        @Override
        public PhotoCollection createFromParcel(Parcel source) {
            return new PhotoCollection(source);
        }

        @Override
        public PhotoCollection[] newArray(int size) {
            return new PhotoCollection[size];
        }
    };
}
