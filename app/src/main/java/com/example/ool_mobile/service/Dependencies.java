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
import com.example.ool_mobile.service.api.setup.json.DataTypeJsonAdapter;
import com.example.ool_mobile.service.log.LogDatabase;
import com.example.ool_mobile.service.log.LogDatabaseTree;
import com.example.ool_mobile.service.log.LogJsonAdapter;
import com.example.ool_mobile.service.log.SQLiteLogDatabase;
import com.example.ool_mobile.ui.log_export.ImmutableLogExport;
import com.example.ool_mobile.ui.log_export.LogExport;
import com.example.ool_mobile.ui.log_export.LogWriter;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

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
    abstract Context getContext();

    @Value.Check
    protected void onCreate() {
        setupPicasso();
    }

    private void setupPicasso() {
        Picasso picasso = new Picasso.Builder(getContext())
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
                .context(getContext())
                .build();
    }

    @NonNull
    @Value.Lazy
    protected TokenStorage getTokenStorage() {
        return new TokenStorage(getContext());
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
        return new SQLiteLogDatabase(getContext());
    }

    @NotNull
    public LogWriter getLogWriter() {

        return new LogWriter(
                getContext(),
                getEmployeeRepository(),
                new Moshi.Builder()
                        .add(new DataTypeJsonAdapter())
                        .add(new LogJsonAdapter())
                        .build()
                        .<LogExport>adapter(ImmutableLogExport.class)
        );
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
