package com.example.ool_mobile.service.api;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public final class ApplicationRetrofit {

    private static Retrofit instance;

    private ApplicationRetrofit() {
    }

    @NonNull
    public static Retrofit get() {

        if (instance == null) {
            instance = build();
        }

        return instance;
    }

    @NotNull
    public static Retrofit build() {

        Retrofit.Builder builder = new Retrofit.Builder();

        builder.addCallAdapterFactory(RxJava3CallAdapterFactory.create());
        builder.addConverterFactory(MoshiConverterFactory.create());

        return builder.build();
    }
}
