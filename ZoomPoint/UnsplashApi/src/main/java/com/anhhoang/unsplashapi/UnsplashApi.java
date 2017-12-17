package com.anhhoang.unsplashapi;

import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.UserProfile;

import java.io.IOException;
import java.util.List;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anh.hoang on 21.11.17.
 */

public class UnsplashApi {
    private static final String API_URL = "https://api.unsplash.com/";
    private static UnsplashApi INSTANCE;

    public static UnsplashApi getInstance(String token) {
        if (INSTANCE == null) {
            INSTANCE = new UnsplashApi(token);
        }

        return INSTANCE;
    }

    private UnsplashApiService unsplashApiService;

    private UnsplashApi(String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthorizationInterceptor(token))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        unsplashApiService = retrofit.create(UnsplashApiService.class);
    }

    public Call<List<Photo>> getPhotos(int page, int perPage, String orderBy) {
        return unsplashApiService.getPhotos(page, perPage, orderBy);
    }

    public Call<List<Photo>> getCuratedPhotos(int page, int perPage) {
        return unsplashApiService.getCuratedPhotos(page, perPage);
    }

    public Call<Photo> getPhoto(String photoId) {
        return unsplashApiService.getPhoto(photoId);
    }

    public Call<Photo> getRandomPhoto(boolean featured) {
        return unsplashApiService.getRandomPhoto(featured);
    }

    public Call<Photo> likePhoto(String photoId) {
        return unsplashApiService.likePhoto(photoId);
    }

    public Call<Photo> unlikePhoto(String photoId) {
        return unsplashApiService.unlikePhoto(photoId);
    }

    public Call<List<Photo>> searchPhotos(String searchTerms, int page, int perPage) {
        return unsplashApiService.searchPhotos(searchTerms, page, perPage);
    }

    public Call<List<PhotoCollection>> getFeaturedCollections(int page, int perPage) {
        return unsplashApiService.getFeaturedCollections(page, perPage);
    }

    public Call<List<Photo>> getCollectionPhotos(long collectionId, int page, int perPage) {
        return unsplashApiService.getCollectionPhotos(collectionId, page, perPage);
    }

    public Call<PhotoCollection> createCollection(PhotoCollection collection) {
        return unsplashApiService.createCollection(collection);
    }

    public Call<Photo> addPhotoToCollection(long collectionId, String photoId) {
        return unsplashApiService.addPhotoToCollection(collectionId, photoId);
    }

    public Call<Photo> removePhotoFromCollection(long collectionId, String photoId) {
        return unsplashApiService.removePhotoFromCollection(collectionId, photoId);
    }

    public Call<UserProfile> getUserProfile(String username) {
        return unsplashApiService.getUserProfile(username);
    }

    public Call<List<PhotoCollection>> getUserCollections(String username) {
        return unsplashApiService.getUserCollections(username);
    }

    public Call<List<Photo>> getUserLikedPhotos(String username, int page, int perPage) {
        return unsplashApiService.getUserLikedPhotos(username, page, perPage);
    }

    public Call<List<Photo>> getUserPhotos(String username, int page, int perPage) {
        return unsplashApiService.getUserPhotos(username, page, perPage);
    }
}
