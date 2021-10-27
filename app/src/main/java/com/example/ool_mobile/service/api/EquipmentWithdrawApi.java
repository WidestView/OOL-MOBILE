package com.example.ool_mobile.service.api;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.EquipmentWithdraw;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EquipmentWithdrawApi {

    @NonNull
    @GET("equipment/withdraw")
    Single<List<EquipmentWithdraw>> listWithdraws();

    @NonNull
    @POST("equipment/withdraw/{id}")
    Single<List<EquipmentWithdraw>> addWithdraw(@NonNull @Body EquipmentWithdraw withdraw);

    @NonNull
    @PUT("equipment/withdraw/{id}")
    Single<List<EquipmentWithdraw>> updateWithdraw(
            @Path("id") int id,
            @NonNull @Body EquipmentWithdraw withdraw
    );
}
