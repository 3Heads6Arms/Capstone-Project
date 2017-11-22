package com.anhhoang.unsplashapi;

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

    private Retrofit retrofit;
    private UnsplashAuthApiService unsplashApiService;

    private UnsplashAuthApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        unsplashApiService = retrofit.create(UnsplashAuthApiService.class);
    }
}
