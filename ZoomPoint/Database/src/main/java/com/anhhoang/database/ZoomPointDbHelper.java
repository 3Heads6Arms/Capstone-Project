package com.anhhoang.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.anhhoang.unsplashmodel.Exif;
import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.PhotoLocation;
import com.anhhoang.unsplashmodel.PhotoUrls;
import com.anhhoang.unsplashmodel.UserProfile;

/**
 * Created by anh.hoang on 26.11.17.
 */

public class ZoomPointDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "zoompoint.db";
    private static final int DB_VERSION = 2;


    public ZoomPointDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public ZoomPointDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createPhotoQuery = "CREATE TABLE " + ZoomPointContract.PhotoEntry.TABLE_NAME + " (" +
                ZoomPointContract.PhotoEntry._ID + " INTEGER PRIMARY KEY, " +
                Photo.COL_IDENTIFIER + " TEXT UNIQUE, " +
                Photo.COL_CREATED_AT + " NUMERIC, " +
                Photo.COL_UPDATED_AT + " NUMERIC, " +
                Photo.COL_WIDTH + " INTEGER, " +
                Photo.COL_HEIGHT + " INTEGER, " +
                Photo.COL_COLOR + " TEXT, " +
                Photo.COL_LIKES + " INTEGER, " +
                Photo.COL_LIKED_BY_USER + " NUMERIC, " +
                Photo.COL_DESC + " TEXT, " +
                Photo.COL_TYPE + " TEXT, " +
                Exif.COL_MAKE + " TEXT, " +
                Exif.COL_MODEL + " TEXT, " +
                Exif.COL_EXPOSURE_TIME + " TEXT, " +
                Exif.COL_APERTURE + " TEXT," +
                Exif.COL_FOCAL_LENGTH + " TEXT, " +
                Exif.COL_ISO + " INTEGER, " +
                PhotoLocation.COL_CITY + " TEXT, " +
                PhotoLocation.COL_COUNTRY + " TEXT, " +
                PhotoLocation.COL_LAT + " TEXT, " +
                PhotoLocation.COL_LON + " TEXT, " +
                PhotoUrls.COL_RAW + " TEXT, " +
                PhotoUrls.COL_FULL + " TEXT, " +
                PhotoUrls.COL_REGULAR + " TEXT, " +
                PhotoUrls.COL_THUMB + " TEXT, " +
                PhotoUrls.COL_SMALL + " TEXT, " +
                Photo.COL_COLLECTION_ID + " INTEGER, " + // UNIQUE but is not primary key, due to API difference. The app will ignore all _IDs columns
                Photo.COL_USER_ID + " TEXT);"; // UNIQUE but is not primary key, due to API difference. The app will ignore all _IDs columns

        final String createCollectionQuery = "CREATE TABLE " + ZoomPointContract.CollectionEntry.TABLE_NAME + " (" +
                ZoomPointContract.CollectionEntry._ID + " INTEGER PRIMARY KEY, " +
                PhotoCollection.COL_ID + " INTEGER UNIQUE, " +
                PhotoCollection.COL_TITLE + " TEXT, " +
                PhotoCollection.COL_DESC + " TEXT, " +
                PhotoCollection.COL_PUBLISHED + " NUMERIC, " +
                PhotoCollection.COL_UPDATED + " NUMERIC, " +
                PhotoCollection.COL_CURATED + " NUMERIC, " +
                PhotoCollection.COL_FEATURED + " NUMERIC, " +
                PhotoCollection.COL_TYPE + " TEXT, " +
                PhotoUrls.COL_RAW + " TEXT, " +
                PhotoUrls.COL_FULL + " TEXT, " +
                PhotoUrls.COL_REGULAR + " TEXT, " +
                PhotoUrls.COL_THUMB + " TEXT, " +
                PhotoUrls.COL_SMALL + " TEXT, " +
                Photo.COL_WIDTH + " INTEGER, " +
                Photo.COL_HEIGHT + " INTEGER, " +
                PhotoCollection.COL_USER_ID + " TEXT);"; // UNIQUE but is not primary key, due to API difference. The app will ignore all _IDs columns

        final String createUserProfileQuery = "CREATE TABLE " + ZoomPointContract.UserProfileEntry.TABLE_NAME + " (" +
                ZoomPointContract.UserProfileEntry._ID + " INTEGER PRIMARY KEY, " +
                UserProfile.COL_ID + " TEXT UNIQUE, " +
                UserProfile.COL_UPDATED + " NUMERIC, " +
                UserProfile.COL_USERNAME + " TEXT UNIQUE, " +
                UserProfile.COL_NAME + " TEXT, " +
                UserProfile.COL_FIRST_NAME + " TEXT, " +
                UserProfile.COL_LAST_NAME + " TEXT, " +
                UserProfile.COL_PORTFOLIO_URL + " TEXT, " +
                UserProfile.COL_BIO + " TEXT, " +
                UserProfile.COL_LOCATION + " TEXT, " +
                UserProfile.COL_TWITTER + " TEXT, " +
                PhotoUrls.COL_MEDIUM + " TEXT, " +
                PhotoUrls.COL_LARGE + " TEXT, " +
                PhotoUrls.COL_SMALL + " TEXT);";

        db.execSQL(createPhotoQuery);
        db.execSQL(createCollectionQuery);
        db.execSQL(createUserProfileQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Since the saved data is not crucial & sensitive to changes
        // the db can be dropped and create new version
        db.execSQL("DROP TABLE IF EXISTS " + ZoomPointContract.PhotoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ZoomPointContract.CollectionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ZoomPointContract.UserProfileEntry.TABLE_NAME);
        onCreate(db);
    }
}
