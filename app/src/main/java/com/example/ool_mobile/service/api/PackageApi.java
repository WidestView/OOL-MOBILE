package com.example.ool_mobile.service.api;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.PackageModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface PackageApi {

    @NonNull
    @GET("package")
    Single<List<PackageModel>> listPackages();
}
