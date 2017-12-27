package com.anhhoang.zoompoint.ui.photo;

import com.anhhoang.zoompoint.BaseMvpContract;

/**
 * Created by anh.hoang on 28.12.17.
 */

public interface PhotoContract {
    interface View extends BaseMvpContract.View {
    }

    interface Presenter extends BaseMvpContract.Presenter<View> {

    }
}
