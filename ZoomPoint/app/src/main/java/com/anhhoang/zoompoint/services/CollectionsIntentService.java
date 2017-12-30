package com.anhhoang.zoompoint.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.anhhoang.database.ZoomPointContract;
import com.anhhoang.unsplashapi.UnsplashApi;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.utils.PhotoCollectionUtils;
import com.anhhoang.zoompoint.utils.UserUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionsIntentService extends IntentService {
    public static final String COLLECTIONS_LOAD_FAILED = "COLLECTIONS_LOAD_FAILED";
    public static final String COLLECTIONS_LOAD_SUCCESS = "COLLECTIONS_LOAD_SUCCESS";
    private static final String COLLECTION_QUERY = PhotoCollection.COL_FEATURED + "=1";
    private static final String CURRENT_PAGE = "CURRENT_PAGE";
    private static final int PAGE_SIZE = 15;

    private boolean forceLoad;
    private int currentPage;

    private Callback<List<PhotoCollection>> callback = new Callback<List<PhotoCollection>>() {
        @Override
        public void onResponse(Call<List<PhotoCollection>> call, Response<List<PhotoCollection>> response) {

            if (response.code() == HttpURLConnection.HTTP_OK) {
                List<PhotoCollection> collections = response.body();

                if (forceLoad) {
                    delete();
                }
                save(collections);

                sendBroadcast(new Intent(COLLECTIONS_LOAD_SUCCESS));
            } else {
                sendBroadcast(new Intent(COLLECTIONS_LOAD_FAILED));
            }
        }

        @Override
        public void onFailure(Call<List<PhotoCollection>> call, Throwable t) {
            sendBroadcast(new Intent(COLLECTIONS_LOAD_FAILED));
        }
    };

    public CollectionsIntentService() {
        super("CollectionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            currentPage = intent.getIntExtra(CURRENT_PAGE, 1);

            if (currentPage == 1) {
                forceLoad = true;
            }


            String token = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(getString(R.string.token_preference_key), null);
            UnsplashApi unsplashApi = UnsplashApi.getInstance(token);

            unsplashApi.getFeaturedCollections(currentPage, PAGE_SIZE)
                    .enqueue(callback);
        }
    }

    public static Intent getStartingIntent(Context context, int currentPage) {
        Intent intent = new Intent(context, CollectionsIntentService.class);
        intent.putExtra(CURRENT_PAGE, currentPage);

        return intent;
    }


    private void delete() {
        getContentResolver()
                .delete(ZoomPointContract.CollectionEntry.CONTENT_URI, COLLECTION_QUERY, null);
    }

    private void save(List<PhotoCollection> collections) {
        List<UserProfile> users = new ArrayList<>();
        for (PhotoCollection collection : collections) {
            users.add(collection.getUser());
        }


        ContentValues[] userContentValues = UserUtils.parseUsers(users);
        ContentValues[] collectionContentValues = PhotoCollectionUtils.parseCollections(collections);

        getContentResolver()
                .bulkInsert(ZoomPointContract.UserProfileEntry.CONTENT_URI, userContentValues);
        getContentResolver()
                .bulkInsert(ZoomPointContract.CollectionEntry.CONTENT_URI, collectionContentValues);
    }
}
