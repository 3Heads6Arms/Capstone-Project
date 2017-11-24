package com.anhhoang.unsplashapi;

import com.anhhoang.unsplashmodel.authmodel.TokenRequest;
import com.anhhoang.unsplashmodel.authmodel.TokenResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anh.Hoang on 11/22/2017.
 */

public class UnsplashAuthApi {
    private static final String API_URL = "https://unsplash.com/oauth/";
    private static UnsplashAuthApi INSTANCE;

    public static UnsplashAuthApi getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UnsplashAuthApi();
        }

        return INSTANCE;
    }

    private UnsplashAuthApiService unsplashApiService;

    private UnsplashAuthApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        unsplashApiService = retrofit.create(UnsplashAuthApiService.class);
    }

    public Call<TokenResponse> getToken(TokenRequest request) {
        return unsplashApiService.getToken(request);
    }
}
