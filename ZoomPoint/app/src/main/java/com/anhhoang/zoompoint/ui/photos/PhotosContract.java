package com.anhhoang.zoompoint.ui.photos;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.BaseMvpContract;

import java.util.List;

/**
 * Created by anh.hoang on 17.12.17.
 */

public interface PhotosContract {
    interface View extends BaseMvpContract.View<Presenter> {
        String getToken();

        void loadLocalPhotos(String query);

        void displayPhotos(List<Photo> photos);

        void clearPhotos();

        void toggleProgress(boolean show);

        void saveUsers(ContentValues[] users);

        void savePhotos(ContentValues[] photos);

        void removePhotos(String query);

        void showError(int idString);

        void showEmpty(boolean isError, int errorId);

        void removeLoadMore();

        void openUser(String username, String fullname);

        void openPhoto(String photoId);
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        void load(Bundle bundle);

        void loadMore();

        void loadFinished(Cursor cursor);

        void loadFinished(List<Photo> photos);

        void onUserSelected(UserProfile userProfile);

        void onPhotoSelected(String photoId);
    }
}
