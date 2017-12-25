package com.anhhoang.zoompoint.ui.home.search;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.ui.photos.PhotosFragment;
import com.anhhoang.zoompoint.utils.PhotosCallType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchContract.View {
    private static final String TAG = SearchFragment.class.getCanonicalName();

    private SearchContract.Presenter presenter;

    @BindView(R.id.edit_text_search)
    EditText searchEt;

    public SearchFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        Drawable searchIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_search_white_24dp);
        searchIcon = DrawableCompat.wrap(searchIcon);
        searchIcon.mutate();
        DrawableCompat.setTint(searchIcon, ContextCompat.getColor(getContext(), R.color.edit_text_icon_color));

        searchEt.setCompoundDrawablesRelativeWithIntrinsicBounds(searchIcon, null, null, null);

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);

                    onSearchClicked();

                    return true;
                }

                return false;
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
            new SearchPresenter().attach(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @OnClick(R.id.button_search)
    public void onSearchClicked() {
        if (!TextUtils.isEmpty(searchEt.getText())) {
            presenter.onSearch(searchEt.getText().toString());
        }
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updatePhotoSource(PhotosCallType type, String query) {

        Fragment fragment = new PhotosFragment();
        fragment.setArguments(PhotosFragment.createBundle(type, query));

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_search_photos, fragment, TAG)
                .commit();
    }
}
