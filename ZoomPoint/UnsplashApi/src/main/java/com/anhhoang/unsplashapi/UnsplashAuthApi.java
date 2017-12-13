package com.anhhoang.unsplashapi;

import android.net.Uri;
import android.text.TextUtils;

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

    private static String[] scopes = new String[]{
            "public",
            "read_user",
            "write_user",
            "read_photos",
            "write_photos",
            "write_likes",
            "write_followers",
            "read_collections",
            "write_collections"
    };

    public static final String AUTH_URL = "https://unsplash.com/oauth/authorize";

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

    public static Uri getAuthUri() {
        return Uri.parse(AUTH_URL)
                .buildUpon()
                .appendQueryParameter("client_id", BuildConfig.UNSPLASH_API_KEY)
                .appendQueryParameter("redirect_uri", "zoompoint://auth/callback")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("scope", TextUtils.join("+", scopes))
                .build();
    }
}
