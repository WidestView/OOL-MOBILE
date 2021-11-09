package com.example.ool_mobile.service.api;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.AccessLevel;
import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.model.Occupation;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface EmployeeApi {

    @NonNull
    @GET("employee/info")
    Single<Response<Employee>> getCurrentEmployeeInfo();

    @NonNull
    @GET("employee")
    Single<List<Employee>> listEmployees();

    @NonNull
    @GET("employee/occupations")
    Single<List<Occupation>> listOccupations();

    @NonNull
    @GET("employee/levels")
    Single<List<AccessLevel>> listAccessLevels();
}
