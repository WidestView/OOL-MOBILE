package com.example.ool_mobile.service;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.service.api.setup.ApiProviderBuilder;
import com.example.ool_mobile.service.api.setup.JwtInterceptor;
import com.example.ool_mobile.service.api.setup.TokenStorage;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.immutables.value.Value;

// todo: use dagger.

@Value.Immutable
public abstract class Dependencies {

    @NonNull
    public static Dependencies from(@NonNull Activity activity) {
        return ((ProjectApplication) activity.getApplication()).getDependencies();
    }

    @NonNull
    public static Dependencies from(@NonNull Fragment fragment) {
        return from(fragment.requireActivity());
    }

    @NonNull
    abstract Context context();

    @Value.Check
    protected void onCreate() {
        setupPicasso();
    }

    private void setupPicasso() {
        Picasso picasso = new Picasso.Builder(context())
                .downloader(new OkHttp3Downloader(getApiProvider().getOkHttpClient()))
                .build();

        Picasso.setSingletonInstance(picasso);
    }

    @Value.Lazy
    @NonNull
    protected ApiProvider getApiProvider() {
        return new ApiProviderBuilder()
                .interceptor(new JwtInterceptor(getTokenStorage()))
                .tokenStorage(getTokenStorage())
                .context(context())
                .build();
    }

    @NonNull
    @Value.Lazy
    protected TokenStorage getTokenStorage() {
        return new TokenStorage(context());
    }

    @NonNull
    public EmployeeRepository getEmployeeRepository() {
        return new EmployeeRepository(
                getApiProvider().getUserApi(),
                getApiProvider().getEmployeeApi(),
                getTokenStorage()
        );
    }

    @NonNull
    @Value.Lazy
    public PhotoshootApi getPhotoshootApi() {
        return getApiProvider().getPhotoshootApi();
    }
}
