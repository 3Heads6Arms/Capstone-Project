package com.anhhoang.zoompoint.ui;

import android.content.Intent;
import android.net.Uri;

import com.anhhoang.unsplashapi.UnsplashAuthApi;
import com.anhhoang.unsplashmodel.authmodel.TokenResponse;
import com.anhhoang.zoompoint.R;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anh.hoang on 13.12.17.
 */

public class LoginPresenter implements LoginContracts.Presenter {
    private LoginContracts.View view;

    @Override
    public void onAttach(LoginContracts.View view) {
        this.view = view;

        if (view != null) {
            view.setPresenter(this);
            view.toggleProgress(true);
            view.checkLoggedIn();
        }
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public Intent getLoginIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW, UnsplashAuthApi.getAuthUri());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    @Override
    public void login(Uri uri) {
        view.toggleProgress(true);
        String authorizationCode = uri.getQueryParameter("code");

        UnsplashAuthApi.getInstance()
                .getToken(authorizationCode)
                .enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            TokenResponse tokenResponse = response.body();

                            view.saveToken(tokenResponse.getAccessToken());
                        } else {
                            view.showError(R.string.login_error);
                        }

                        view.toggleProgress(false);
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        view.showError(R.string.login_connection_error);
                        view.toggleProgress(false);
                    }
                });
    }

    @Override
    public void createAccount() {
        // TODO: Create account
    }
}
