package com.example.ool_mobile.service;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.service.api.EquipmentWithdrawApi;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.service.api.setup.ApiProviderBuilder;
import com.example.ool_mobile.service.api.setup.JwtInterceptor;
import com.example.ool_mobile.service.api.setup.TokenStorage;
import com.example.ool_mobile.service.log.LogDatabase;
import com.example.ool_mobile.service.log.LogDatabaseTree;
import com.example.ool_mobile.service.log.SQLiteLogDatabase;
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
    public ApiProvider getApiProvider() {
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
    public LogDatabase getLogDatabase() {
        return new SQLiteLogDatabase(context());
    }

    @NonNull
    @Value.Lazy
    public LogDatabaseTree getLogDatabaseTree() {
        return new LogDatabaseTree(getLogDatabase());
    }

    @NonNull
    @Value.Lazy
    public PhotoshootApi getPhotoshootApi() {
        return getApiProvider().getPhotoshootApi();
    }

    @NonNull
    @Value.Lazy
    public EquipmentApi getEquipmentApi() {
        return getApiProvider().getEquipmentApi();
    }

    @NonNull
    @Value.Lazy
    public EquipmentWithdrawApi getWithdrawApi() {
        return getApiProvider().getWithdrawApi();
    }
}
