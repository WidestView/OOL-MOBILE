package com.example.ool_mobile.model.api;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface EmployeeApi {

    @NonNull
    @GET("employee/info")
    Single<Response<EmployeeData>> getCurrentEmployeeInfo();

    // todo: fill with token data from api

    class EmployeeData {


    }

}
