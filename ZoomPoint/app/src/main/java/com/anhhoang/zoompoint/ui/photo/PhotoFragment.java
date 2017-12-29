package com.anhhoang.zoompoint.ui.photo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anhhoang.zoompoint.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhotoFragment extends Fragment {
    private static final String PHOTO_ID_KEY = "PhotoIdKey";
    private String photoId;

    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_view_photo)
    ImageView photoIv;
    @BindView(R.id.image_view_user_photo)
    ImageView userPhotoIv;
    @BindView(R.id.text_view_name)
    TextView nameTv;
    @BindView(R.id.text_view_username)
    TextView usernameTv;
    @BindView(R.id.image_view_like)
    ImageView likeIv;
    @BindView(R.id.text_view_likes)
    TextView likesTv;
    @BindView(R.id.text_view_description)
    TextView descriptionTv;
    @BindView(R.id.recycler_view_exif)
    RecyclerView exifRv;
    @BindView(R.id.fab_download)
    FloatingActionButton downloadFab;
    @BindView(R.id.fab_set_wallpaper)
    FloatingActionButton setWallpaperFab;
    @BindView(R.id.fab_add_to_collection)
    FloatingActionButton addToCollectionFab;

    public PhotoFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        checkNotNull(bundle);
        photoId = bundle.getString(PHOTO_ID_KEY);
        checkArgument(!TextUtils.isEmpty(photoId), "Photo Id must be passed as parameter");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();


        adView.loadAd(adRequest);
    }

    public static Bundle getStartingBundle(String photoId) {
        Bundle bundle = new Bundle();
        bundle.putString(PHOTO_ID_KEY, photoId);

        return bundle;
    }
}
