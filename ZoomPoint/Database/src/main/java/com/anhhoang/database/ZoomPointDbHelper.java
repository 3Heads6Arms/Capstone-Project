package com.anhhoang.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

import com.anhhoang.unsplashmodel.Photo;

/**
 * Created by anh.hoang on 26.11.17.
 */

public class ZoomPointDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "zoompoint.db";
    private static final int DB_VERSION = 1;


    public ZoomPointDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public ZoomPointDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createPhotoDbQuery = "CREATE TABLE " + ZoomPointContract.PhotoEntry.TABLE_NAME + " (" +
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
                // TODO put EXIF & URLS
                Photo.COL_COLLECTION_ID + " INTEGER, " + // UNIQUE but is not primary key, due to API difference. The app will ignore all _IDs columns
                Photo.COL_USER_ID + " TEXT,";
                ;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ZoomPointContract.PhotoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ZoomPointContract.CollectionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ZoomPointContract.UserProfileEntry.TABLE_NAME);
        onCreate(db);
    }
}
