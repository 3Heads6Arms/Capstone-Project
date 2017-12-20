package com.anhhoang.zoompoint.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.anhhoang.unsplashmodel.Exif;
import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.PhotoLocation;
import com.anhhoang.unsplashmodel.PhotoUrls;
import com.anhhoang.unsplashmodel.UserProfile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by anh.hoang on 19.12.17.
 */

public class PhotoUtils {
    private PhotoUtils() {
        throw new UnsupportedOperationException("PhotoUtils cannot be instantiated");
    }

    public static ContentValues parsePhoto(Photo photo, String type) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Photo.COL_IDENTIFIER, photo.getId());
        contentValues.put(Photo.COL_CREATED_AT, photo.getCreatedAt().getTime());
        contentValues.put(Photo.COL_UPDATED_AT, photo.getUpdatedAt().getTime());
        contentValues.put(Photo.COL_WIDTH, photo.getWidth());
        contentValues.put(Photo.COL_HEIGHT, photo.getHeight());
        contentValues.put(Photo.COL_COLOR, photo.getColor());
        contentValues.put(Photo.COL_LIKES, photo.getLikes());
        contentValues.put(Photo.COL_LIKED_BY_USER, photo.isLikedByUser());
        contentValues.put(Photo.COL_DESC, photo.getDescription());
        contentValues.put(Photo.COL_TYPE, type);
        contentValues.put(PhotoUrls.COL_RAW, photo.getUrls().getRaw());
        contentValues.put(PhotoUrls.COL_FULL, photo.getUrls().getFull());
        contentValues.put(PhotoUrls.COL_REGULAR, photo.getUrls().getRegular());
        contentValues.put(PhotoUrls.COL_THUMB, photo.getUrls().getThumb());
        contentValues.put(PhotoUrls.COL_SMALL, photo.getUrls().getSmall());
        contentValues.put(Photo.COL_USER_ID, photo.getUser().getId());

        if (photo.getExif() != null) {
            contentValues.put(Exif.COL_MAKE, photo.getExif().getMake());
            contentValues.put(Exif.COL_MODEL, photo.getExif().getModel());
            contentValues.put(Exif.COL_EXPOSURE_TIME, photo.getExif().getMake());
            contentValues.put(Exif.COL_APERTURE, photo.getExif().getAperture());
            contentValues.put(Exif.COL_FOCAL_LENGTH, photo.getExif().getFocalLength());
            contentValues.put(Exif.COL_ISO, photo.getExif().getIso());
        }
        if (photo.getLocation() != null) {
            contentValues.put(PhotoLocation.COL_CITY, photo.getLocation().getCity());
            contentValues.put(PhotoLocation.COL_COUNTRY, photo.getLocation().getCountry());
            if (photo.getLocation().getPosition() != null) {
                contentValues.put(PhotoLocation.COL_LAT, photo.getLocation().getPosition().getLatitude());
                contentValues.put(PhotoLocation.COL_LON, photo.getLocation().getPosition().getLongitude());
            }
        }

        return contentValues;
    }

    public static List<ContentValues> parsePhotos(List<Photo> photos, String type) {
        List<ContentValues> contentValues = new ArrayList<>();
        for (Photo photo : photos) {
            contentValues.add(parsePhoto(photo, type));
        }

        return contentValues;
    }

    public static List<Photo> parsePhotos(Cursor cursor) {
        List<Photo> photos = new ArrayList<>();


        if (cursor.moveToNext()) {
            do {
                photos.add(parsePhoto(cursor));
            } while (cursor.moveToNext());
        }

        return photos;
    }

    public static Photo parsePhoto(Cursor cursor) {
        Photo result = new Photo();
        PhotoUrls urls = new PhotoUrls();
        UserProfile user = new UserProfile();
        Exif exif = new Exif();
        PhotoLocation location = new PhotoLocation();
        PhotoLocation.Position position = new PhotoLocation.Position();

        result.setId(cursor.getString(cursor.getColumnIndex(Photo.COL_IDENTIFIER)));
        result.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex(Photo.COL_CREATED_AT))));
        result.setUpdatedAt(new Date(cursor.getLong(cursor.getColumnIndex(Photo.COL_UPDATED_AT))));
        result.setWidth(cursor.getInt(cursor.getColumnIndex(Photo.COL_WIDTH)));
        result.setHeight(cursor.getInt(cursor.getColumnIndex(Photo.COL_HEIGHT)));
        result.setColor(cursor.getString(cursor.getColumnIndex(Photo.COL_COLOR)));
        result.setLikes(cursor.getInt(cursor.getColumnIndex(Photo.COL_LIKES)));
        result.setLikedByUser(cursor.getInt(cursor.getColumnIndex(Photo.COL_LIKED_BY_USER)) > 0);

        urls.setRaw(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_RAW)));
        urls.setFull(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_FULL)));
        urls.setRegular(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_REGULAR)));
        urls.setThumb(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_THUMB)));
        urls.setSmall(cursor.getString(cursor.getColumnIndex(PhotoUrls.COL_SMALL)));

        user.setId(cursor.getString(cursor.getColumnIndex(Photo.COL_USER_ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(UserProfile.COL_NAME)));

        exif.setMake(cursor.getString(cursor.getColumnIndex(Exif.COL_MAKE)));
        exif.setModel(cursor.getString(cursor.getColumnIndex(Exif.COL_MODEL)));
        exif.setExposureTime(cursor.getDouble(cursor.getColumnIndex(Exif.COL_EXPOSURE_TIME)));
        exif.setAperture(cursor.getDouble(cursor.getColumnIndex(Exif.COL_APERTURE)));
        exif.setFocalLength(cursor.getInt(cursor.getColumnIndex(Exif.COL_FOCAL_LENGTH)));
        exif.setIso(cursor.getInt(cursor.getColumnIndex(Exif.COL_ISO)));

        location.setCity(cursor.getString(cursor.getColumnIndex(PhotoLocation.COL_CITY)));
        location.setCountry(cursor.getString(cursor.getColumnIndex(PhotoLocation.COL_COUNTRY)));
        position.setLatitude(cursor.getDouble(cursor.getColumnIndex(PhotoLocation.COL_LAT)));
        position.setLongitude(cursor.getDouble(cursor.getColumnIndex(PhotoLocation.COL_LON)));
        location.setPosition(position);

        result.setUrls(urls);
        result.setUser(user);
        result.setExif(exif);
        result.setLocation(location);

        return result;
    }
}
