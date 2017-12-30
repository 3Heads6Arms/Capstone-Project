package com.anhhoang.zoompoint.ui.photos;

import android.database.Cursor;

import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.BaseMvpContract;
import com.anhhoang.zoompoint.utils.PhotosCallType;

import java.util.List;

/**
 * Created by anh.hoang on 17.12.17.
 */

public interface PhotosContract {
    interface View extends BaseMvpContract.View<Presenter> {
        void displayPhotos(List<Photo> photos);

        void toggleProgress(boolean show);

        void showEmpty();

        void openUser(String username, String fullname);

        void openPhoto(String photoId);

        void startLoading(long collectionId, PhotosCallType callType, String query, int currentPage);
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        void load();

        void loadMore();

        void loadFinished(Cursor cursor);

        void onUserSelected(UserProfile userProfile);

        void onPhotoSelected(String photoId);

        String getSqlSelection();
    }
}
