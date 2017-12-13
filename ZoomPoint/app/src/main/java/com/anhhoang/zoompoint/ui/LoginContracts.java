package com.anhhoang.zoompoint.ui;

import android.content.Intent;

import com.anhhoang.zoompoint.BaseMvpContract;

/**
 * Created by Anh.Hoang on 12/5/2017.
 */

public interface LoginContracts {
    interface View extends BaseMvpContract.View<Presenter> {
        void saveToken(String token);

        void checkLoggedIn();

        void toggleProgress(boolean show);

        void openLogin(Intent intent);
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        void login();

        void createAccount();
    }
}
