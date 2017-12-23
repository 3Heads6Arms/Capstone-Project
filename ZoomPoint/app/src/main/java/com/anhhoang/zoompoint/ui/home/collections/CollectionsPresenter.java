package com.anhhoang.zoompoint.ui.home.collections;

import android.content.ContentValues;
import android.database.Cursor;

import com.anhhoang.unsplashapi.UnsplashApi;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.utils.PhotoCollectionUtils;
import com.anhhoang.zoompoint.utils.UserUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anh.hoang on 22.12.17.
 */

public class CollectionsPresenter implements CollectionsContract.Presenter {
    private static final String COLLECTION_QUERY = PhotoCollection.COL_FEATURED + "=1";

    private UnsplashApi unsplashApi;

    private boolean isLoading;
    private boolean forceLoad;
    private int pageSize;
    private int currentPage;

    private CollectionsContract.View view;
    private Callback<List<PhotoCollection>> callback = new Callback<List<PhotoCollection>>() {
        @Override
        public void onResponse(Call<List<PhotoCollection>> call, Response<List<PhotoCollection>> response) {

            if (view != null) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<PhotoCollection> collections = response.body();
                    loadFinished(collections);
                } else {
                    // LoadMore is not required when load from local.
                    view.removeLoadMore();
                    view.showError(R.string.unable_to_get_photo);
                    view.loadLocalCollections(COLLECTION_QUERY);
                }
            }
        }

        @Override
        public void onFailure(Call<List<PhotoCollection>> call, Throwable t) {
            if (view != null) {
                view.showError(R.string.unable_to_get_photo);
                view.loadLocalCollections(COLLECTION_QUERY);
            }
        }
    };

    public CollectionsPresenter() {
        pageSize = 10;
        currentPage = 1;
    }

    @Override
    public void attach(CollectionsContract.View view) {
        this.view = view;

        if (view != null) {
            view.setPresenter(this);
            unsplashApi = UnsplashApi.getInstance(view.getToken());
        }
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void load() {
        if (view != null) {
            view.clearCollections();
        }

        forceLoad = true;
        currentPage = 1;
        loadCollections();
    }

    @Override
    public void loadMore() {
        if (!isLoading) {
            currentPage++;
            loadCollections();
        }
    }

    @Override
    public void loadFinished(Cursor cursor) {
        if (view != null) {
            // on loadFinished with cursor means something went wrong with the server and had to load from local DB,
            // where all data will be loaded once from beginning
            // To prevent displaying same item twice or more, current list needs to be cleared.
            view.clearCollections();

            List<PhotoCollection> collections = PhotoCollectionUtils.parseCollections(cursor);

            view.toggleProgress(false);
            if (collections.size() <= 0) {
                // App is loading locally only when unable to get from server (empty server is not error)
                // Hence its always error when have to reach to local DB
                view.removeLoadMore();
                view.showEmpty(true, R.string.unable_to_get_photo);
            } else {
                view.displayCollections(collections);
            }

        }
    }

    @Override
    public void loadFinished(List<PhotoCollection> collections) {
        if (view != null) {
            view.toggleProgress(false);
            view.displayCollections(collections);
            if (collections.size() <= 0 && currentPage == 1) {
                view.removeLoadMore();
                view.showEmpty(false, 0);
            } else {
                if (forceLoad) {
                    view.removeCollections(COLLECTION_QUERY);
                    forceLoad = false;
                }
                save(collections);
            }

        }

        isLoading = false;
    }

    private void save(List<PhotoCollection> collections) {
        List<UserProfile> users = new ArrayList<>();
        for (PhotoCollection collection : collections) {
            users.add(collection.getUser());
        }


        ContentValues[] userContentValues = UserUtils.parseUsers(users);
        ContentValues[] collectionContentValues = PhotoCollectionUtils.parseCollections(collections);

        view.saveCollections(collectionContentValues);
        view.saveUsers(userContentValues);
    }

    private void loadCollections() {
        isLoading = true;
        if (view != null) {
            view.toggleProgress(true);
        }

        unsplashApi.getFeaturedCollections(currentPage, pageSize)
                .enqueue(callback);
    }
}