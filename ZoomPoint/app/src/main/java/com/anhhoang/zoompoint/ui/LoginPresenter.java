package com.anhhoang.zoompoint.ui;

import android.content.Intent;

import com.anhhoang.unsplashapi.UnsplashAuthApi;

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
    public void login() {
        Intent intent = new Intent(Intent.ACTION_VIEW, UnsplashAuthApi.getAuthUri());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (view != null) {
            view.openLogin(intent);
        }
    }

    @Override
    public void createAccount() {

    }
}
