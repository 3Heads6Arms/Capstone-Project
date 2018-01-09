package com.anhhoang.zoompoint.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.PhotoUrls;
import com.anhhoang.unsplashmodel.UserProfile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by anh.hoang on 22.12.17.
 */

public class PhotoCollectionUtils {
    private PhotoCollectionUtils() {
        throw new UnsupportedOperationException("PhotoCollectionUtils cannot be instantiated");
    }

    public static ContentValues parseCollection(PhotoCollection collection, String type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotoCollection.COL_ID, collection.getId());
        contentValues.put(PhotoCollection.COL_TITLE, collection.getTitle());
        contentValues.put(PhotoCollection.COL_DESC, collection.getDescription());
        contentValues.put(PhotoCollection.COL_PUBLISHED, collection.getPublishedAt().getTime());
        contentValues.put(PhotoCollection.COL_UPDATED, collection.getUpdatedAt().getTime());
        contentValues.put(PhotoCollection.COL_CURATED, collection.isCurated());
        contentValues.put(PhotoCollection.COL_FEATURED, collection.isFeatured());
        contentValues.put(PhotoCollection.COL_TYPE, type);

        if (collection.getPhoto() != null) {
            contentValues.put(Photo.COL_WIDTH, collection.getPhoto().getWidth());
            contentValues.put(Photo.COL_HEIGHT, collection.getPhoto().getHeight());

            if (collection.getPhoto().getUrls() != null) {
                contentValues.put(PhotoUrls.COL_RAW, collection.getPhoto().getUrls().getRaw());
                contentValues.put(PhotoUrls.COL_FULL, collection.getPhoto().getUrls().getFull());
                contentValues.put(PhotoUrls.COL_REGULAR, collection.getPhoto().getUrls().getRegular());
                contentValues.put(PhotoUrls.COL_THUMB, collection.getPhoto().getUrls().getThumb());
                contentValues.put(PhotoUrls.COL_SMALL, collection.getPhoto().getUrls().getSmall());
            }
        }

        if (collection.getUser() != null) {
            contentValues.put(PhotoCollection.COL_USER_ID, collection.getUser().getId());
        }

        return contentValues;
    }

    public static ContentValues[] parseCollections(List<PhotoCollection> collections, String type) {
        List<ContentValues> contentValues = new ArrayList<>();

        for (PhotoCollection collection : collections) {
            contentValues.add(parseCollection(collection, type));
        }

        return contentValues.toArray(new ContentValues[contentValues.size()]);
    }

    public static PhotoCollection parseCollection(Cursor cursor) {
        PhotoCollection collection = new PhotoCollection();
        Photo photo = new Photo();
        PhotoUrls urls = new PhotoUrls();
        UserProfile user = new UserProfile();

        collection.setId(cursor.getInt(cursor.getColumnIndex(PhotoCollection.COL_ID)));
        collection.setTitle(cursor.getString(cursor.getColumnIndex(PhotoCollection.COL_TITLE)));
        collection.setDescription(cursor.getString(cursor.getColumnIndex(PhotoCollection.COL_DESC)));
        collection.setPublishedAt(new Date(cursor.getLong(cursor.getColumnIndex(PhotoCollection.COL_PUBLISHED))));
        collection.setUpdatedAt(new Date(cursor.getLong(cursor.getColumnIndex(PhotoCollection.COL_UPDATED))));
        collection.setCurated(cursor.getInt(cursor.getColumnIndex(PhotoCollection.COL_CURATED)) > 0);
        collection.setFeatured(cursor.getInt(cursor.getColumnIndex(PhotoCollection.COL_FEATURED)) > 0);
        collection.setType(cursor.getString(cursor.getColumnIndex(PhotoCollection.COL_TYPE)));


        photo.setWidth(cursor.getInt(cursor.getColumnIndex(Photo.COL_WIDTH)));
        photo.setHeight(cursor.getInt(cursor.getColumnIndex(Photo.COL_HEIGHT)));

        urls.setRaw(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_RAW)));
        urls.setFull(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_FULL)));
        urls.setRegular(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_REGULAR)));
        urls.setThumb(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_THUMB)));
        urls.setSmall(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_SMALL)));

        user.setId(cursor.getString(cursor.getColumnIndex(UserProfile.COL_ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(UserProfile.COL_NAME)));

        photo.setUrls(urls);
        collection.setPhoto(photo);
        collection.setUser(user);

        return collection;
    }

    public static List<PhotoCollection> parseCollections(Cursor cursor) {
        List<PhotoCollection> collections = new ArrayList<>();


        if (cursor.moveToNext()) {
            do {
                collections.add(parseCollection(cursor));
            } while (cursor.moveToNext());
        }

        return collections;
    }
}
