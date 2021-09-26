package com.example.ool_mobile.service;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.service.api.setup.ApiProviderBuilder;
import com.example.ool_mobile.service.api.setup.TokenStorage;

// todo: use dagger.

public class ProjectApplication extends Application {
    private EmployeeRepository repository;

    @Nullable
    private ApiProvider apiClient;

    @Nullable
    private TokenStorage tokenStorage;

    @NonNull
    private ApiProvider getApiClient() {

        if (apiClient == null) {
            apiClient = new ApiProviderBuilder().build();
        }

        return apiClient;
    }

    @NonNull
    private TokenStorage getTokenStorage() {

        if (tokenStorage == null) {
            tokenStorage = new TokenStorage(this);
        }

        return tokenStorage;
    }

    @NonNull
    public EmployeeRepository getEmployeeRepository() {

        if (repository == null) {
            repository = new EmployeeRepository(
                    getApiClient().getUserApi(),
                    getApiClient().getEmployeeApi(),
                    getTokenStorage()
            );
        }

        return repository;
    }

    @NonNull
    public static ProjectApplication from(@NonNull Activity activity) {
        return ((ProjectApplication) activity.getApplication());
    }

    @NonNull
    public static ProjectApplication from(@NonNull Fragment fragment) {
        return from(fragment.requireActivity());
    }
}
