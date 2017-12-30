package com.anhhoang.zoompoint.ui.photos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhhoang.database.ZoomPointContract;
import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.services.PhotosIntentService;
import com.anhhoang.zoompoint.ui.ItemSpacingDecoration;
import com.anhhoang.zoompoint.ui.photo.PhotoActivity;
import com.anhhoang.zoompoint.ui.userprofile.UserProfileActivity;
import com.anhhoang.zoompoint.utils.EndlessScrollListener;
import com.anhhoang.zoompoint.utils.PhotosCallType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhotosFragment extends Fragment implements PhotosContract.View {
    public static final String TAG = PhotosFragment.class.getCanonicalName();

    private static final String PHOTOS_ITEMS = "PhotoItems";
    private static final String RECYCLER_VIEW_POSITION = "RecyclerViewPosition";
    private static final String COLLECTION_ID = "CollectionIdKey";
    private static final String QUERY = "QueryKey";
    private static final String CALL_TYPE = "CallTypeKey";
    private static final int PHOTO_LOADER = 1;

    private PhotosContract.Presenter presenter;
    private PhotosAdapter adapter;
    private boolean tryOnce = true;

    @BindView(R.id.recycler_view_photos)
    RecyclerView photosRv;
    @BindView(R.id.text_view_error)
    TextView errorTv;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(PhotosIntentService.PHOTOS_LOAD_SUCCESS)) {
                getLoaderManager().restartLoader(PHOTO_LOADER, null, loaderCallbacks);
            } else if (intent.getAction().equals(PhotosIntentService.PHOTOS_LOAD_FAILED)) {
                Snackbar.make(getView(), R.string.unable_to_get_photos, Snackbar.LENGTH_LONG).show();
                getLoaderManager().restartLoader(PHOTO_LOADER, null, loaderCallbacks);
            }
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            String query = presenter.getSqlSelection();
            checkNotNull(query, "Query cannot be null");
            refreshLayout.setRefreshing(true);

            return new CursorLoader(
                    getContext(),
                    ZoomPointContract.PhotoEntry.CONTENT_URI,
                    null,
                    query,
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
    private PhotosAdapter.OnUserClickListener userClickListener = new PhotosAdapter.OnUserClickListener() {
        @Override
        public void onUserClicked(UserProfile userProfile) {
            presenter.onUserSelected(userProfile);
        }
    };
    private RecyclerView.OnScrollListener endlessScrollListener;
    private PhotosAdapter.OnPhotoClickListener photoClickListener = new PhotosAdapter.OnPhotoClickListener() {
        @Override
        public void onPhotoClicked(String photoId, String photoType) {
            presenter.onPhotoSelected(photoId, photoType);
        }
    };
    private PhotosCallType callType;

    public PhotosFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        checkNotNull(bundle, "Missing bundle!");

        callType = (PhotosCallType) bundle.getSerializable(CALL_TYPE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_collection, container, false);
        ButterKnife.bind(this, view);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        endlessScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                presenter.loadMore();
            }
        };
        adapter = new PhotosAdapter(userClickListener, photoClickListener);

        photosRv.setLayoutManager(layoutManager);
        photosRv.setAdapter(adapter);
        photosRv.addItemDecoration(new ItemSpacingDecoration((int) getResources().getDimension(R.dimen.grid_item_padding), false));
        photosRv.addOnScrollListener(endlessScrollListener);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.load();
                // Re-enable onscrolllistener in case there are need
                photosRv.removeOnScrollListener(endlessScrollListener);
                photosRv.addOnScrollListener(endlessScrollListener);
            }
        });

        if (savedInstanceState != null) {
            List<Photo> photos = savedInstanceState.getParcelableArrayList(PHOTOS_ITEMS);
            adapter.addPhotos(photos);
            photosRv.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLER_VIEW_POSITION));
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (presenter == null) {
            new PhotosPresenter(getArguments()).attach(this);
            if (callType != PhotosCallType.SEARCH_PHOTOS) {
                getLoaderManager().restartLoader(PHOTO_LOADER, null, loaderCallbacks);
            } else {
                presenter.load();
            }
        } else {
            presenter.attach(this);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PhotosIntentService.PHOTOS_LOAD_SUCCESS);
        intentFilter.addAction(PhotosIntentService.PHOTOS_LOAD_FAILED);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().unregisterReceiver(broadcastReceiver);

        presenter.detach();

        if (callType == PhotosCallType.SEARCH_PHOTOS) {
            getContext().getContentResolver()
                    .delete(ZoomPointContract.PhotoEntry.CONTENT_URI, presenter.getSqlSelection(), null);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(PHOTOS_ITEMS, new ArrayList<Photo>(adapter.getPhotos()));
        outState.putParcelable(RECYCLER_VIEW_POSITION, photosRv.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void setPresenter(PhotosContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayPhotos(List<Photo> photos) {
        if (photos.size() <= adapter.getItemCount()) {
            photosRv.removeOnScrollListener(endlessScrollListener);
        }
        adapter.addPhotos(photos);
        if (adapter.getPhotos().size() > 0) {
            errorTv.setVisibility(View.INVISIBLE);
            photosRv.setVisibility(View.VISIBLE);
        } else {
            tryOnce();
        }

        tryOnce = false;
    }

    @Override
    public void toggleProgress(boolean show) {
        if (show) {
            refreshLayout.setRefreshing(true);
        } else {
            refreshLayout.setRefreshing(false);
        }

        errorTv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showEmpty() {
        errorTv.setVisibility(View.VISIBLE);
        photosRv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void openUser(String username, String fullname) {
        Intent intent = UserProfileActivity.getStartingIntent(getContext(), username, fullname);
        startActivity(intent);
    }

    @Override
    public void openPhoto(String photoId, String photoType) {
        startActivity(PhotoActivity.getStartingIntent(getContext(), photoId, photoType));
    }

    @Override
    public void startLoading(long collectionId, PhotosCallType callType, String query, int currentPage) {
        Intent intent = PhotosIntentService.getStartingIntent(getContext(), collectionId, callType, query, currentPage);
        getActivity().startService(intent);
    }

    public static Bundle getStartingBundle(long collectionId) {
        checkArgument(collectionId > 0, "Invalid collection Id, should be greater than 0");

        Bundle bundle = new Bundle();
        bundle.putLong(COLLECTION_ID, collectionId);
        bundle.putSerializable(CALL_TYPE, PhotosCallType.COLLECTION_PHOTOS);

        return bundle;
    }

    public static Bundle getStartingBundle(PhotosCallType callType, String query) {
        checkArgument(!TextUtils.isEmpty(query), "Invalid query, mustn't be null or empty");

        Bundle bundle = new Bundle();
        bundle.putString(QUERY, query);
        bundle.putSerializable(CALL_TYPE, callType);

        return bundle;
    }

    private void tryOnce() {
        if (tryOnce) {
            presenter.load();
            // Re-enable onscrolllistener in case there are need
            photosRv.removeOnScrollListener(endlessScrollListener);
            photosRv.addOnScrollListener(endlessScrollListener);
        }
        tryOnce = false;
    }
}
