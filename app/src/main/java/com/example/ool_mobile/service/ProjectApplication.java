package com.example.ool_mobile.service;

import android.app.Application;

import androidx.annotation.NonNull;


public class ProjectApplication extends Application {

    private Dependencies dependencies;

    @Override
    public void onCreate() {
        super.onCreate();

        dependencies = ImmutableDependencies.builder().context(this).build();
    }

    @NonNull
    public Dependencies getDependencies() {
        return dependencies;
    }

}
