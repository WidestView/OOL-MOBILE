package com.example.ool_mobile.service.api;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface EmployeeApi {

    @NonNull
    @GET("employee/info")
    Single<Response<EmployeeData>> getCurrentEmployeeInfo();

    @SuppressLint("UnknownNullness")
    class EmployeeData {

        public String cpf;

        public String name;

        public String socialName;

        public String birthDate;

        public String phone;

        public String email;

        public int accessLevel;

        public int occupationId;

        @Nullable
        public OccupationData occupation;

        public String gender;

        public String rg;
    }

    @SuppressLint("UnknownNullness")
    class OccupationData {
        public int id;

        public String name;

        public String description;
    }

}
