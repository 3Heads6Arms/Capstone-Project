package com.anhhoang.zoompoint;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Anh.Hoang on 12/5/2017.
 */

public class BaseView<P extends BaseMvpContract.Presenter> extends AppCompatActivity implements BaseMvpContract.View<P> {
    protected P presenter;

    @Override
    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }
}
