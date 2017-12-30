package com.anhhoang.unsplashapi.RequestModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anh.hoang on 30.12.17.
 */

public class RequestAddToCollection {
    @SerializedName("collection_id")
    private long collectionId;
    @SerializedName("photo_id")
    private String photoId;

    public RequestAddToCollection(long collectionId, String photoId) {
        this.collectionId = collectionId;
        this.photoId = photoId;
    }
}
