package com.anhhoang.zoompoint.ui.home.homephotos;

import com.anhhoang.zoompoint.utils.PhotosCallType;

/**
 * Created by anh.hoang on 18.12.17.
 */

public class HomePhotosPresenter implements HomePhotosContract.Presenter {
    private HomePhotosContract.View view;

    @Override
    public void attach(HomePhotosContract.View view) {
        this.view = view;
        if(view != null){
            view.setPresenter(this);
        }
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void onPhotoSourceChanged(int positionType) {
        if (view != null) {
            switch (positionType) {
                case 0:
                    view.updatePhotoSource(PhotosCallType.PHOTOS, "latest");
                    break;
                case 1:
                    view.updatePhotoSource(PhotosCallType.PHOTOS, "popular");
                    break;
                case 2:
                    view.updatePhotoSource(PhotosCallType.CURATED_PHOTOS, "latest");
                    break;
                case 3:
                    view.updatePhotoSource(PhotosCallType.PHOTOS, "oldest");
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported Type");
            }
        }
    }
}
