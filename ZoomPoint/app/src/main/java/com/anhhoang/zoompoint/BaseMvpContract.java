package com.anhhoang.zoompoint;

/**
 * Created by Anh.Hoang on 12/5/2017.
 */

public interface BaseMvpContract {
    interface View<P extends Presenter>{
        void setPresenter(P presenter);
    }
    interface Presenter<V extends View> {
        void attach(V view);
        void detach();
    }
}
