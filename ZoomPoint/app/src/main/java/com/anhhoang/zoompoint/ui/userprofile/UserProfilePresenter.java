package com.anhhoang.zoompoint.ui.userprofile;

import android.database.Cursor;

import com.anhhoang.unsplashapi.UnsplashApi;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.utils.UserUtils;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anh.hoang on 25.12.17.
 */

public class UserProfilePresenter implements UserProfileContract.Presenter {
    private UserProfileContract.View view;
    private UnsplashApi unsplashApi;
    private String username;
    private boolean isLoading;
    private int currentPage;
    private int pageSize;

    private Callback<UserProfile> userCallback = new Callback<UserProfile>() {
        @Override
        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
            if (view != null) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    loadFinished(response.body(), false);
                } else {
                    view.showEmptyCollection(true, R.string.no_collection_found);
                    view.showError(R.string.unable_to_connect);
                    view.loadUserFromLocal(getSqlSelectionQuery());
                }
            }
        }

        @Override
        public void onFailure(Call<UserProfile> call, Throwable t) {
            if (view != null) {
                view.showEmptyCollection(true, R.string.no_collection_found);
                view.showError(R.string.unable_to_connect);
                view.loadUserFromLocal(getSqlSelectionQuery());
            }
        }
    };
    private Callback<List<PhotoCollection>> collectionsCallback = new Callback<List<PhotoCollection>>() {
        @Override
        public void onResponse(Call<List<PhotoCollection>> call, Response<List<PhotoCollection>> response) {
            if (view != null) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    loadFinished(response.body());
                } else {
                    view.showEmptyCollection(true, R.string.no_collection_found);
                    view.showError(R.string.unable_to_connect);
                    view.loadUserFromLocal(getSqlSelectionQuery());
                }
            }
        }

        @Override
        public void onFailure(Call<List<PhotoCollection>> call, Throwable t) {
            if (view != null) {
                view.showEmptyCollection(true, R.string.no_collection_found);
                view.showError(R.string.unable_to_connect);
            }
        }
    };

    public UserProfilePresenter() {
        pageSize = 20;
        currentPage = 1;
    }

    @Override
    public void attach(UserProfileContract.View view) {
        this.view = view;
        if (view != null) {
            view.setPresenter(this);
            unsplashApi = UnsplashApi.getInstance(view.getToken());
        }
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void loadProfile(String username) {
        this.username = username;

        if (view != null) {
            view.toggleProgress(true);
            unsplashApi.getUserProfile(username)
                    .enqueue(userCallback);
        }
    }

    @Override
    public void loadFinished(Cursor data) {
        if (view != null) {
            UserProfile userProfile = UserUtils.parse(data);
            loadFinished(userProfile, true);
        }
    }

    @Override
    public void loadMore() {
        if (!isLoading) {
            currentPage++;
            loadCollections();
        }
    }

    private void loadCollections() {
        if (view != null) {
            isLoading = true;
            view.toggleProgress(true);
            unsplashApi.getUserCollections(username, currentPage, pageSize)
                    .enqueue(collectionsCallback);
        }
    }

    private void loadFinished(UserProfile userProfile, boolean hasError) {
        if (userProfile.getProfileImage() != null) {
            view.displayProfilePicture(userProfile.getProfileImage().getLarge());
        }
        view.displayProfile(userProfile);

        if (hasError) {
            view.removeLoadMore();
            view.toggleProgress(false);
        } else {
            view.saveUserProfile(UserUtils.parseUser(userProfile));
            loadCollections();
        }
    }

    private void loadFinished(List<PhotoCollection> collections) {
        if (view != null) {
            view.toggleProgress(false);
            if (collections.size() <= 0) {
                view.showEmptyCollection(false, 0);
            } else {
                view.displayCollections(collections);
            }

            if (collections.size() < pageSize) {
                view.removeLoadMore();
            }
        }

        isLoading = false;
    }

    private String getSqlSelectionQuery() {
        return UserProfile.COL_USERNAME + "='" + username + "'";
    }
}
