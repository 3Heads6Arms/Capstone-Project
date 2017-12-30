package com.anhhoang.unsplashapi;

import com.anhhoang.unsplashmodel.authmodel.TokenRequest;
import com.anhhoang.unsplashmodel.authmodel.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

public interface UnsplashAuthApiService {
    @POST("token")
    Call<TokenResponse> getToken(@Body TokenRequest body);
}
