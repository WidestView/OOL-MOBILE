package com.example.ool_mobile.service.api;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.model.EquipmentKind;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EquipmentApi {

    @NonNull
    @GET("equipment/details")
    Single<List<EquipmentDetails>> listDetails();

    @NonNull
    @GET("equipment/details/{id}")
    Single<EquipmentDetails> getDetailsById(@Path("id") int id);

    @NonNull
    @GET("equipment")
    Single<List<Equipment>> listEquipments();

    @NonNull
    @POST("equipment/details")
    Single<EquipmentDetails> addDetails(@NonNull @Body EquipmentDetails details);

    @NonNull
    @POST("equipment")
    Single<Equipment> addEquipment(@NonNull @Body Equipment equipment);

    @NonNull
    @GET("equipment/types")
    Single<List<EquipmentKind>> listKinds();

    @NonNull
    @POST("equipment/types")
    Single<EquipmentKind> addEquipmentKind(@NonNull @Body EquipmentKind kind);

    @NonNull
    @POST("equipment/{id}")
    Single<Equipment> getEquipmentById(@Path("id") int id);
}
