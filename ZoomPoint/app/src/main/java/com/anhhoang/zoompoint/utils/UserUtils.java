package com.anhhoang.zoompoint.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.anhhoang.unsplashmodel.PhotoUrls;
import com.anhhoang.unsplashmodel.UserProfile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by anh.hoang on 19.12.17.
 */

public class UserUtils {
    private UserUtils() {
        throw new UnsupportedOperationException("UserUtils cannot be instantiated");
    }

    public static ContentValues parseUser(UserProfile user) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(UserProfile.COL_ID, user.getId());
        contentValues.put(UserProfile.COL_UPDATED, user.getUpdatedAt().getTime());
        contentValues.put(UserProfile.COL_USERNAME, user.getUsername());
        contentValues.put(UserProfile.COL_NAME, user.getName());
        contentValues.put(UserProfile.COL_FIRST_NAME, user.getFirstName());
        contentValues.put(UserProfile.COL_LAST_NAME, user.getLastName());
        contentValues.put(UserProfile.COL_PORTFOLIO_URL, user.getPortfolioUrl());
        contentValues.put(UserProfile.COL_BIO, user.getBio());
        contentValues.put(UserProfile.COL_LOCATION, user.getLocation());
        contentValues.put(PhotoUrls.COL_MEDIUM, user.getProfileImage().getMedium());
        contentValues.put(PhotoUrls.COL_LARGE, user.getProfileImage().getLarge());
        contentValues.put(PhotoUrls.COL_SMALL, user.getProfileImage().getSmall());

        return contentValues;
    }

    public static ContentValues[] parseUsers(List<UserProfile> users) {
        List<ContentValues> contentValues = new ArrayList<>();

        for (UserProfile user : users) {
            contentValues.add(parseUser(user));
        }

        return contentValues.toArray(new ContentValues[contentValues.size()]);
    }

    public static UserProfile parse(Cursor cursor) {
        if (cursor.moveToFirst()) {
            UserProfile userProfile = new UserProfile();
            PhotoUrls urls = new PhotoUrls();

            userProfile.setId(cursor.getString(cursor.getColumnIndex(UserProfile.COL_ID)));
            userProfile.setUpdatedAt(new Date(cursor.getLong(cursor.getColumnIndex(UserProfile.COL_UPDATED))));
            userProfile.setUsername(cursor.getString(cursor.getColumnIndex(UserProfile.COL_USERNAME)));
            userProfile.setName(cursor.getString(cursor.getColumnIndex(UserProfile.COL_NAME)));
            userProfile.setFirstName(cursor.getString(cursor.getColumnIndex(UserProfile.COL_FIRST_NAME)));
            userProfile.setLastName(cursor.getString(cursor.getColumnIndex(UserProfile.COL_LAST_NAME)));
            userProfile.setPortfolioUrl(cursor.getString(cursor.getColumnIndex(UserProfile.COL_PORTFOLIO_URL)));
            userProfile.setBio(cursor.getString(cursor.getColumnIndex(UserProfile.COL_BIO)));
            userProfile.setLocation(cursor.getString(cursor.getColumnIndex(UserProfile.COL_LOCATION)));

            urls.setMedium(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_MEDIUM)));
            urls.setLarge(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_LARGE)));
            urls.setSmall(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_SMALL)));

            userProfile.setProfileImage(urls);

            return userProfile;
        }

        return null;
    }
}
