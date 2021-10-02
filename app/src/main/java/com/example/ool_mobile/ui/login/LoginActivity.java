package com.example.ool_mobile.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.content.ContentActivity;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;

    private CompositeDisposable disposable;

    private Button startButton;

    private EditText emailEditText;

    private EditText passwordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupViews();

        setupViewModel();

        viewModel.checkAlreadyLogged();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this,
                LoginViewModel.create(
                        Dependencies.from(this).getEmployeeRepository()
                )
        ).get(LoginViewModel.class);

        disposable = new CompositeDisposable();

        setupViewModelEvents();

        setupViewEvents();
    }

    private void setupViews() {


        startButton = findViewById(R.id.login_startButton);

        emailEditText = findViewById(R.id.login_emailEditText);

        passwordEditText = findViewById(R.id.login_passwordEditText);

        ConstraintLayout layout = findViewById(R.id.layout_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            layout.setForceDarkAllowed(false);
        }

    }

    private void setupViewEvents() {
        startButton.setOnClickListener(v -> viewModel.login(
                emailEditText.getText().toString(),
                passwordEditText.getText().toString()
        ));
    }

    private void setupViewModelEvents() {
        disposable.add(viewModel.getEvents().subscribe(event -> {
            switch (event) {


                case LoginViewModel.START_CONTENT_WITH_ANIMATION:
                    startActivity(new Intent(this, ContentActivity.class));
                    break;
                case LoginViewModel.START_CONTENT_WITHOUT_ANIMATION:
                    finish();
                    startActivity(new Intent(this, ContentActivity.class));
                    break;


                case LoginViewModel.REPORT_FAILED_LOGIN:

                    Snackbar.make(
                            findViewById(android.R.id.content),
                            R.string.error_invalidLogin,
                            Snackbar.LENGTH_LONG
                    ).show();
                    break;


                case LoginViewModel.REPORT_API_UNAVAILABLE:
                    emailEditText.setEnabled(false);
                    passwordEditText.setEnabled(false);
                    startButton.setEnabled(false);

                    Snackbar.make(
                            findViewById(android.R.id.content),
                            R.string.error_api_unavailable,
                            Snackbar.LENGTH_LONG
                    ).show();
                    break;
            }
        }));
    }

}