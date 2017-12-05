package com.anhhoang.zoompoint.ui;

import com.anhhoang.zoompoint.BaseMvpContract;

/**
 * Created by Anh.Hoang on 12/5/2017.
 */

public interface LoginContracts {
    interface LoginView extends BaseMvpContract.View<LoginPresenter>{
        void saveToken(String token);
    }

    interface LoginPresenter extends BaseMvpContract.Presenter<LoginView>{
        void login();
        void createAccount();
    }
}
