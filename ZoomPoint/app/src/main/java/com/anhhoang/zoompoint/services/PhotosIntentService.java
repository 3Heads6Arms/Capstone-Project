package com.anhhoang.zoompoint.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.anhhoang.database.ZoomPointContract;
import com.anhhoang.unsplashapi.RequestModel.RequestSearchPhoto;
import com.anhhoang.unsplashapi.UnsplashApi;
import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.utils.PhotoUtils;
import com.anhhoang.zoompoint.utils.PhotosCallType;
import com.anhhoang.zoompoint.utils.UserUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.util.Preconditions.checkNotNull;


public class PhotosIntentService extends IntentService {
    public static final String PHOTOS_LOAD_SUCCESS = "PHOTOS_LOAD_SUCCESS";
    public static final String PHOTOS_LOAD_FAILED = "PHOTOS_LOAD_FAILED";

    private static final String COLLECTION_ID = "CollectionIdKey";
    private static final String QUERY = "QueryKey";
    private static final String CALL_TYPE = "CallTypeKey";
    private static final String CURRENT_PAGE = "CurrentPageKey";


    private static final int PAGE_SIZE = 15;
    private Callback<List<Photo>> photosCallback = new Callback<List<Photo>>() {
        @Override
        public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
            if (response.code() == HttpURLConnection.HTTP_OK) {
                List<Photo> photos = response.body();

                if (forceLoad) {
                    deletePhotos();
                }
                save(photos);

                sendBroadcast(new Intent(PHOTOS_LOAD_SUCCESS));
            } else {
                sendBroadcast(new Intent(PHOTOS_LOAD_FAILED));
            }
        }

        @Override
        public void onFailure(Call<List<Photo>> call, Throwable t) {
            sendBroadcast(new Intent(PHOTOS_LOAD_FAILED));
        }
    };
    private Callback<RequestSearchPhoto> searchPhotoCallback = new Callback<RequestSearchPhoto>() {
        @Override
        public void onResponse(Call<RequestSearchPhoto> call, Response<RequestSearchPhoto> response) {
            if (response.code() == HttpURLConnection.HTTP_OK) {
                List<Photo> photos = response.body().getResults();
                deletePhotos();
                save(photos);

                sendBroadcast(new Intent(PHOTOS_LOAD_SUCCESS));
            } else {
                sendBroadcast(new Intent(PHOTOS_LOAD_FAILED));
            }
        }

        @Override
        public void onFailure(Call<RequestSearchPhoto> call, Throwable t) {
            sendBroadcast(new Intent(PHOTOS_LOAD_FAILED));
        }
    };
    private long collectionId;
    private PhotosCallType photosCallType;
    private String query;
    private int currentPage;
    private UnsplashApi unsplashApi;
    private boolean forceLoad;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public PhotosIntentService() {
        super(PhotosIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String token = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.token_preference_key), null);
        unsplashApi = UnsplashApi.getInstance(token);
        if (intent != null) {

            collectionId = intent.getLongExtra(COLLECTION_ID, -1);
            photosCallType = (PhotosCallType) intent.getSerializableExtra(CALL_TYPE);

            if (collectionId < 0) {
                query = intent.getStringExtra(QUERY);
            }

            if (collectionId < 0 && TextUtils.isEmpty(query)) {
                throw new IllegalArgumentException("Bundles of required arguments was not passed to fragment.");
            }

            currentPage = intent.getIntExtra(CURRENT_PAGE, 1);

            if (currentPage == 1) {
                forceLoad = true;
            }

            loadPhotos();
        }
    }

    public static Intent getStartingIntent(Context context, long collectionId, PhotosCallType callType, String query, int currentPage) {
        Intent intent = new Intent(context, PhotosIntentService.class);
        intent.putExtra(COLLECTION_ID, collectionId);
        intent.putExtra(CALL_TYPE, callType);
        intent.putExtra(QUERY, query);
        intent.putExtra(CURRENT_PAGE, currentPage);

        return intent;
    }

    private void loadPhotos() {
        switch (photosCallType) {
            case PHOTOS:
                unsplashApi.getPhotos(currentPage, PAGE_SIZE, query)
                        .enqueue(photosCallback);
                break;
            case CURATED_PHOTOS:
                unsplashApi.getCuratedPhotos(currentPage, PAGE_SIZE)
                        .enqueue(photosCallback);
                break;
            case SEARCH_PHOTOS:
                unsplashApi.searchPhotos(query, currentPage, PAGE_SIZE)
                        .enqueue(searchPhotoCallback);
                break;
            case COLLECTION_PHOTOS:
                unsplashApi.getCollectionPhotos(collectionId, currentPage, PAGE_SIZE)
                        .enqueue(photosCallback);
                break;
            case USER_PHOTOS:
                unsplashApi.getUserPhotos(query, currentPage, PAGE_SIZE)
                        .enqueue(photosCallback);
                break;
            case LIKED_PHOTOS:
                unsplashApi.getUserLikedPhotos(query, currentPage, PAGE_SIZE)
                        .enqueue(photosCallback);
                break;
        }
    }


    private void save(List<Photo> photos) {
        List<UserProfile> users = new ArrayList<>();
        for (Photo photo : photos) {
            users.add(photo.getUser());
        }

        String type = getPhotoType();

        ContentValues[] userContentValues = UserUtils.parseUsers(users);
        ContentValues[] photoContentValues = PhotoUtils.parsePhotos(photos, type);

        getContentResolver()
                .bulkInsert(ZoomPointContract.UserProfileEntry.CONTENT_URI, userContentValues);
        getContentResolver()
                .bulkInsert(ZoomPointContract.PhotoEntry.CONTENT_URI, photoContentValues);
    }

    private String getPhotoType() {
        String type = null;
        switch (photosCallType) {
            case PHOTOS:
            case USER_PHOTOS:
                type = query;
                break;
            case LIKED_PHOTOS:
                type = query + "liked";
                break;
            case COLLECTION_PHOTOS:
                type = String.valueOf(collectionId);
                break;
            case CURATED_PHOTOS:
                type = "curated";
                break;
            case SEARCH_PHOTOS:
                type = "search_photos";
                break;
        }
        return type;
    }

    private String getSqlSelection() {
        String type = getPhotoType();

        checkNotNull(type);

        return Photo.COL_TYPE + "='" + type + "'";
    }

    private void deletePhotos() {
        getContentResolver()
                .delete(ZoomPointContract.PhotoEntry.CONTENT_URI, getSqlSelection(), null);
    }
}
