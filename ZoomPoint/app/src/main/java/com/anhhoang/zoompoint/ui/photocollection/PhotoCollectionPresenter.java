package com.anhhoang.zoompoint.ui.photocollection;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

import com.anhhoang.unsplashapi.UnsplashApi;
import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.utils.PhotoUtils;
import com.anhhoang.zoompoint.utils.PhotosCallType;
import com.anhhoang.zoompoint.utils.UserUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anh.hoang on 17.12.17.
 */

public class PhotoCollectionPresenter implements PhotoCollectionContract.Presenter {

    private static final String COLLECTION_ID = "CollectionIdKey";
    private static final String QUERY = "QueryKey";
    private static final String CALL_TYPE = "CallTypeKey";

    private PhotoCollectionContract.View view;
    private UnsplashApi unsplashApi;

    private boolean isLoading;
    private boolean forceLoad;
    private int pageSize;
    private int currentPage;
    private PhotosCallType photosCallType;
    private long collectionId;
    private String query;
    private Callback<List<Photo>> photosCallback = new Callback<List<Photo>>() {
        @Override
        public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
            if (view != null) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Photo> photos = response.body();
                    loadFinished(photos);
                } else {
                    // Loadmore is not required when load from local db
                    view.removeLoadMore();
                    view.showError(R.string.unable_to_get_photo);
                    view.loadLocalPhotos(getSqlSelection());
                }
            }
        }

        @Override
        public void onFailure(Call<List<Photo>> call, Throwable t) {
            if (view != null) {
                view.showError(R.string.unable_to_get_photo);
                view.loadLocalPhotos(getSqlSelection());
            }
        }
    };

    public PhotoCollectionPresenter() {
        this(1);
        pageSize = 10;
    }

    public PhotoCollectionPresenter(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public void attach(PhotoCollectionContract.View view) {
        this.view = view;

        if (view != null) {
            view.setPresenter(this);
            String token = view.getToken();
            unsplashApi = UnsplashApi.getInstance(token);
        }
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void load(Bundle bundle) {
        this.collectionId = bundle.getLong(COLLECTION_ID, -1);

        if (collectionId < 0) {
            this.photosCallType = (PhotosCallType) bundle.getSerializable(CALL_TYPE);
            this.query = bundle.getString(QUERY);
        }

        if (collectionId < 0 && TextUtils.isEmpty(query)) {
            throw new IllegalArgumentException("Bundles of required arguments was not passed to fragment.");
        }

        if (view != null) {
            // Load is only called when is newly loaded or swiperefresh.
            // So clearing is required
            view.clearPhotos();
        }
        forceLoad = true;
        currentPage = 1;
        loadPhotos();
    }

    @Override
    public void loadMore() {
        if (!isLoading) {
            currentPage++;
            loadPhotos();
        }
    }

    @Override
    public void loadFinished(Cursor cursor) {
        if (view != null) {
            // on loadFinished with cursor means something went wrong with the server and had to load from local DB,
            // where all data will be loaded once from beginning
            // To prevent displaying same item twice or more, current list needs to be cleared.
            view.toggleProgress(false);
            view.clearPhotos();

            List<Photo> photos = PhotoUtils.parsePhotos(cursor);
            if (photos.size() <= 0) {
                // App is loading locally only when unable to get from server (empty server is not error)
                // Hence its always error when have to reach to local DB
                view.removeLoadMore();
                view.showEmpty(true, R.string.unable_to_get_photo);
            } else {
                view.displayPhotos(photos);
            }

        }
    }

    @Override
    public void loadFinished(List<Photo> photos) {
        if (view != null) {
            view.toggleProgress(false);
            view.displayPhotos(photos);
            if (photos.size() <= 0 && currentPage == 1) {
                // Concrete call from server and is empty is also considered error
                // to preven loadMore from firing
                view.removeLoadMore();
                view.showEmpty(false, 0);
            } else {
                // Remove & save only if there is success load from server
                if (forceLoad) {
                    view.removePhotos(getSqlSelection());
                    forceLoad = false;
                }
                save(photos);
            }
        }

        isLoading = false;
    }

    private void loadPhotos() {
        isLoading = true;
        if (view != null) {
            view.toggleProgress(true);
        }

        switch (photosCallType) {
            case PHOTOS:
                unsplashApi.getPhotos(currentPage, pageSize, query)
                        .enqueue(photosCallback);
                break;
            case CURATED_PHOTOS:
                unsplashApi.getCuratedPhotos(currentPage, pageSize)
                        .enqueue(photosCallback);
                break;
            case SEARCH_PHOTOS:
                unsplashApi.searchPhotos(query, currentPage, pageSize)
                        .enqueue(photosCallback);
                break;
            case COLLECTION_PHOTOS:
                unsplashApi.getCollectionPhotos(collectionId, currentPage, pageSize)
                        .enqueue(photosCallback);
                break;
            case USER_PHOTOS:
                unsplashApi.getUserPhotos(query, currentPage, pageSize)
                        .enqueue(photosCallback);
                break;
            case LIKED_PHOTOS:
                unsplashApi.getUserLikedPhotos(query, currentPage, pageSize)
                        .enqueue(photosCallback);
                break;
        }
    }

    private void save(List<Photo> photos) {
        List<UserProfile> users = new ArrayList<>();
        for (Photo photo : photos) {
            users.add(photo.getUser());
        }

        String type = null;

        switch (photosCallType) {
            case PHOTOS:
                type = query;
                break;
            case CURATED_PHOTOS:
                type = "curated";
                break;
        }

        ContentValues[] userContentValues = UserUtils.parseUsers(users);
        ContentValues[] photoContentValues = PhotoUtils.parsePhotos(photos, type);

        view.savePhotos(photoContentValues);
        view.saveUsers(userContentValues);
    }

    private String getSqlSelection() {
        return Photo.COL_TYPE + "='" + query + "'";
    }
}
