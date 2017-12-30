package com.anhhoang.zoompoint.ui.photo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;

import com.anhhoang.zoompoint.R;

import static com.bumptech.glide.util.Preconditions.checkArgument;

public class PhotoActivity extends AppCompatActivity {
    private static final String TAG = PhotoActivity.class.getCanonicalName();
    private static final String PHOTO_ID_KEY = "PhotoIdKey";
    private static final String PHOTO_TYPE_KEY = "PhotoTypeKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Intent intent = getIntent();
        String photoId = intent.getStringExtra(PHOTO_ID_KEY);
        String photoType = intent.getStringExtra(PHOTO_TYPE_KEY);
        checkArgument(!TextUtils.isEmpty(photoId), "PhotoId parameter cannot be null");
        checkArgument(!TextUtils.isEmpty(photoId), "PhotoType parameter cannot be null");


        if (savedInstanceState == null) {
            Fragment fragment = new PhotoFragment();
            fragment.setArguments(PhotoFragment.getStartingBundle(photoId, photoType));

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_photo, fragment, TAG)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getStartingIntent(Context context, String photoId, String photoType) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(PHOTO_ID_KEY, photoId);
        intent.putExtra(PHOTO_TYPE_KEY, photoType);
        return intent;
    }
}
