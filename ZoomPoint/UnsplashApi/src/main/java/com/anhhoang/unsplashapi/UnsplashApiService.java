package com.anhhoang.unsplashapi;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anh.hoang on 21.11.17.
 */

public class UnsplashApiService {
    private static final String API_URL = "";
    private static UnsplashApiService INSTANCE;

    public static UnsplashApiService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UnsplashApiService();
        }

        return INSTANCE;
    }

    private Retrofit retrofit;
    private UnsplashApi unsplashApi;

    private UnsplashApiService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        unsplashApi = retrofit.create(UnsplashApi.class);
    }
}
