package com.anhhoang.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.PhotoUrls;
import com.anhhoang.unsplashmodel.UserProfile;

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
        int match = URI_MATCHER.match(uri);
        Cursor cursor;
        SQLiteDatabase database = openHelper.getReadableDatabase();
        switch (match) {
            case PHOTOS:
                cursor = database.rawQuery(
                        "SELECT p.*, u." + UserProfile.COL_NAME +
                                ", u." + UserProfile.COL_USERNAME +
                                ", u." + PhotoUrls.COL_MEDIUM +
                                ", u." + PhotoUrls.COL_LARGE +
                                ", u." + PhotoUrls.COL_SMALL +
                                " FROM " + ZoomPointContract.PhotoEntry.TABLE_NAME + " p " +
                                "INNER JOIN " + ZoomPointContract.UserProfileEntry.TABLE_NAME + " u " +
                                "ON p." + Photo.COL_USER_ID + "=u." + UserProfile.COL_ID + " " +
                                "WHERE p." + selection,
                        selectionArgs);
                break;
            case PHOTO:
                String photoId = uri.getPathSegments().get(0);
                cursor = database.rawQuery(
                        "SELECT p.*, u." + UserProfile.COL_NAME +
                                ", u." + UserProfile.COL_USERNAME +
                                ", u." + PhotoUrls.COL_MEDIUM +
                                ", u." + PhotoUrls.COL_LARGE +
                                ", u." + PhotoUrls.COL_SMALL +
                                " FROM " + ZoomPointContract.PhotoEntry.TABLE_NAME + " p " +
                                "INNER JOIN " + ZoomPointContract.UserProfileEntry.TABLE_NAME + " u " +
                                "ON p." + Photo.COL_USER_ID + "=u." + UserProfile.COL_ID + " " +
                                "WHERE p." + Photo.COL_IDENTIFIER + "='" + photoId + "'",
                        selectionArgs);

                database.query(
                        ZoomPointContract.PhotoEntry.TABLE_NAME,
                        null,
                        Photo.COL_IDENTIFIER + "=?",
                        new String[]{photoId},
                        null,
                        null,
                        null);
                break;
            case COLLECTIONS:
                cursor = database.rawQuery(
                        "SELECT p.*, u." + UserProfile.COL_NAME + ", u." + UserProfile.COL_USERNAME + " FROM " + ZoomPointContract.CollectionEntry.TABLE_NAME + " p " +
                                "INNER JOIN " + ZoomPointContract.UserProfileEntry.TABLE_NAME + " u " +
                                "ON p." + PhotoCollection.COL_USER_ID + "=u." + UserProfile.COL_ID + " " +
                                "WHERE p." + selection,
                        selectionArgs);
                break;
            case COLLECTION:
                String collectionId = uri.getPathSegments().get(0);
                cursor = database.query(
                        ZoomPointContract.CollectionEntry.TABLE_NAME,
                        null,
                        PhotoCollection.COL_ID + "=?",
                        new String[]{collectionId},
                        null,
                        null,
                        null);
                break;
            case USER_PROFILES:
                cursor = database.query(
                        ZoomPointContract.UserProfileEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case USER_PROFILE:
                String userId = uri.getPathSegments().get(0);
                cursor = database.query(
                        ZoomPointContract.UserProfileEntry.TABLE_NAME,
                        null,
                        UserProfile.COL_ID + "=?",
                        new String[]{userId},
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return cursor;
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
        final int match = URI_MATCHER.match(uri);
        SQLiteDatabase database = openHelper.getWritableDatabase();
        long id;
        Uri resultUri;
        switch (match) {
            case PHOTOS:
                id = database.insertWithOnConflict(ZoomPointContract.PhotoEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                resultUri = ContentUris.withAppendedId(ZoomPointContract.PhotoEntry.CONTENT_URI, id);
                break;
            case COLLECTIONS:
                id = database.insertWithOnConflict(ZoomPointContract.CollectionEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                resultUri = ContentUris.withAppendedId(ZoomPointContract.CollectionEntry.CONTENT_URI, id);
                break;
            case USER_PROFILES:
                id = database.insertWithOnConflict(ZoomPointContract.UserProfileEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                resultUri = ContentUris.withAppendedId(ZoomPointContract.UserProfileEntry.CONTENT_URI, id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        // Since the DB uses its own kind of ID(some are string some are integer,
        // so the returned id from insert doesn't serve any real purpose.
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = URI_MATCHER.match(uri);
        SQLiteDatabase database = openHelper.getWritableDatabase();
        int rowsDeleted;
        switch (match) {
            case PHOTOS:
                rowsDeleted = database.delete(ZoomPointContract.PhotoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COLLECTIONS:
                rowsDeleted = database.delete(ZoomPointContract.CollectionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER_PROFILES:
                rowsDeleted = database.delete(ZoomPointContract.UserProfileEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = URI_MATCHER.match(uri);
        SQLiteDatabase database = openHelper.getWritableDatabase();
        int rowsUpdated;
        switch (match) {
            case PHOTOS:
                rowsUpdated = database.update(ZoomPointContract.PhotoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case COLLECTIONS:
                rowsUpdated = database.update(ZoomPointContract.CollectionEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case USER_PROFILES:
                rowsUpdated = database.update(ZoomPointContract.UserProfileEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final int match = URI_MATCHER.match(uri);
        SQLiteDatabase database = openHelper.getWritableDatabase();
        int rowsInserted = 0;
        String tableName;
        switch (match) {
            case PHOTOS:
                tableName = ZoomPointContract.PhotoEntry.TABLE_NAME;
                break;
            case COLLECTIONS:
                tableName = ZoomPointContract.CollectionEntry.TABLE_NAME;
                break;
            case USER_PROFILES:
                tableName = ZoomPointContract.UserProfileEntry.TABLE_NAME;
                break;
            default:
                return super.bulkInsert(uri, values);

        }

        database.beginTransaction();
        try {
            for (ContentValues contentValues : values) {
                long id = database.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                if (id != -1) {
                    rowsInserted++;
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }


        return rowsInserted;
    }
}
