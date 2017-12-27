package com.anhhoang.zoompoint.ui.userprofile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anhhoang.zoompoint.R;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = UserProfileActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_user_profile, new UserProfileFragment(), TAG)
                    .commit();
        }
    }

}
