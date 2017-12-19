package com.anhhoang.zoompoint.utils;

import android.content.ContentValues;

import com.anhhoang.unsplashmodel.PhotoUrls;
import com.anhhoang.unsplashmodel.UserProfile;

import java.util.ArrayList;
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

    public static List<ContentValues> parseUsers(List<UserProfile> users) {
        List<ContentValues> contentValues = new ArrayList<>();

        for (UserProfile user : users) {
            contentValues.add(parseUser(user));
        }

        return contentValues;
    }

    public static UserProfile parse(ContentValues contentValues) {
        // TODO: Parse to user
        throw new UnsupportedOperationException();
    }
}
