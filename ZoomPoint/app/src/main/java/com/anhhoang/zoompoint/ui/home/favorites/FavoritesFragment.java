package com.anhhoang.zoompoint.ui.home.favorites;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.ui.photocollection.PhotoCollectionFragment;
import com.anhhoang.zoompoint.utils.PhotosCallType;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {
    private static final String TAG = FavoritesFragment.class.getCanonicalName();

    private boolean isRestoringScreen;

    public FavoritesFragment() {
        // Required empty public constructor
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isRestoringScreen = savedInstanceState != null;

        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isRestoringScreen) {
            String username = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.username_preference_key), null);
            Fragment fragment = new PhotoCollectionFragment();
            fragment.setArguments(PhotoCollectionFragment.createBundle(PhotosCallType.LIKED_PHOTOS, username));
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_favorite_photos, fragment, TAG)
                    .commit();
        }
    }
}
