package com.anhhoang.zoompoint.ui.login;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;

import com.anhhoang.zoompoint.BaseMvpContract;

/**
 * Created by Anh.Hoang on 12/5/2017.
 */

public interface LoginContract {
    interface View extends BaseMvpContract.View<Presenter> {
        void saveToken(String token);

        void saveUsername(String username);

        void saveFullName(String fullName);

        void saveMyProfile(ContentValues userProfile);

        boolean isLoggedIn();

        void toggleProgress(boolean show);

        void showError(String message);

        void showError(int idString);

        void navigateToHome();
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
        Intent getLoginIntent();

        Intent getRegisterIntent();

        void login(Uri callbackUri);
    }
}
