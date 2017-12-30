package com.anhhoang.zoompoint.ui.userprofile;

import android.database.Cursor;

import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.utils.PhotoCollectionUtils;
import com.anhhoang.zoompoint.utils.UserUtils;

import java.util.List;

/**
 * Created by anh.hoang on 25.12.17.
 */

public class UserProfilePresenter implements UserProfileContract.Presenter {
    public static final int PAGE_SIZE = 15;

    private UserProfileContract.View view;
    private String username;
    private int currentPage;

    public UserProfilePresenter(String username) {
        this.username = username;
        currentPage = 1;
    }

    @Override
    public void attach(UserProfileContract.View view) {
        this.view = view;
        if (view != null) {
            view.setPresenter(this);
        }
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void loadProfile() {
        if (view != null) {
            view.startLoadingUser(username);
            view.startLoadingCollections(username, currentPage);
        }
    }

    @Override
    public void loadFinished(Cursor data) {
        if (view != null) {
            view.toggleProgress(false);

            UserProfile userProfile = UserUtils.parse(data);

            if (userProfile.getProfileImage() != null) {
                view.displayProfilePicture(userProfile.getProfileImage().getLarge());
            }
            view.displayProfile(userProfile);
        }
    }

    @Override
    public void loadCollectionsFinished(Cursor data) {
        if (view != null) {
            view.toggleProgress(false);
            List<PhotoCollection> collections = PhotoCollectionUtils.parseCollections(data);

            view.displayCollections(collections);
            if (collections.size() <= 0) {
                view.showEmptyCollection();
            } else {
                currentPage = collections.size() / PAGE_SIZE;
            }
        }
    }

    @Override
    public void loadMore() {
        if (view != null) {
            currentPage++;
            view.startLoadingCollections(username, currentPage);
        }
    }

    @Override
    public void collectionSelected(PhotoCollection collection) {
        if (view != null) {
            view.openCollection(collection.getId(), collection.getTitle());
        }
    }

    @Override
    public String getSqlQuery() {
        return UserProfile.COL_USERNAME + "='" + username + "'";
    }

    @Override
    public String getSqlQueryCollections() {
        return PhotoCollection.COL_TYPE + "='MY_COLLECTIONS'";
    }
}
