package com.anhhoang.zoompoint.ui.userprofile;

import android.database.Cursor;

import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.BaseMvpContract;

import java.util.List;

/**
 * Created by anh.hoang on 25.12.17.
 */

public interface UserProfileContract {
    interface View extends BaseMvpContract.View<Presenter> {
        void displayProfilePicture(String url);

        void displayProfile(UserProfile profile);

        void displayCollections(List<PhotoCollection> collections);

        void toggleProgress(boolean show);

        void showEmptyCollection();

        void openCollection(int id, String collectionName);

        void startLoadingUser(String username);

        void startLoadingCollections(String username, int page);
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        void loadProfile();

        void loadFinished(Cursor data);

        void loadCollectionsFinished(Cursor data);

        void loadMore();

        void collectionSelected(PhotoCollection collection);

        String getSqlQuery();

        String getSqlQueryCollections();
    }
}
