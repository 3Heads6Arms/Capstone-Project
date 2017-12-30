package com.anhhoang.zoompoint.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.anhhoang.database.ZoomPointContract;
import com.anhhoang.unsplashapi.UnsplashApi;
import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.ui.photo.PhotoActivity;
import com.anhhoang.zoompoint.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;

import java.net.HttpURLConnection;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class RandomFeaturedWidget extends AppWidgetProvider {


    static void updateAppWidget(final Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.random_featured_widget);

        UnsplashApi unsplashApi = getUnsplashApi(context);
        unsplashApi.getRandomPhoto(true)
                .enqueue(new Callback<Photo>() {
                    @Override
                    public void onResponse(Call<Photo> call, Response<Photo> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            Photo photo = response.body();
                            ContentValues contentValues = PhotoUtils.parsePhoto(photo, "random_featured");
                            context.getContentResolver()
                                    .insert(ZoomPointContract.PhotoEntry.CONTENT_URI, contentValues);
                            setData(photo, context, views);
                        } else {
                            Photo photo = getRandomPhotoFromDb(context);
                            setData(photo, context, views);
                        }
                    }

                    @Override
                    public void onFailure(Call<Photo> call, Throwable t) {
                        Photo photo = getRandomPhotoFromDb(context);
                        setData(photo, context, views);
                    }
                });

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void setData(final Photo photo, final Context context, final RemoteViews views) {
        if (photo != null) {
            ComponentName widgetName = new ComponentName(context, RandomFeaturedWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] widgetIds = manager.getAppWidgetIds(widgetName);

            AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, R.id.image_view_photo, views, widgetIds);
            Glide.with(context)
                    .asBitmap()
                    .load(photo.getUrls().getRegular())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .skipMemoryCache(true))
                    .into(appWidgetTarget);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    PhotoActivity.getStartingIntent(context, photo.getId()),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.image_view_photo, pendingIntent);

            manager.updateAppWidget(widgetName, views);
        }
    }

    private static Photo getRandomPhotoFromDb(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        ZoomPointContract.PhotoEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

        if (cursor.getCount() > 0) {
            Random random = new Random();
            int index = random.nextInt(cursor.getCount());
            cursor.moveToPosition(index);
            return PhotoUtils.parsePhoto(cursor);
        }

        return null;
    }

    private static UnsplashApi getUnsplashApi(Context context) {
        String token = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.token_preference_key), null);

        return UnsplashApi.getInstance(token);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

