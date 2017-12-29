package com.anhhoang.zoompoint.ui.photo;

import android.content.ContentValues;
import android.database.Cursor;

import com.anhhoang.zoompoint.BaseMvpContract;

import java.util.List;

/**
 * Created by anh.hoang on 28.12.17.
 */

public interface PhotoContract {
    interface View extends BaseMvpContract.View {
        void toggleProgress(boolean show);

        void displayPhotoImage(String url);

        void displayUser(String name, String username, String profileImageUrl);

        void displayLikes(boolean likedByUser, int likes);
        void displayDescription(String description);

        void displayExif(List<Pair> items);

        void showError(int idErrorString);

        void showEmptyExif();

        void displayCollections(List<Pair> collections);

        String getToken();

        void openUser(String username, String fullname);

        void updatePhoto(ContentValues photo);

        String getMyUsername();
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        void load(String photoId);

        void loadFinished(Cursor data);

        void onUserSelected();

        void onLikeButtonSelected();

        void onDownloadSelected();

        void onSetWallpaperSelected();

        void onAddToCollection();

        void onCollectionSelected(int collectionId, String photoId);

        void loadMyCollections();
    }
}
