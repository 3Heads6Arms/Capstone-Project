package com.anhhoang.zoompoint.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.ui.userprofile.UserProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    int[] tabIcons = {
            R.drawable.ic_image_white_24dp,
            R.drawable.ic_photo_library_white_24dp,
            R.drawable.ic_favorite_white_24dp,
            R.drawable.ic_search_white_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        tabs.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new ZoomPointPagerAdapter(getSupportFragmentManager()));
        setTabIcons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String username = preferences.getString(getString(R.string.username_preference_key), null);
            String fullname = preferences.getString(getString(R.string.fullname_preference_key), null);

            Intent intent = UserProfileActivity.getStartingIntent(this, username, fullname);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTabIcons() {
        for (int i = 0; i < 4; i++) {
            tabs.getTabAt(i).setIcon(tabIcons[i]);
        }
    }
}
