package com.anhhoang.zoompoint.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.anhhoang.zoompoint.ui.home.collections.CollectionsFragment;
import com.anhhoang.zoompoint.ui.home.favorites.FavoritesFragment;
import com.anhhoang.zoompoint.ui.home.photos.HomePhotosFragment;

/**
 * Created by anh.hoang on 17.12.17.
 */

public class ZoomPointPagerAdapter extends FragmentPagerAdapter {

    public ZoomPointPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomePhotosFragment();
            case 1:
                return new CollectionsFragment();
            case 2:
                return new FavoritesFragment();
            default:
                return new CollectionsFragment();
        }

    }

    // ZoomPoint Tabs are fixed
    @Override
    public int getCount() {
        return 4;
    }
}
