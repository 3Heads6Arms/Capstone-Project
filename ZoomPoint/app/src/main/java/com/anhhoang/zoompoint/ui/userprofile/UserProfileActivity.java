package com.anhhoang.zoompoint.ui.userprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.anhhoang.zoompoint.R;

import static com.bumptech.glide.util.Preconditions.checkArgument;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = UserProfileActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();

        checkArgument(intent.hasExtra(UserProfileFragment.USERNAME_KEY), "Username parameter must be passed");
        checkArgument(intent.hasExtra(UserProfileFragment.FULL_NAME_KEY), "Fullname parameter must be passed");

        if (savedInstanceState == null) {
            String username = intent.getStringExtra(UserProfileFragment.USERNAME_KEY);
            String fullname = intent.getStringExtra(UserProfileFragment.FULL_NAME_KEY);

            Fragment fragment = new UserProfileFragment();
            fragment.setArguments(UserProfileFragment.getStartingBundle(username, fullname));
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_user_profile, fragment, TAG)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getStartingIntent(Context context, String username, String fullname) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(UserProfileFragment.USERNAME_KEY, username);
        intent.putExtra(UserProfileFragment.FULL_NAME_KEY, fullname);

        return intent;
    }
}
