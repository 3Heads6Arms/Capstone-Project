package com.anhhoang.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.anhhoang.unsplashmodel.Photo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by anh.hoang on 29.11.17.
 */

@RunWith(AndroidJUnit4.class)
public class ZoomPointProviderTest {
    private Context context;

    @Before
    public void setupTest() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Clean Db for new test
        SQLiteOpenHelper sqLiteOpenHelper = new ZoomPointDbHelper(context);
        sqLiteOpenHelper.onUpgrade(sqLiteOpenHelper.getWritableDatabase(), 0, 0);
    }

    @Test
    public void testInsertPhoto() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Photo.COL_IDENTIFIER, "number1");

        Uri uri = context.getContentResolver().insert(ZoomPointContract.PhotoEntry.CONTENT_URI, contentValues);

        long id = ContentUris.parseId(uri);

        Assert.assertTrue(id != -1);
    }
}
