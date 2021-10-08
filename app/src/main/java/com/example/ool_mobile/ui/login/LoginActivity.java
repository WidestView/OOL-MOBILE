package com.example.ool_mobile.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.ActivityLoginBinding;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.content.ContentActivity;
import com.example.ool_mobile.ui.util.AutoDisposable;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity
        implements LoginViewModel.Event.Visitor {

    private LoginViewModel viewModel;

    private AutoDisposable subscriptions;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subscriptions = AutoDisposable.onStop(getLifecycle());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        setupViews();

        setupViewModel();

        binding.setViewModel(viewModel);

        viewModel.checkAlreadyLogged();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this,
                LoginViewModel.create(
                        Dependencies.from(this).getEmployeeRepository()
                )
        ).get(LoginViewModel.class);
    }

    private void setupViews() {


        ConstraintLayout layout = binding.layoutLogin;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            layout.setForceDarkAllowed(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        subscriptions.add(viewModel.getEvents().subscribe(event ->
                event.accept(this)
        ));
    }

    @Override
    public void visitStartContentWithoutAnimation() {

        finish();
        startActivity(new Intent(this, ContentActivity.class));
    }

    @Override
    public void visitStartContentWithAnimation() {
        startActivity(new Intent(this, ContentActivity.class));
    }

    @Override
    public void visitReportFailedLogin() {

        Snackbar.make(
                findViewById(android.R.id.content),
                R.string.error_invalidLogin,
                Snackbar.LENGTH_LONG
        ).show();
    }

    @Override
    public void visitReportApiUnavailable() {

        binding.loginEmailEditText.setEnabled(false);
        binding.loginPasswordEditText.setEnabled(false);
        binding.loginStartButton.setEnabled(false);

        Snackbar.make(
                findViewById(android.R.id.content),
                R.string.error_api_unavailable,
                Snackbar.LENGTH_LONG
        ).show();
    }
}