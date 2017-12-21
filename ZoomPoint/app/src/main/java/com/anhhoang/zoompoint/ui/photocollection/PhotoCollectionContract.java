package com.anhhoang.zoompoint.ui.photocollection;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.zoompoint.BaseMvpContract;

import java.util.List;

/**
 * Created by anh.hoang on 17.12.17.
 */

public interface PhotoCollectionContract {
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

    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        void load(Bundle bundle);

        void loadMore();

        void loadFinished(Cursor cursor);

        void loadFinished(List<Photo> photos);
    }
}
