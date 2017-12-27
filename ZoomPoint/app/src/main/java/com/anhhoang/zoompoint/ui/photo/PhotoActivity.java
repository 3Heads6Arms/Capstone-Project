package com.anhhoang.zoompoint.ui.photo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.anhhoang.zoompoint.R;

import static com.bumptech.glide.util.Preconditions.checkArgument;

public class PhotoActivity extends AppCompatActivity {
    private static final String TAG = PhotoActivity.class.getCanonicalName();
    private static final String PHOTO_ID_KEY = "PhotoIdKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Intent intent = getIntent();

        String photoId = intent.getStringExtra(PHOTO_ID_KEY);

        checkArgument(!TextUtils.isEmpty(photoId), "PhotoId parameter cannot be null");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public static Intent getStartingIntent(Context context, String photoId) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(PHOTO_ID_KEY, photoId);

        return intent;
    }
}
