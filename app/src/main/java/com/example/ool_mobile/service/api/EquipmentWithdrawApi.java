package com.example.ool_mobile.service.api;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.EquipmentWithdraw;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EquipmentWithdrawApi {

    @NonNull
    @GET("equipment/withdraw")
    Single<List<EquipmentWithdraw>> listWithdraws();

    @NonNull
    @POST("equipment/withdraw")
    Completable addWithdraw(@NonNull @Body EquipmentWithdraw withdraw);

    @NonNull
    @GET("equipment/withdraw/{id}")
    Single<EquipmentWithdraw> getById(@Path("id") int id);

    @NonNull
    @PUT("equipment/withdraw/{id}")
    Single<EquipmentWithdraw> updateWithdraw(
            @Path("id") int id,
            @NonNull @Body EquipmentWithdraw withdraw
    );

    @DELETE("equipment/withdraw/{id}")
    @NonNull
    Completable archiveWithdraw(
            @Path("id") int id
    );

    @GET("equipment/withdraw/finish/{id}")
    @NonNull
    Completable finishWithdraw(@Path("id") int id);
}
