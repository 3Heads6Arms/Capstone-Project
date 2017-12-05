package com.anhhoang.zoompoint;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Anh.Hoang on 12/5/2017.
 */

public abstract class BasePresenter<V extends BaseMvpContract.View> implements BaseMvpContract.Presenter<V> {
    protected V view;

    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        this.view = null;
    }
}
