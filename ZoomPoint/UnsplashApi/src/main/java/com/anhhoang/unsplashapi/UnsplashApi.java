package com.anhhoang.unsplashapi;

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

public interface UnsplashApi {

    @GET("photos")
    Call<Object> getPhotos(
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Query("order_by") String orderBy);

    @GET("photos/curated")
    Call<Object> getCuratedPhotos(
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Query("order_by") String orderBy);

    @GET("photos/{id}")
    Call<Object> getPhoto(@Path("id") String photoId);

    @GET("photos/random")
    Call<Object> getRandomPhoto(@Query("featured") boolean featured); // TODO: Check featured's type

    @POST("photos/{id}/like")
    Call<Object> likePhoto(@Path("id") String photoId);


    @DELETE("photos/{id}/like")
    Call<Object> unlikePhoto(@Path("id") String photoId);

    @GET("search/photos")
    Call<Object> searchPhotos(
            @Query("query") String searchTerms,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("collections/featured")
    Call<Object> getFeaturedCollections(@Query("page") int page, @Query("per_page") int perPage);

    @GET("collections/{id}/photos")
    Call<Object> getCollectionPhotos(
            @Path("id") long collectionId,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @POST("collections")
    Call<Object> createCollection(@Body Object collection);

    @POST("collections/{id}/add")
    Call<Object> addPhotoToCollection(@Path("id") long collectionId, @Body Object photoId);


    @DELETE("collections/{id}/remove")
    Call<Object> removePhotoFromCollection(@Path("id") long collectionId, @Body Object photoId);

    @GET("users/{username}")
    Call<Object> getUserProfile(@Path("username") String username);

    @GET("users/{username}/collections")
    Call<Object> getUserCollections(@Path("username") String username);

    @GET("users/{username}/likes")
    Call<Object> getUserLikedPhotos(
            @Path("username") String username,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("users/{username}/photos")
    Call<Object> getUserPhotos(
            @Path("username") String username,
            @Query("page") int page,
            @Query("per_page") int perPage);
}
