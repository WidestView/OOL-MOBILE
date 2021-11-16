package com.example.ool_mobile.service;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.ool_mobile.BuildConfig;

import timber.log.Timber;


public class ProjectApplication extends Application {

    private Dependencies dependencies;

    @Override
    public void onCreate() {
        super.onCreate();

        dependencies = ImmutableDependencies.builder().context(this).build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Timber.plant(dependencies.getLogDatabaseTree());
    }

    @NonNull
    public Dependencies getDependencies() {
        return dependencies;
    }

}
