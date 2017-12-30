package com.anhhoang.zoompoint.ui.home.homephotos;

import com.anhhoang.zoompoint.BaseMvpContract;
import com.anhhoang.zoompoint.utils.PhotosCallType;

/**
 * Created by anh.hoang on 18.12.17.
 */

public interface HomePhotosContract {
    interface View extends BaseMvpContract.View<Presenter> {
        void updatePhotoSource(PhotosCallType callType, String query);
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        void onPhotoSourceChanged(int positionType);
    }
}
