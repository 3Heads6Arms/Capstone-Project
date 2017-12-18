package com.anhhoang.zoompoint.ui.photocollection;

import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.utils.PhotosCallType;

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

    private static final String COLLECTION_ID = "CollectionIdKey";
    private static final String QUERY = "QueryKey";
    private static final String CALL_TYPE = "CallTypeKey";

    private PhotoCollectionContract.Presenter presenter;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view_photos)
    RecyclerView photosRv;
    private PhotosAdapter adapter;
    // TODO: State params

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

        photosRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new PhotosAdapter();
        photosRv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        photosRv.setAdapter(adapter);

        if (presenter == null) {
            new PhotoCollectionPresenter().attach(this);
        } else {
            presenter.attach(this);
        }

        presenter.loadPhotos(getArguments());
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void setPresenter(PhotoCollectionContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadLocalPhotos(Uri uri) {
        // TODO:
    }

    @Override
    public void updatePhotos(List<Photo> photos) {
        // TODO: remove loading
        adapter.addPhotos(photos);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(photosRv, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(int idString) {
        showError(getString(idString));
    }

    @Override
    public void toggleProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            photosRv.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            photosRv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String getToken() {
        return PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getString(getString(R.string.token_preference_key), null);
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
