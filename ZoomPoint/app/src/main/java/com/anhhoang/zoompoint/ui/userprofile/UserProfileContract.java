package com.anhhoang.zoompoint.ui.userprofile;

import com.anhhoang.zoompoint.BaseMvpContract;

/**
 * Created by anh.hoang on 25.12.17.
 */

public interface UserProfileContract {
    interface View extends BaseMvpContract.View<Presenter> {

    }

    interface Presenter extends BaseMvpContract.Presenter<View> {
    }
}
