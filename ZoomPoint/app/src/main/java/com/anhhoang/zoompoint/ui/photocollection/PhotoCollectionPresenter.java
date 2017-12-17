package com.anhhoang.zoompoint.ui.photocollection;

import com.anhhoang.unsplashapi.UnsplashApi;
import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.zoompoint.utils.PhotosCallType;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anh.hoang on 17.12.17.
 */

public class PhotoCollectionPresenter implements PhotoCollectionContract.Presenter {
    private PhotoCollectionContract.View view;
    private UnsplashApi unsplashApi;

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
                    view.updatePhotos(response.body());
                } else {

                    // TODO: Set proper message
                    view.showError("Something went wrong");
                    // TODO: load local photos
                    // view.loadLocalPhotos();
                }

                view.toggleProgress(false);
            }
        }

        @Override
        public void onFailure(Call<List<Photo>> call, Throwable t) {
            if (view != null) {
                // TODO: Set proper message
                view.showError("Something went wrong");
                // TODO: load local photos
                //view.loadLocalPhotos();

                view.toggleProgress(false);
            }
        }
    };

    public PhotoCollectionPresenter() {
        this(1);
        pageSize = 15;
    }

    public PhotoCollectionPresenter(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public void attach(PhotoCollectionContract.View view) {
        this.view = view;

        if (view != null) {
            String token = view.getToken();
            unsplashApi = UnsplashApi.getInstance(token);
        }
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void loadPhotos(PhotosCallType type, long collectionId, String query) {
        photosCallType = type;
        this.collectionId = collectionId;
        this.query = query;

        loadPhotos(collectionId, query);
    }

    @Override
    public void loadMore() {
        currentPage++;
        loadPhotos(collectionId, query);
    }

    private void loadPhotos(long collectionId, String query) {
        view.toggleProgress(true);

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
}
