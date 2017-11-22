package com.anhhoang.unsplashapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anh.hoang on 21.11.17.
 */

public class UnsplashApi {
    private static final String API_URL = "https://api.unsplash.com/";
    private static UnsplashApi INSTANCE;

    public static UnsplashApi getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UnsplashApi();
        }

        return INSTANCE;
    }

    private Retrofit retrofit;
    private UnsplashApiService unsplashApiService;

    private UnsplashApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        unsplashApiService = retrofit.create(UnsplashApiService.class);
    }
}
