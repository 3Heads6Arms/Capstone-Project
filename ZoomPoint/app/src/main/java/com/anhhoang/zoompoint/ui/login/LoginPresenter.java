package com.anhhoang.zoompoint.ui.login;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.anhhoang.unsplashapi.UnsplashApi;
import com.anhhoang.unsplashapi.UnsplashAuthApi;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.unsplashmodel.authmodel.TokenResponse;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.utils.UserUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anh.hoang on 13.12.17.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private String token;
    private LoginContract.View view;

    private Callback<UserProfile> publicProfileCallback = new Callback<UserProfile>() {
        @Override
        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
            if (view != null) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ContentValues userProfile = UserUtils.parseUser(response.body());
                    view.saveMyProfile(userProfile);
                    view.navigateToHome();
                } else {
                    view.showError(R.string.login_error);
                }
                view.toggleProgress(false);
            }
        }

        @Override
        public void onFailure(Call<UserProfile> call, Throwable t) {
            if (view != null) {
                view.showError(R.string.login_error);
                view.toggleProgress(false);
            }
        }
    };
    private Callback<UserProfile> privateProfileCallback = new Callback<UserProfile>() {
        @Override
        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
            if (view != null) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    String username = response.body().getUsername();
                    view.saveUsername(username);
                    view.saveToken(token);

                    UnsplashApi.getInstance(token)
                            .getUserProfile(username)
                            .enqueue(publicProfileCallback);
                    view.navigateToHome();
                } else {
                    view.showError(R.string.login_error);
                    view.toggleProgress(false);
                }
            }
        }

        @Override
        public void onFailure(Call<UserProfile> call, Throwable t) {
            if (view != null) {
                view.showError(R.string.login_error);
                view.toggleProgress(false);
            }
        }
    };
    private Callback<TokenResponse> tokenCallback = new Callback<TokenResponse>() {
        @Override
        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
            if (view != null) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    TokenResponse tokenResponse = response.body();
                    token = tokenResponse.getAccessToken();

                    if (!TextUtils.isEmpty(token)) {
                        UnsplashApi.getInstance(token)
                                .getMyProfile()
                                .enqueue(privateProfileCallback);
                    } else {
                        view.showError(R.string.login_error);
                        view.toggleProgress(false);
                    }
                } else {
                    view.showError(R.string.login_error);
                    view.toggleProgress(false);
                }
            }
        }

        @Override
        public void onFailure(Call<TokenResponse> call, Throwable t) {
            if (view != null) {
                view.showError(R.string.login_connection_error);
                view.toggleProgress(false);
            }
        }
    };

    @Override
    public void attach(LoginContract.View view) {
        this.view = view;

        if (view != null) {
            view.setPresenter(this);
            if (view.isLoggedIn()) {
                view.navigateToHome();
            }
        }
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public Intent getLoginIntent() {
        return getUnsplashIntent(UnsplashAuthApi.getAuthUri());
    }

    @Override
    public Intent getRegisterIntent() {
        return getUnsplashIntent(UnsplashAuthApi.getRegisterUri());
    }

    @Override
    public void login(Uri uri) {
        view.toggleProgress(true);
        String authorizationCode = uri.getQueryParameter("code");
        UnsplashAuthApi.getInstance()
                .getToken(authorizationCode)
                .enqueue(tokenCallback);
    }

    private Intent getUnsplashIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }
}
