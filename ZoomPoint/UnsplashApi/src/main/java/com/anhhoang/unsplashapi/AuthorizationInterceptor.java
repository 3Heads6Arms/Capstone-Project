package com.anhhoang.unsplashapi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by anh.hoang on 22.11.17.
 */

public class AuthorizationInterceptor implements Interceptor {
    private String token;

    public AuthorizationInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", token)
                .build();

        Response response = chain.proceed(request);
        return response;
    }
}
