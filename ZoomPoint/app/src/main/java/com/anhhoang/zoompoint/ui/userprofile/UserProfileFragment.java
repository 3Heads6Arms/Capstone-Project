package com.anhhoang.zoompoint.ui.userprofile;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

import com.anhhoang.database.ZoomPointContract;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.services.CollectionsIntentService;
import com.anhhoang.zoompoint.services.UserIntentService;
import com.anhhoang.zoompoint.ui.ItemSpacingDecoration;
import com.anhhoang.zoompoint.ui.login.LoginActivity;
import com.anhhoang.zoompoint.ui.photocollection.PhotoCollectionActivity;
import com.anhhoang.zoompoint.utils.EndlessScrollListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bumptech.glide.util.Preconditions.checkArgument;
import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserProfileFragment extends Fragment implements UserProfileContract.View {
    public static final String USERNAME_KEY = "UsernameKey";
    public static final String FULL_NAME_KEY = "FullNameKey";

    private static final String RECYCLER_VIEW_POSITION = "RecyclerViewPosition";
    private static final String COLLECTIONS_KEY = "CollectionsKey";
    private static final String TWITTER_KEY = "TwitterKey";
    private static final String UNSPLASH_KEY = "UnsplashKey";
    private static final String LOCATION_KEY = "LocationKey";
    private static final String BIO_KEY = "BioKey";
    private static final int USER_LOADER_ID = 9;
    private static final int COLLECTIONS_LOADER_ID = 10;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_view_user_photo)
    ImageView userPhotoIv;
    @BindView(R.id.text_view_username)
    TextView usernameTv;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.text_view_twitter)
    TextView twitterTv;
    @BindView(R.id.text_view_unsplash)
    TextView unsplashTv;
    @BindView(R.id.text_view_location)
    TextView locationTv;
    @BindView(R.id.text_view_bio)
    TextView bioTv;
    @BindView(R.id.recycler_view_collections)
    RecyclerView collectionsRv;
    @BindView(R.id.text_view_empty)
    TextView emptyTv;
    @BindView(R.id.button_logout)
    Button logoutBtn;

    private boolean tryOnce = true;
    private UserProfileContract.Presenter presenter;
    private String username;
    private String fullName;
    private EndlessScrollListener scrollListener;
    private UserCollectionAdapter adapter;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallBack = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            refreshLayout.setRefreshing(true);
            return new CursorLoader(
                    getContext(),
                    ZoomPointContract.UserProfileEntry.CONTENT_URI,
                    null,
                    presenter.getSqlQuery(),
                    null,
                    null);
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
    private LoaderManager.LoaderCallbacks<Cursor> collectionsCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(
                    getContext(),
                    ZoomPointContract.CollectionEntry.CONTENT_URI,
                    null,
                    presenter.getSqlQueryCollections(),
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            presenter.loadCollectionsFinished(data);
            data.close();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    BroadcastReceiver userReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UserIntentService.USER_LOAD_SUCCESS)) {
                getLoaderManager().restartLoader(USER_LOADER_ID, null, loaderCallBack);
            } else if (intent.getAction().equals(UserIntentService.USER_LOAD_FAILED)) {
                Snackbar.make(getView(), R.string.unable_to_connect, Snackbar.LENGTH_LONG).show();
                getLoaderManager().restartLoader(USER_LOADER_ID, null, loaderCallBack);
            }
        }
    };

    BroadcastReceiver collectionsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CollectionsIntentService.COLLECTIONS_LOAD_SUCCESS)) {
                getLoaderManager().restartLoader(COLLECTIONS_LOADER_ID, null, collectionsCallback);
            } else if (intent.getAction().equals(CollectionsIntentService.COLLECTIONS_LOAD_FAILED)) {
                Snackbar.make(getView(), R.string.unable_to_get_collections, Snackbar.LENGTH_LONG).show();
                getLoaderManager().restartLoader(COLLECTIONS_LOADER_ID, null, collectionsCallback);
            }
        }
    };

    public UserProfileFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        checkNotNull(args);

        username = args.getString(USERNAME_KEY);
        fullName = args.getString(FULL_NAME_KEY);

        checkArgument(!TextUtils.isEmpty(username), "Username cannot be null or empty");
        checkArgument(!TextUtils.isEmpty(fullName), "fullname cannot be null or empty");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);

        toolbar.setTitle(fullName);
        usernameTv.setText("@" + username);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                collectionsRv.removeOnScrollListener(scrollListener);
                collectionsRv.addOnScrollListener(scrollListener);
                presenter.loadProfile();
            }
        });


        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL);
        scrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                presenter.loadMore();
            }
        };
        adapter = new UserCollectionAdapter(new UserCollectionAdapter.OnCollectionClickListener() {
            @Override
            public void onCollectionClicked(PhotoCollection collection) {
                presenter.collectionSelected(collection);
            }
        });

        collectionsRv.setLayoutManager(layoutManager);
        collectionsRv.addOnScrollListener(scrollListener);
        collectionsRv.addItemDecoration(new ItemSpacingDecoration((int) getResources().getDimension(R.dimen.grid_item_padding), true));
        collectionsRv.setAdapter(adapter);


        String myUsername = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(getString(R.string.username_preference_key), null);

        if (username.equals(myUsername)) {
            logoutBtn.setVisibility(View.VISIBLE);
        }

        if (savedInstanceState != null) {
            List<PhotoCollection> collections = savedInstanceState.getParcelableArrayList(COLLECTIONS_KEY);

            if (collections.size() < UserProfilePresenter.PAGE_SIZE) {
                collectionsRv.removeOnScrollListener(scrollListener);
            }
            adapter.updateCollections(collections);

            collectionsRv.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLER_VIEW_POSITION));

            twitterTv.setText(savedInstanceState.getCharSequence(TWITTER_KEY));
            unsplashTv.setText(savedInstanceState.getCharSequence(UNSPLASH_KEY));
            locationTv.setText(savedInstanceState.getCharSequence(LOCATION_KEY));
            bioTv.setText(savedInstanceState.getCharSequence(BIO_KEY));
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.attach(this);
        } else {
            new UserProfilePresenter(username).attach(this);

            getLoaderManager().restartLoader(USER_LOADER_ID, null, loaderCallBack);
            getLoaderManager().restartLoader(COLLECTIONS_LOADER_ID, null, collectionsCallback);
        }

        IntentFilter userIntentFilter = new IntentFilter();
        userIntentFilter.addAction(UserIntentService.USER_LOAD_SUCCESS);
        userIntentFilter.addAction(UserIntentService.USER_LOAD_FAILED);
        IntentFilter collectionsIntentFilter = new IntentFilter();
        collectionsIntentFilter.addAction(CollectionsIntentService.COLLECTIONS_LOAD_SUCCESS);
        collectionsIntentFilter.addAction(CollectionsIntentService.COLLECTIONS_LOAD_FAILED);

        getActivity().registerReceiver(userReceiver, userIntentFilter);
        getActivity().registerReceiver(collectionsReceiver, collectionsIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(userReceiver);
        getActivity().unregisterReceiver(collectionsReceiver);
        presenter.detach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(COLLECTIONS_KEY, new ArrayList<>(adapter.getCollections()));
        outState.putParcelable(RECYCLER_VIEW_POSITION, collectionsRv.getLayoutManager().onSaveInstanceState());
        outState.putCharSequence(TWITTER_KEY, twitterTv.getText());
        outState.putCharSequence(UNSPLASH_KEY, unsplashTv.getText());
        outState.putCharSequence(LOCATION_KEY, locationTv.getText());
        outState.putCharSequence(BIO_KEY, bioTv.getText());
    }

    @OnClick(R.id.button_logout)
    public void onLogoutClicked() {
        ContentResolver contentResolver = getContext().getContentResolver();
        contentResolver.delete(ZoomPointContract.PhotoEntry.CONTENT_URI, null, null);
        contentResolver.delete(ZoomPointContract.CollectionEntry.CONTENT_URI, null, null);
        contentResolver.delete(ZoomPointContract.UserProfileEntry.CONTENT_URI, null, null);

        PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit()
                .remove(getString(R.string.token_preference_key))
                .remove(getString(R.string.username_preference_key))
                .apply();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void setPresenter(UserProfileContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayProfilePicture(String url) {
        Glide.with(getContext())
                .asDrawable()
                .load(url)
                .apply(new RequestOptions()
                        .error(R.drawable.user_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .skipMemoryCache(true))
                .transition(new DrawableTransitionOptions().crossFade())
                .into(userPhotoIv);
    }

    @Override
    public void displayProfile(UserProfile profile) {
        unsplashTv.setText("https://unsplash.com/" + profile.getUsername());
        if (!TextUtils.isEmpty(profile.getTwitter())) twitterTv.setText(profile.getTwitter());
        if (!TextUtils.isEmpty(profile.getLocation())) locationTv.setText(profile.getLocation());
        if (!TextUtils.isEmpty(profile.getBio())) bioTv.setText(profile.getBio());
    }

    @Override
    public void displayCollections(List<PhotoCollection> collections) {
        if (adapter.getItemCount() >= collections.size()) {
            collectionsRv.removeOnScrollListener(scrollListener);
        }
        adapter.updateCollections(collections);

        if (adapter.getCollections().size() > 0) {
            emptyTv.setVisibility(View.INVISIBLE);
            collectionsRv.setVisibility(View.VISIBLE);
        } else {
            tryOnce();
        }

        tryOnce = false;
    }

//    @Override
//    public void clearCollections() {
//        adapter.clear();
//    }

    @Override
    public void toggleProgress(boolean show) {
        refreshLayout.setRefreshing(show);
    }

    @Override
    public void showEmptyCollection() {
        collectionsRv.setVisibility(View.INVISIBLE);
        emptyTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void openCollection(int id, String collectionName) {
        Intent intent = PhotoCollectionActivity.getStartingIntent(getContext(), id, collectionName);
        startActivity(intent);
    }

    @Override
    public void startLoadingUser(String username) {
        Intent intent = UserIntentService.getStartingIntent(getContext(), username);
        getActivity().startService(intent);
    }

    @Override
    public void startLoadingCollections(String username, int page) {
        Intent intent = CollectionsIntentService.getStartingIntent(getContext(), username, page);
        getActivity().startService(intent);
    }

    public static Bundle getStartingBundle(String username, String fullName) {
        Bundle args = new Bundle();
        args.putString(USERNAME_KEY, username);
        args.putString(FULL_NAME_KEY, fullName);

        return args;
    }

    private void tryOnce() {
        if (tryOnce) {
            collectionsRv.removeOnScrollListener(scrollListener);
            collectionsRv.addOnScrollListener(scrollListener);
            presenter.loadProfile();
        }

        tryOnce = false;
    }
}
