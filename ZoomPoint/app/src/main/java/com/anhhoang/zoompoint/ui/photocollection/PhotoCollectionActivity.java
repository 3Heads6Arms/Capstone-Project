package com.anhhoang.zoompoint.ui.photocollection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.anhhoang.zoompoint.R;

import static com.google.common.base.Preconditions.checkArgument;

public class PhotoCollectionActivity extends AppCompatActivity {
    private static final String COLLECTION_ID = "CollectionIdKey";
    private static final String COLLECTION_NAME = "CollectionNameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_collection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        long collectionId = intent.getLongExtra(COLLECTION_ID, -1);
        if (collectionId <= -1 && !intent.hasExtra(COLLECTION_NAME)) {
            throw new IllegalArgumentException("Missing required extras. Get starting intent from static method or provide required extras");
        }

        String collectionName = intent.getStringExtra(COLLECTION_NAME);

        getSupportActionBar().setTitle(collectionName);

        if (savedInstanceState == null) {
            PhotoCollectionFragment fragment = new PhotoCollectionFragment();
            fragment.setArguments(PhotoCollectionFragment.createBundle(collectionId));

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_photo_collection, fragment, PhotoCollectionFragment.TAG)
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

    public static Intent getStartingIntent(Context context, long collectionId, String collectionName) {
        checkArgument(collectionId > 0, "Invalid collection Id, must be greater than 0");

        Intent intent = new Intent(context, PhotoCollectionActivity.class);

        intent.putExtra(COLLECTION_ID, collectionId);
        intent.putExtra(COLLECTION_NAME, collectionName);

        return intent;
    }
}
