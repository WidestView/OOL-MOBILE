package com.example.ool_mobile.model;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ool_mobile.model.api.fake.FakeUserApi;

public class ProjectApplication extends Application {
    private EmployeeRepository repository;

    @NonNull
    public static ProjectApplication from(@NonNull Activity activity) {
        return ((ProjectApplication) activity.getApplication());
    }

    @NonNull
    public static ProjectApplication from(@NonNull Fragment fragment) {
        return from(fragment.requireActivity());
    }

    @NonNull
    public EmployeeRepository getEmployeeRepository() {

        if (repository == null) {
            repository = new EmployeeRepository(new FakeUserApi(), null, null);
        }

        return repository;
    }

}
