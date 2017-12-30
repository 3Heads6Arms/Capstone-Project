package com.anhhoang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.UserProfile;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by anh.hoang on 28.11.17.
 */

@RunWith(AndroidJUnit4.class)
public class ZoomPointDbTest {
    private SQLiteOpenHelper sqLiteOpenHelper;

    @Before
    public void setupDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        sqLiteOpenHelper = new ZoomPointDbHelper(context);
        sqLiteOpenHelper.onUpgrade(sqLiteOpenHelper.getWritableDatabase(), 0, 0);
    }

    @Test
    public void userTableCreatedAndInserted() {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        SQLiteDatabase readDatabase = sqLiteOpenHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserProfile.COL_BIO, "#gask432");

        long id = database.insert(ZoomPointContract.UserProfileEntry.TABLE_NAME, null, contentValues);

        Cursor cursor = readDatabase.query(ZoomPointContract.UserProfileEntry.TABLE_NAME, null, null, null, null, null, null);

        Assert.assertTrue(cursor.getCount() > 0);
    }

    @Test
    public void photoTableCreatedAndInserted() {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        SQLiteDatabase readDatabase = sqLiteOpenHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Photo.COL_IDENTIFIER, "#gask432");

        long id = database.insert(ZoomPointContract.PhotoEntry.TABLE_NAME, null, contentValues);

        Cursor cursor = readDatabase.query(ZoomPointContract.PhotoEntry.TABLE_NAME, null, null, null, null, null, null);

        Assert.assertTrue(cursor.getCount() > 0);
    }

    @Test
    public void collectionTableCreatedAndInserted() {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        SQLiteDatabase readDatabase = sqLiteOpenHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotoCollection.COL_ID, "#gask432");

        long id = database.insert(ZoomPointContract.CollectionEntry.TABLE_NAME, null, contentValues);

        Cursor cursor = readDatabase.query(ZoomPointContract.CollectionEntry.TABLE_NAME, null, null, null, null, null, null);

        Assert.assertTrue(cursor.getCount() > 0);
    }
}
