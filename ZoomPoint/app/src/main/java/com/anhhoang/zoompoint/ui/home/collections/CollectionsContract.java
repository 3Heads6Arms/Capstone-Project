package com.anhhoang.zoompoint.ui.home.collections;

import android.database.Cursor;

import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.zoompoint.BaseMvpContract;

import java.util.List;

/**
 * Created by anh.hoang on 21.12.17.
 */

public interface CollectionsContract {
    interface View extends BaseMvpContract.View<Presenter> {
//        String getToken();

        void toggleProgress(boolean show);

//        void showError(int idString);

        void showEmpty();

//        void removeLoadMore();

        void openCollection(long id, String collectionName);


//        void loadLocalCollections(String query);

//        void saveUsers(ContentValues[] users);

//        void saveCollections(ContentValues[] collections);

//        void removeCollections(String query);

        void displayCollections(List<PhotoCollection> collections);

//        void clearCollections();

        void startLoading(int currentPage);
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        void load();

        void loadMore();

        void loadFinished(Cursor cursor);

//        void loadFinished(List<PhotoCollection> collections);

        void collectionSelected(PhotoCollection collection);

        String getSqlQuery();
    }
}
