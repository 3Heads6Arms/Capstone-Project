package com.anhhoang.zoompoint.ui.photo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anhhoang.zoompoint.R;

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

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
    }

    public static Bundle getStartingBundle(String photoId) {
        Bundle bundle = new Bundle();
        bundle.putString(PHOTO_ID_KEY, photoId);

        return bundle;
    }
}
