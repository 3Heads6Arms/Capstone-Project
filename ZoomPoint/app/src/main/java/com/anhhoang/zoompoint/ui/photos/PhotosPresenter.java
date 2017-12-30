package com.anhhoang.zoompoint.ui.photos;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.utils.PhotoUtils;
import com.anhhoang.zoompoint.utils.PhotosCallType;

import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * Created by anh.hoang on 17.12.17.
 */

public class PhotosPresenter implements PhotosContract.Presenter {
    private static final int PAGE_SIZE = 15;
    private static final String COLLECTION_ID = "CollectionIdKey";
    private static final String QUERY = "QueryKey";
    private static final String CALL_TYPE = "CallTypeKey";

    private PhotosContract.View view;
    private int currentPage;
    private PhotosCallType photosCallType;
    private long collectionId;
    private String query;

    public PhotosPresenter(Bundle bundle) {
        currentPage = 1;
        this.collectionId = bundle.getLong(COLLECTION_ID, -1);
        this.photosCallType = (PhotosCallType) bundle.getSerializable(CALL_TYPE);

        if (collectionId < 0) {
            this.query = bundle.getString(QUERY);
        }

        if (collectionId < 0 && TextUtils.isEmpty(query)) {
            throw new IllegalArgumentException("Bundles of required arguments was not passed to fragment.");
        }
    }

    @Override
    public void attach(PhotosContract.View view) {
        this.view = view;

        if (view != null) {
            view.setPresenter(this);
        }
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void load() {
        if (view != null) {
            currentPage = 1;
            view.startLoading(collectionId, photosCallType, query, currentPage);
        }
    }

    @Override
    public void loadMore() {
        if (view != null) {
            currentPage++;
            view.startLoading(collectionId, photosCallType, query, currentPage);
        }
    }

    @Override
    public void loadFinished(Cursor cursor) {
        if (view != null) {
            view.toggleProgress(false);

            List<Photo> photos = PhotoUtils.parsePhotos(cursor);
            view.displayPhotos(photos);
            if (photos.size() <= 0) {
                view.showEmpty();
            } else {
                currentPage = photos.size() / PAGE_SIZE;
            }

        }
    }

    @Override
    public void onUserSelected(UserProfile userProfile) {
        if (view != null) {
            view.openUser(userProfile.getUsername(), userProfile.getName());
        }
    }

    @Override
    public void onPhotoSelected(String photoId, String photoType) {
        if (view != null) {
            view.openPhoto(photoId, photoType);
        }
    }

    private String getPhotoType() {
        String type = null;
        switch (photosCallType) {
            case PHOTOS:
            case USER_PHOTOS:
                type = query;
                break;
            case LIKED_PHOTOS:
                type = query + "liked";
                break;
            case COLLECTION_PHOTOS:
                type = String.valueOf(collectionId);
                break;
            case CURATED_PHOTOS:
                type = "curated";
                break;
            case SEARCH_PHOTOS:
                type = "search_photos";
                break;
        }
        return type;
    }

    @Override
    public String getSqlSelection() {
        String type = getPhotoType();

        checkNotNull(type);

        return Photo.COL_TYPE + "='" + type + "'";
    }
}
