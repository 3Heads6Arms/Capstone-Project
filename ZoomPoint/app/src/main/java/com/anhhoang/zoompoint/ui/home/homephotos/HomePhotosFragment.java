package com.anhhoang.zoompoint.ui.home.homephotos;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.ui.photos.PhotosFragment;
import com.anhhoang.zoompoint.utils.PhotosCallType;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomePhotosFragment extends Fragment implements HomePhotosContract.View {
    private static final String TAG = HomePhotosFragment.class.getCanonicalName();
    private static final String SPINNER_POSITION = "SpinnerPosition";

    @BindView(R.id.spinner_sort_query)
    Spinner sortQuerySpn;

    private HomePhotosContract.Presenter presenter;
    private boolean isUserAction;

    public HomePhotosFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_photos, container, false);
        ButterKnife.bind(this, view);

        SpinnerAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, new String[]{
                getString(R.string.latest),
                getString(R.string.populars),
                getString(R.string.curated),
                getString(R.string.oldest)
        });
        sortQuerySpn.setAdapter(adapter);
        sortQuerySpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isUserAction) {
                    presenter.onPhotoSourceChanged(position);
                    isUserAction = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // On screen rotation this will triggers ItemSelectedListener. Which will cause reload of fragment
        sortQuerySpn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isUserAction = true;
                return false;
            }
        });

        if (savedInstanceState != null) {
            sortQuerySpn.setSelection(savedInstanceState.getInt(SPINNER_POSITION, 0));
        } else {
            // On first/creation onItemSelected is wanted to be called with user action to access presenter logic
            isUserAction = true;
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter == null) {
            new HomePhotosPresenter().attach(this);
        } else {
            presenter.attach(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        presenter.detach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SPINNER_POSITION, sortQuerySpn.getSelectedItemPosition());
    }

    @Override
    public void setPresenter(HomePhotosContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updatePhotoSource(PhotosCallType callType, String query) {
        Fragment fragment = new PhotosFragment();
        fragment.setArguments(PhotosFragment.getStartingBundle(callType, query));

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_photos, fragment, TAG)
                .commit();
    }
}
