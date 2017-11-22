package com.anhhoang.unsplashapi;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
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

    private Retrofit retrofit;
    private UnsplashApiService unsplashApiService;
    private String token;

    private UnsplashApi(String token) {
        this.token = token;

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthorizationInterceptor(this.token))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        unsplashApiService = retrofit.create(UnsplashApiService.class);
    }
}
