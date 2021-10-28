package com.example.ool_mobile.service.api;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.Photoshoot;

import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.annotations.CheckReturnValue;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PhotoshootApi {

    @NonNull
    @CheckReturnValue
    @GET("photoshoot")
    Single<List<Photoshoot>> listAll();

    @NonNull
    @CheckReturnValue
    @GET("photoshoot/current")
    Single<List<Photoshoot>> listFromCurrentEmployee();

    @NonNull
    @CheckReturnValue
    @GET("photoshoot/{id}")
    Single<Photoshoot> getPhotoshootWithId(@NonNull @Path("id") UUID id);

    @NonNull
    @CheckReturnValue
    @POST("photoshoot/add")
    Single<Photoshoot> addPhotoshoot(@NonNull @Body Photoshoot photoshoot);

}
