package com.anhhoang.zoompoint.ui.home.search;

import com.anhhoang.zoompoint.BaseMvpContract;
import com.anhhoang.zoompoint.utils.PhotosCallType;

/**
 * Created by anh.hoang on 24.12.17.
 */

public interface SearchContract {
    interface View extends BaseMvpContract.View<Presenter> {
        void updatePhotoSource(PhotosCallType type, String query);
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        void onSearch(String searchQuery);
    }
}
