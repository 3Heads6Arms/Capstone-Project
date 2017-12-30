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

import java.util.Random;

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
        long id = insertPhoto("number1");

        Assert.assertTrue(id != -1);
    }

    @Test
    public void testDeletePhoto() {
        insertPhoto("number1");
        int deletedRows = deletePhoto("number1");

        Assert.assertTrue(deletedRows > 0);
    }

    @Test
    public void testMassDeletePhoto() {
        Random random = new Random();
        int expectedCounts = random.nextInt(10);
        for (int i = 0; i < expectedCounts; i++) {
            insertPhoto("number" + i);
        }
        int deletedRows = massDeletePhoto();
        Assert.assertEquals(expectedCounts, deletedRows);
    }


    private long insertPhoto(String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Photo.COL_IDENTIFIER, id);

        Uri uri = context.getContentResolver().insert(ZoomPointContract.PhotoEntry.CONTENT_URI, contentValues);

        return ContentUris.parseId(uri);
    }

    private int deletePhoto(String id) {
        return context.getContentResolver()
                .delete(
                        ZoomPointContract.PhotoEntry.CONTENT_URI,
                        Photo.COL_IDENTIFIER + "=?",
                        new String[]{id});
    }

    private int massDeletePhoto() {
        return context.getContentResolver()
                .delete(
                        ZoomPointContract.PhotoEntry.CONTENT_URI,
                        null,
                        null);
    }

    // TODO: add more tests
}
