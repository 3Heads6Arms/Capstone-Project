package com.anhhoang.unsplashapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

public interface UnsplashAuthApiService {
    @POST("token")
    Call<Object> getToken(@Body Object body);
}
