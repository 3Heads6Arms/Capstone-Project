package com.anhhoang.zoompoint.ui.photocollection;

import android.content.ContentValues;
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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhhoang.database.ZoomPointContract;
import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.ui.ItemSpacingDecoration;
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
public class PhotoCollectionFragment extends Fragment implements PhotoCollectionContract.View {
    public static final String TAG = PhotoCollectionFragment.class.getCanonicalName();

    private static final String PHOTOS_ITEMS = "PhotoItems";
    private static final String RECYCLER_VIEW_POSITION = "RecyclerViewPosition";
    private static final String COLLECTION_ID = "CollectionIdKey";
    private static final String QUERY = "QueryKey";
    private static final String CALL_TYPE = "CallTypeKey";
    private static final int PHOTO_LOADER = 1;

    private PhotoCollectionContract.Presenter presenter;

    @BindView(R.id.recycler_view_photos)
    RecyclerView photosRv;
    @BindView(R.id.text_view_error)
    TextView errorTv;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private PhotosAdapter adapter;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            checkNotNull(args);

            String query = args.getString("query");
            checkNotNull(query, "Query cannot be null");

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

    public PhotoCollectionFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        checkNotNull(bundle, "Missing bundle!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_collection, container, false);
        ButterKnife.bind(this, view);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.load(getArguments());
            }
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        adapter = new PhotosAdapter();
        photosRv.setLayoutManager(layoutManager);
        photosRv.setAdapter(adapter);
        photosRv.addItemDecoration(new ItemSpacingDecoration(
                (int) getResources().getDimension(R.dimen.grid_item_padding)
        ));

        photosRv.addOnScrollListener(new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                presenter.loadMore();
            }
        });

        if (presenter == null) {
            new PhotoCollectionPresenter().attach(this);
        } else {
            presenter.attach(this);
        }

        if (savedInstanceState != null) {
            List<Photo> photos = savedInstanceState.getParcelableArrayList(PHOTOS_ITEMS);
            adapter.addPhotos(photos);
            photosRv.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLER_VIEW_POSITION));
        } else {
            presenter.load(getArguments());
        }

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(PHOTOS_ITEMS, new ArrayList<Photo>(adapter.getPhotos()));
        outState.putParcelable(RECYCLER_VIEW_POSITION, photosRv.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void setPresenter(PhotoCollectionContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadLocalPhotos(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        getLoaderManager().restartLoader(PHOTO_LOADER, bundle, loaderCallbacks);
    }

    @Override
    public void displayPhotos(List<Photo> photos) {
        adapter.addPhotos(photos);
    }

    @Override
    public void clearPhotos() {
        adapter.clearPhotos();
    }

    @Override
    public void showError(int idString) {
        Snackbar.make(photosRv, idString, Snackbar.LENGTH_LONG).show();
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
    public String getToken() {
        return PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getString(getString(R.string.token_preference_key), null);
    }

    @Override
    public void savePhotos(final ContentValues[] photos) {
        getContext().getContentResolver().bulkInsert(ZoomPointContract.PhotoEntry.CONTENT_URI, photos);
    }


    @Override
    public void saveUsers(final ContentValues[] users) {
        getContext().getContentResolver().bulkInsert(ZoomPointContract.UserProfileEntry.CONTENT_URI, users);
    }

    @Override
    public void removePhotos(String query) {
        getContext()
                .getContentResolver()
                .delete(ZoomPointContract.PhotoEntry.CONTENT_URI, query, null);
    }

    @Override
    public void showEmpty(boolean isError, int errorId) {
        if (isError) {
            errorTv.setText(errorId);
        } else {
            errorTv.setText(R.string.no_photos_here);
        }

        errorTv.setVisibility(View.VISIBLE);
        refreshLayout.setRefreshing(false);
        photosRv.setVisibility(View.INVISIBLE);
    }

    public static Bundle createBundle(long collectionId) {
        checkArgument(collectionId > 0, "Invalid collection Id, should be greater than 0");

        Bundle bundle = new Bundle();
        bundle.putLong(COLLECTION_ID, collectionId);

        return bundle;
    }

    public static Bundle createBundle(PhotosCallType callType, String query) {
        checkArgument(!TextUtils.isEmpty(query), "Invalid query, mustn't be null or empty");

        Bundle bundle = new Bundle();
        bundle.putString(QUERY, query);
        bundle.putSerializable(CALL_TYPE, callType);

        return bundle;
    }
}
