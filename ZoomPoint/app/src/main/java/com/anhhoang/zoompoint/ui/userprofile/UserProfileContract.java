package com.anhhoang.zoompoint.ui.userprofile;

import android.content.ContentValues;
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
        String getToken();

        void displayProfilePicture(String url);

        void displayProfile(UserProfile profile);

        void displayCollections(List<PhotoCollection> collections);

        void toggleProgress(boolean show);

        void showError(int idStringError);

        void showEmptyCollection(boolean hasError, int idStringError);

        void saveUserProfile(ContentValues userProfile);

        void loadUserFromLocal(String query);

        void removeLoadMore();
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        void loadProfile(String username);

        void loadFinished(Cursor data);

        void loadMore();
    }
}
