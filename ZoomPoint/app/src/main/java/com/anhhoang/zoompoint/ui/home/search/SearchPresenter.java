package com.anhhoang.zoompoint.ui.home.search;

import com.anhhoang.zoompoint.utils.PhotosCallType;

/**
 * Created by anh.hoang on 24.12.17.
 */

public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View view;

    @Override
    public void attach(SearchContract.View view) {
        this.view = view;
        if (view != null) {
            this.view.setPresenter(this);
        }
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void onSearch(String searchQuery) {
        if (view != null) {
            this.view.updatePhotoSource(PhotosCallType.SEARCH_PHOTOS, searchQuery);
        }
    }
}
