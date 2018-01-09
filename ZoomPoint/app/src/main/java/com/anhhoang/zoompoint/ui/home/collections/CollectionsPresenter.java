package com.anhhoang.zoompoint.ui.home.collections;

import android.database.Cursor;

import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.zoompoint.utils.PhotoCollectionUtils;

import java.util.List;

/**
 * Created by anh.hoang on 22.12.17.
 */

public class CollectionsPresenter implements CollectionsContract.Presenter {
    private static final String COLLECTION_QUERY = PhotoCollection.COL_FEATURED + "=1";
    private static final int PAGE_SIZE = 15;

    private int currentPage;
    private CollectionsContract.View view;

    public CollectionsPresenter() {
        currentPage = 1;
    }

    @Override
    public void attach(CollectionsContract.View view) {
        this.view = view;

        if (view != null) {
            view.setPresenter(this);
        }
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void load() {
        if (view != null) {
            currentPage = 1;
            view.startLoading(currentPage);
        }
    }

    @Override
    public void loadMore() {
        if (view != null) {
            currentPage++;
            view.startLoading(currentPage);
        }
    }

    @Override
    public void loadFinished(Cursor cursor) {
        if (view != null) {
            view.toggleProgress(false);

            List<PhotoCollection> collections = PhotoCollectionUtils.parseCollections(cursor);
            view.displayCollections(collections);

            if (collections.size() <= 0) {
                view.showEmpty();
            } else {
                currentPage = collections.size() / PAGE_SIZE;
            }

        }
    }


    @Override
    public void collectionSelected(PhotoCollection collection) {
        view.openCollection(collection.getId(), collection.getTitle());
    }

    @Override
    public String getSqlQuery() {
        return COLLECTION_QUERY;
    }
}
