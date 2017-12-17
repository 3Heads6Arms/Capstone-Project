package com.anhhoang.zoompoint.ui.photocollection;

import android.net.Uri;

import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.zoompoint.BaseMvpContract;
import com.anhhoang.zoompoint.utils.PhotosCallType;

import java.util.List;

/**
 * Created by anh.hoang on 17.12.17.
 */

public interface PhotoCollectionContract {
    interface View extends BaseMvpContract.View<Presenter> {
        void loadLocalPhotos(Uri uri);

        void updatePhotos(List<Photo> photos);

        void showError(String message);

        void showError(int idString);

        void toggleProgress(boolean show);

        String getToken();
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        void loadPhotos(PhotosCallType type, long collectionId, String query);

        void loadMore();
    }
}
