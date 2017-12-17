package com.anhhoang.zoompoint.ui.photocollection;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anhhoang.zoompoint.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhotoCollectionFragment extends Fragment {
    public static final String TAG = PhotoCollectionFragment.class.getCanonicalName();

    public PhotoCollectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_collection, container, false);
    }
}
