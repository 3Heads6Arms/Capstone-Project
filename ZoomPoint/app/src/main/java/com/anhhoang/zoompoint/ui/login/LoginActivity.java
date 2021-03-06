package com.anhhoang.zoompoint.ui.login;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.anhhoang.database.ZoomPointContract;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.ui.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    @BindView(R.id.login_controls)
    View loginControls;
    @BindView(R.id.button_login)
    Button loginButton;
    @BindView(R.id.button_signup)
    Button signUpButton;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new LoginPresenter().attach(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null) {
            presenter.login(uri);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void saveToken(String token) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(getString(R.string.token_preference_key), token)
                .apply();
    }

    @Override
    public void saveUsername(String username) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(getString(R.string.username_preference_key), username)
                .apply();
    }

    @Override
    public void saveFullName(String fullName) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(getString(R.string.fullname_preference_key), fullName)
                .apply();
    }

    @Override
    public void saveMyProfile(ContentValues userProfile) {
        getContentResolver().insert(ZoomPointContract.UserProfileEntry.CONTENT_URI, userProfile);
    }

    @Override
    public boolean isLoggedIn() {
        return PreferenceManager.getDefaultSharedPreferences(this)
                .contains(getString(R.string.token_preference_key));
    }

    @Override
    public void toggleProgress(boolean show) {
        if (show) {
            loginControls.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            loginControls.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showError(String message) {
        Snackbar.make(loginControls, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(int idString) {
        showError(getString(idString));
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_login)
    public void loginBtn_onClick() {
        Intent intent = presenter.getLoginIntent();
        startActivity(intent);
    }

    @OnClick(R.id.button_signup)
    public void signUpButton_onClick() {
        startActivity(presenter.getRegisterIntent());
    }
}
