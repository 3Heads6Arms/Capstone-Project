package com.anhhoang.zoompoint.ui.home.collections;

import android.content.ContentValues;
import android.database.Cursor;

import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.zoompoint.BaseMvpContract;

import java.util.List;

/**
 * Created by anh.hoang on 21.12.17.
 */

public interface CollectionsContract {
    interface View<Presenter> extends BaseMvpContract.View {
        String getToken();

        void toggleProgress(boolean show);

        void showError(int idString);

        void showEmpty(boolean hasError, int idString);


        void loadLocalCollections();

        void saveUsers(ContentValues[] users);

        void saveCollections(ContentValues[] collections);

        void displayCollections(List<PhotoCollection> collections);
    }

    interface Presenter<View> extends BaseMvpContract.Presenter {
        void load();

        void loadMore();

        void loadFinished(Cursor cursor);

        void loadFinished(List<PhotoCollection> collections);
    }
}
