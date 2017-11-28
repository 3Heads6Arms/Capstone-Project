package com.anhhoang.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by anh.hoang on 28.11.17.
 */

public class ZoomPointProvider extends ContentProvider {
    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private static final int PHOTOS = 100;
    private static final int PHOTO = 101;
    private static final int COLLECTIONS = 200;
    private static final int COLLECTION = 201;
    private static final int USER_PROFILES = 300;
    private static final int USER_PROFILE = 301;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ZoomPointContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, ZoomPointContract.PATH_PHOTO, PHOTOS);
        uriMatcher.addURI(authority, ZoomPointContract.PATH_PHOTO + "/*", PHOTO);

        uriMatcher.addURI(authority, ZoomPointContract.PATH_COLLECTION, COLLECTIONS);
        uriMatcher.addURI(authority, ZoomPointContract.PATH_COLLECTION + "/#", COLLECTION);

        uriMatcher.addURI(authority, ZoomPointContract.PATH_USER_PROFILE, USER_PROFILES);
        uriMatcher.addURI(authority, ZoomPointContract.PATH_USER_PROFILE + "/*", USER_PROFILE);

        return uriMatcher;
    }

    private SQLiteOpenHelper openHelper;


    @Override
    public boolean onCreate() {
        openHelper = new ZoomPointDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = URI_MATCHER.match(uri);

        switch (match) {
            case PHOTOS:
                return ZoomPointContract.PhotoEntry.CONTENT_TYPE;
            case PHOTO:
                return ZoomPointContract.PhotoEntry.CONTENT_ITEM_TYPE;
            case COLLECTIONS:
                return ZoomPointContract.CollectionEntry.CONTENT_TYPE;
            case COLLECTION:
                return ZoomPointContract.CollectionEntry.CONTENT_ITEM_TYPE;
            case USER_PROFILES:
                return ZoomPointContract.UserProfileEntry.CONTENT_TYPE;
            case USER_PROFILE:
                return ZoomPointContract.UserProfileEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
