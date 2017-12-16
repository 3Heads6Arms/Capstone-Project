package com.anhhoang.zoompoint.ui;

import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.anhhoang.zoompoint.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContracts.View {
    @BindView(R.id.login_controls)
    View loginControls;
    @BindView(R.id.button_login)
    Button loginButton;
    @BindView(R.id.button_signup)
    Button signUpButton;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private LoginContracts.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        new LoginPresenter().onAttach(this);
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
    public void setPresenter(LoginContracts.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void saveToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            // TODO: Remove comments following
//            PreferenceManager.getDefaultSharedPreferences(this)
//                    .edit()
//                    .putString(getString(R.string.token_preference_key), token)
//                    .apply();

            // TODO: Proceed next screen
            showError("Logged in with token: " + token);
        }
    }

    @Override
    public void checkLoggedIn() {
        boolean isLoggedIn = PreferenceManager.getDefaultSharedPreferences(this)
                .contains(getString(R.string.token_preference_key));
        if (isLoggedIn) {
            // TODO: Proceed next screen
        }
        toggleProgress(false);

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
