package com.anhhoang.zoompoint.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.anhhoang.database.ZoomPointContract;
import com.anhhoang.unsplashapi.UnsplashApi;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.utils.UserUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserIntentService extends IntentService {
    public static final String USER_LOAD_FAILED = "USER_LOAD_FAILED";
    public static final String USER_LOAD_SUCCESS = "USER_LOAD_SUCCESS";
    private static final String USERNAME = "USERNAME";
    private Callback<UserProfile> userCallback = new Callback<UserProfile>() {
        @Override
        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
            if (response.code() == HttpURLConnection.HTTP_OK) {
                ContentValues contentValues = UserUtils.parseUser(response.body());
                getContentResolver()
                        .insert(ZoomPointContract.UserProfileEntry.CONTENT_URI, contentValues);

                sendBroadcast(new Intent(USER_LOAD_SUCCESS));
            } else {
                sendBroadcast(new Intent(USER_LOAD_FAILED));
            }
        }

        @Override
        public void onFailure(Call<UserProfile> call, Throwable t) {
            sendBroadcast(new Intent(USER_LOAD_FAILED));
        }
    };


    private String username;


    public UserIntentService() {
        super("UserIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String token = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(getString(R.string.token_preference_key), null);
            UnsplashApi unsplashApi = UnsplashApi.getInstance(token);

            username = intent.getStringExtra(USERNAME);

            unsplashApi.getUserProfile(username).enqueue(userCallback);
        }
    }

    public static Intent getStartingIntent(Context context, String username) {
        Intent intent = new Intent(context, UserIntentService.class);
        intent.putExtra(USERNAME, username);

        return intent;
    }
}
