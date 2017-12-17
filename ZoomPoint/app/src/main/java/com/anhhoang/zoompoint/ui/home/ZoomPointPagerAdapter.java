package com.anhhoang.zoompoint.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by anh.hoang on 17.12.17.
 */

public class ZoomPointPagerAdapter extends FragmentPagerAdapter {

    public ZoomPointPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    // ZoomPoint Tabs are fixed
    @Override
    public int getCount() {
        return 4;
    }
}
