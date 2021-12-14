package com.example.ool_mobile.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.content.ContentActivity;
import com.example.ool_mobile.ui.login.LoginActivity;
import com.example.ool_mobile.ui.login.LoginActivityArgs;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;

public class SplashActivity extends AppCompatActivity implements SplashViewModel.Event.Visitor {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashViewModel viewModel =
                new ViewModelProvider(
                        this,
                        SplashViewModel.create(
                                Dependencies.from(this)
                                        .getEmployeeRepository()
                        )
                ).get(SplashViewModel.class);


        viewModel.checkAlreadyLogged();

        viewModel.getEvents()
                .to(DisposedFromLifecycle.of(this))
                .subscribe(event -> {
                    event.accept(this);
                });



    }

    @Override
    public void visitError() {
        startLogin(true);
    }

    @Override
    public void visitNotLogged() {
        startLogin(false);
    }


    @Override
    public void visitAlreadyLogged() {

        forgetActivityAndStartIntent(
                new Intent(this, ContentActivity.class)
        );
    }

    private void startLogin(boolean isError) {
        LoginActivityArgs activityArgs = new LoginActivityArgs
                .Builder()
                .setIsError(isError)
                .build();

        forgetActivityAndStartIntent(
                new Intent(this, LoginActivity.class)
                        .putExtras(activityArgs.toBundle())
        );
    }

    private void forgetActivityAndStartIntent(Intent intent) {
        startActivity(intent);
        finish();
    }

}
