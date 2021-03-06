package com.anhhoang.zoompoint.ui.photo;

import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anhhoang.database.ZoomPointContract;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.ui.ItemSpacingDecoration;
import com.anhhoang.zoompoint.ui.map.MapActivity;
import com.anhhoang.zoompoint.ui.userprofile.UserProfileActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhotoFragment extends Fragment implements PhotoContract.View {
    private static final String PHOTO_ID_KEY = "PhotoIdKey";
    private static final int PHOTO_LOADER_ID = 42;
    private static final String PHOTO_TYPE = "PhotoTypeKey";

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
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
    @BindView(R.id.text_view_location)
    TextView locationTv;
    @BindView(R.id.image_view_location)
    ImageView locationIv;
    @BindView(R.id.image_view_like)
    AppCompatImageView likeIv;
    @BindView(R.id.text_view_likes)
    TextView likesTv;
    @BindView(R.id.text_view_description)
    TextView descriptionTv;
    @BindView(R.id.recycler_view_exif)
    RecyclerView exifRv;
    @BindView(R.id.text_view_empty_exif)
    TextView emptyExifTv;
    @BindView(R.id.button_add_to_collection)
    Button addtoCollectionBtn;
    @BindView(R.id.button_set_wallpaper)
    Button setWallpaperBtn;
    @BindView(R.id.button_download)
    Button downloadBtn;
    @BindView(R.id.hiddenPhotoImmersive)
    ImageView hiddenPhotoImmersive;

    private String photoId;
    private String photoType;
    private ExifAdapter adapter;
    private PhotoContract.Presenter presenter;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri queryUri = ZoomPointContract.PhotoEntry.buildItemUri(photoId);
            return new CursorLoader(
                    getContext(),
                    queryUri,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            presenter.loadFinished(data);
            data.close();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
    private View.OnClickListener locationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onLocationSelected();
        }
    };

    public PhotoFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        checkNotNull(bundle);
        photoId = bundle.getString(PHOTO_ID_KEY);
        photoType = bundle.getString(PHOTO_TYPE);
        checkArgument(!TextUtils.isEmpty(photoId), "Photo Id must be passed as parameter");
        checkArgument(!TextUtils.isEmpty(photoType), "Photo Type must be passed as parameter");
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

        Drawable addCollectionIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_add_collection);
        addCollectionIcon = DrawableCompat.wrap(addCollectionIcon);
        addCollectionIcon.mutate();
        DrawableCompat.setTint(addCollectionIcon, ContextCompat.getColor(getContext(), R.color.colorAccent));
        addtoCollectionBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, addCollectionIcon, null, null);

        Drawable downloadIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_file_download);
        downloadIcon = DrawableCompat.wrap(downloadIcon);
        downloadIcon.mutate();
        DrawableCompat.setTint(downloadIcon, ContextCompat.getColor(getContext(), R.color.colorAccent));
        downloadBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, downloadIcon, null, null);

        Drawable setWallpaperIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_wallpaper);
        setWallpaperIcon = DrawableCompat.wrap(setWallpaperIcon);
        setWallpaperIcon.mutate();
        DrawableCompat.setTint(setWallpaperIcon, ContextCompat.getColor(getContext(), R.color.colorAccent));
        setWallpaperBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, setWallpaperIcon, null, null);

        locationTv.setOnClickListener(locationClickListener);
        locationIv.setOnClickListener(locationClickListener);
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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.load(photoId, photoType);
            }
        });
        likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLikeButtonSelected();
            }
        });
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDownloadSelected(getContext());
            }
        });
        setWallpaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSetWallpaperSelected();
            }
        });
        addtoCollectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddToCollectionSelected();
            }
        });

        adapter = new ExifAdapter();
        exifRv.addItemDecoration(new ItemSpacingDecoration(
                (int) getResources().getDimension(R.dimen.grid_item_padding),
                false));
        exifRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        exifRv.setAdapter(adapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.attach(this);
            getLoaderManager().restartLoader(PHOTO_LOADER_ID, null, loaderCallback);
        } else {
            new PhotoPresenter().attach(this);
            presenter.load(photoId, photoType);
        }

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();


        adView.loadAd(adRequest);

    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void setPresenter(PhotoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void toggleProgress(boolean show) {
        refreshLayout.setRefreshing(show);
    }

    @Override
    public void displayPhotoImage(String url) {
        Glide.with(this)
                .asDrawable()
                .load(url)
                .apply(new RequestOptions()
                        .error(R.drawable.ic_image_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .skipMemoryCache(true))
                .transition(new DrawableTransitionOptions().crossFade())
                .into(photoIv);

        Glide.with(this)
                .asDrawable()
                .load(url)
                .apply(new RequestOptions()
                        .error(R.drawable.ic_image_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .skipMemoryCache(true))
                .transition(new DrawableTransitionOptions().crossFade())
                .into(hiddenPhotoImmersive);

        hiddenPhotoImmersive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenPhotoImmersive.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
                getActivity()
                        .getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });

        photoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenPhotoImmersive.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
                getActivity()
                        .getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
        });
    }

    @Override
    public void displayUser(String name, String username, String profileImageUrl) {
        Glide.with(this)
                .asDrawable()
                .load(profileImageUrl)
                .apply(new RequestOptions()
                        .error(R.drawable.ic_image_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .skipMemoryCache(true))
                .transition(new DrawableTransitionOptions().crossFade())
                .into(userPhotoIv);

        nameTv.setText(name);
        usernameTv.setText("@" + username);


        View.OnClickListener userClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onUserSelected();
            }
        };

        photoIv.setContentDescription(getString(R.string.photo_content_description, name));
        hiddenPhotoImmersive.setContentDescription(getString(R.string.photo_content_description, name));

        userPhotoIv.setOnClickListener(userClickListener);
        nameTv.setOnClickListener(userClickListener);
        usernameTv.setOnClickListener(userClickListener);
    }

    @Override
    public void displayLikes(boolean likedByUser, int likes) {
        if (likedByUser) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_white_24dp);
            drawable = DrawableCompat.wrap(drawable);
            drawable.mutate();
            DrawableCompat.setTint(drawable, Color.RED);
            likeIv.setImageDrawable(drawable);
        } else {
            likeIv.setImageResource(R.drawable.ic_favorite_border);
        }
        likesTv.setText(getResources().getQuantityString(R.plurals.likes, likes, likes));
    }

    @Override
    public void displayDescription(String description) {
        descriptionTv.setText(description);
    }

    @Override
    public void displayExif(List<Pair> items) {
        adapter.updateExifs(items);
        if (items.size() > 0) {
            emptyExifTv.setVisibility(View.GONE);
            exifRv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayLocation(String location) {
        locationTv.setText(location);
    }

    @Override
    public void showError(int idErrorString) {
        Snackbar.make(getView(), idErrorString, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyExif() {
        emptyExifTv.setVisibility(View.VISIBLE);
        exifRv.setVisibility(View.GONE);
    }

    @Override
    public void displayCollections(List<Pair> collections) {
        final CollectionPickerDialog dialog = new CollectionPickerDialog();
        dialog.setArguments(CollectionPickerDialog.getStartingBundle(collections));
        dialog.setCollectionSelectedListener(new CollectionPickerDialog.CollectionSelectedListener() {
            @Override
            public void onCollectionSelected(int collectionId) {
                presenter.onCollectionSelected(collectionId, photoId);
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(), CollectionPickerDialog.TAG);
    }

    @Override
    public String getToken() {
        return PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getString(getString(R.string.token_preference_key), null);
    }

    @Override
    public void openUser(String username, String fullname) {
        Intent intent = UserProfileActivity.getStartingIntent(getContext(), username, fullname);
        startActivity(intent);
    }

    @Override
    public void updatePhoto(ContentValues photo) {
        // Insert will override old values due to conflict with unique identifier
        getContext()
                .getContentResolver()
                .insert(ZoomPointContract.PhotoEntry.CONTENT_URI, photo);
    }

    @Override
    public void openLocation(double lat, double lng) {
        Intent intent = MapActivity.getStartingIntent(getContext(), lat, lng);
        startActivity(intent);
    }

    @Override
    public void loadPhotoFromLocalDb() {
        getLoaderManager()
                .restartLoader(PHOTO_LOADER_ID, null, loaderCallback);
    }

    @Override
    public void setWallpaper(String url) {
        Glide.with(this)
                .asBitmap()
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        showError(R.string.unable_to_get_photo);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getActivity().getApplicationContext());

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final Context context = getActivity().getApplicationContext();
                                try {
                                    wallpaperManager.setBitmap(resource);

                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, R.string.wallpaper_is_set, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } catch (IOException e) {

                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, R.string.unable_set_wallpaper, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                        return true;
                    }
                })
                .load(url)
                .submit();

    }

    @Override
    public String getMyUsername() {
        return PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getString(getString(R.string.username_preference_key), null);
    }

    public static Bundle getStartingBundle(String photoId, String photoType) {
        Bundle bundle = new Bundle();
        bundle.putString(PHOTO_ID_KEY, photoId);
        bundle.putString(PHOTO_TYPE, photoType);

        return bundle;
    }

    public static class CollectionPickerDialog extends DialogFragment {
        interface CollectionSelectedListener {
            void onCollectionSelected(int collectionId);
        }

        public static final String TAG = CollectionPickerDialog.class.getCanonicalName();
        private RecyclerView recyclerView;
        private CollectionSelectedListener collectionSelected;

        public void setCollectionSelectedListener(CollectionSelectedListener listener) {
            collectionSelected = listener;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.collection_picker_layout, container, false);

            List<Pair> collections = getArguments().getParcelableArrayList("collections");
            recyclerView = view.findViewById(R.id.recycler_view_collections);
            recyclerView.setAdapter(new CollectionPickerAdapter(collections));

            return view;
        }

        public static Bundle getStartingBundle(List<Pair> collections) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("collections", new ArrayList<>(collections));

            return bundle;
        }


        class CollectionPickerAdapter extends RecyclerView.Adapter<CollectionPickerAdapter.ViewHolder> {

            private final List<Pair> items;

            CollectionPickerAdapter(List<Pair> items) {
                this.items = items;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.collection_picker_item_view, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                Pair pair = items.get(position);
                holder.textView.setTag(pair);
                holder.textView.setText(pair.value);
            }

            @Override
            public int getItemCount() {
                return items.size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder {
                TextView textView;

                public ViewHolder(View itemView) {
                    super(itemView);
                    textView = itemView.findViewById(R.id.textView);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (collectionSelected != null) {
                                Pair pair = (Pair) textView.getTag();
                                collectionSelected.onCollectionSelected(pair.id);
                            }
                        }
                    });
                }
            }
        }
    }
}
