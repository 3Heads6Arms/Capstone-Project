package com.anhhoang.unsplashapi;

import com.anhhoang.unsplashapi.RequestModel.RequestAddToCollection;
import com.anhhoang.unsplashapi.RequestModel.RequestSearchPhoto;
import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by anh.hoang on 21.11.17.
 */

public interface UnsplashApiService {

    @GET("photos")
    Call<List<Photo>> getPhotos(
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Query("order_by") String orderBy);

    @GET("photos/curated")
    Call<List<Photo>> getCuratedPhotos(
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("photos/{id}")
    Call<Photo> getPhoto(@Path("id") String photoId);

    @GET("photos/random")
    Call<Photo> getRandomPhoto(@Query("featured") boolean featured);

    @POST("photos/{id}/like")
    Call<Photo> likePhoto(@Path("id") String photoId);


    @DELETE("photos/{id}/like")
    Call<Photo> unlikePhoto(@Path("id") String photoId);

    @GET("search/photos")
    Call<RequestSearchPhoto> searchPhotos(
            @Query("query") String searchTerms,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("collections/featured")
    Call<List<PhotoCollection>> getFeaturedCollections(@Query("page") int page, @Query("per_page") int perPage);

    @GET("collections/{id}/photos")
    Call<List<Photo>> getCollectionPhotos(
            @Path("id") long collectionId,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @POST("collections")
    Call<PhotoCollection> createCollection(@Body PhotoCollection collection);

    // Returns an object that wraps Photo
    @POST("collections/{id}/add")
    Call<Photo> addPhotoToCollection(@Path("id") long collectionId, @Body RequestAddToCollection addToCollection);


    // Returns an object that wraps Photo
    @DELETE("collections/{id}/remove")
    Call<Photo> removePhotoFromCollection(@Path("id") long collectionId, @Body String photoId);

    @GET("me")
    Call<UserProfile> getMyProfile();

    @GET("users/{username}")
    Call<UserProfile> getUserProfile(@Path("username") String username);

    @GET("users/{username}/collections")
    Call<List<PhotoCollection>> getUserCollections(
            @Path("username") String username,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("users/{username}/likes")
    Call<List<Photo>> getUserLikedPhotos(
            @Path("username") String username,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("users/{username}/photos")
    Call<List<Photo>> getUserPhotos(
            @Path("username") String username,
            @Query("page") int page,
            @Query("per_page") int perPage);
}
