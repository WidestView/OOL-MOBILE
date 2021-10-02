package com.example.ool_mobile.service.api;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.Employee;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface EmployeeApi {

    @NonNull
    @GET("employee/info")
    Single<Response<Employee>> getCurrentEmployeeInfo();

}
