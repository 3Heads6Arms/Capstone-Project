package com.anhhoang.zoompoint.ui.home.collections;


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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhhoang.database.ZoomPointContract;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.services.CollectionsIntentService;
import com.anhhoang.zoompoint.ui.ItemSpacingDecoration;
import com.anhhoang.zoompoint.ui.photocollection.PhotoCollectionActivity;
import com.anhhoang.zoompoint.utils.EndlessScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionsFragment extends Fragment implements CollectionsContract.View {
    private static final int COLLECTION_LOADER_ID = 2;
    private static final String COLLECTION_ITEMS = "CollectionItems";
    private static final String RECYCLER_VIEW_POSITION = "RecyclerViewPosition";

    private boolean tryOnce = true;
    private CollectionsContract.Presenter presenter;
    private CollectionAdapter adapter;
    private RecyclerView.OnScrollListener endlessScrollListener;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view_collections)
    RecyclerView collectionsRv;
    @BindView(R.id.text_view_error)
    TextView errorTv;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            refreshLayout.setRefreshing(true);
            return new CursorLoader(
                    getContext(),
                    ZoomPointContract.CollectionEntry.CONTENT_URI,
                    null,
                    presenter.getSqlQuery(),
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            presenter.loadFinished(cursor);
            cursor.close();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CollectionsIntentService.COLLECTIONS_LOAD_SUCCESS)) {
                getLoaderManager().restartLoader(COLLECTION_LOADER_ID, null, loaderCallbacks);
            } else if (intent.getAction().equals(CollectionsIntentService.COLLECTIONS_LOAD_FAILED)) {
                Snackbar.make(getView(), R.string.unable_to_get_collections, Snackbar.LENGTH_LONG).show();
                getLoaderManager().restartLoader(COLLECTION_LOADER_ID, null, loaderCallbacks);
            }
        }
    };

    public CollectionsFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collections, container, false);
        ButterKnife.bind(this, view);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        adapter = new CollectionAdapter(new CollectionAdapter.OnCollectionItemClickListener() {
            @Override
            public void onCollectionItemClicked(PhotoCollection collection) {
                presenter.collectionSelected(collection);
            }
        });
        endlessScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                collectionsRv.removeOnScrollListener(endlessScrollListener);
                collectionsRv.addOnScrollListener(endlessScrollListener);
                presenter.loadMore();
            }
        };

        collectionsRv.setLayoutManager(layoutManager);
        collectionsRv.addOnScrollListener(endlessScrollListener);
        collectionsRv.setAdapter(adapter);
        collectionsRv.addItemDecoration(new ItemSpacingDecoration(
                (int) getResources().getDimension(R.dimen.grid_item_padding), false
        ));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.load();
                collectionsRv.removeOnScrollListener(endlessScrollListener);
                collectionsRv.addOnScrollListener(endlessScrollListener);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (presenter != null) {
            presenter.attach(this);
        } else {
            new CollectionsPresenter().attach(this);
            getLoaderManager()
                    .restartLoader(COLLECTION_LOADER_ID, null, loaderCallbacks);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CollectionsIntentService.COLLECTIONS_LOAD_SUCCESS);
        intentFilter.addAction(CollectionsIntentService.COLLECTIONS_LOAD_FAILED);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);
        presenter.detach();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            List<PhotoCollection> collections = savedInstanceState.getParcelableArrayList(COLLECTION_ITEMS);
            adapter.addCollections(collections);
            collectionsRv.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLER_VIEW_POSITION));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(COLLECTION_ITEMS, new ArrayList<>(adapter.getCollections()));
        outState.putParcelable(RECYCLER_VIEW_POSITION, collectionsRv.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void setPresenter(CollectionsContract.Presenter presenter) {
        this.presenter = presenter;
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
        collectionsRv.setVisibility(View.INVISIBLE);
        errorTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void openCollection(long id, String collectionName) {
        Intent intent = PhotoCollectionActivity.getStartingIntent(getContext(), id, collectionName);
        startActivity(intent);
    }

    @Override
    public void displayCollections(List<PhotoCollection> collections) {
        if (adapter.getItemCount() >= collections.size()) {
            collectionsRv.removeOnScrollListener(endlessScrollListener);
        }
        adapter.addCollections(collections);

        if (adapter.getCollections().size() > 0) {
            collectionsRv.setVisibility(View.VISIBLE);
            errorTv.setVisibility(View.INVISIBLE);
        } else {
            tryOnce();
        }

        tryOnce = false;
    }

    @Override
    public void startLoading(int currentPage) {
        Intent intent = CollectionsIntentService.getStartingIntent(getContext(), currentPage);
        getActivity().startService(intent);
    }

    private void tryOnce() {
        if (tryOnce) {
            presenter.load();
            collectionsRv.removeOnScrollListener(endlessScrollListener);
            collectionsRv.addOnScrollListener(endlessScrollListener);
        }

        tryOnce = false;
    }
}
