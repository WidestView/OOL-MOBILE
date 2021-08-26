package com.example.ool_mobile.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.model.api.UserApi;

import java.util.Objects;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;

public class EmployeeRepository {

    @NonNull
    private final UserApi api;

    private String token;

    @Nullable
    private Employee currentEmployee;

    public EmployeeRepository(@NonNull UserApi api) {
        this.api = api;
    }

    @Nullable
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    @Nullable
    public Single<Boolean> login(@NonNull String username, @NonNull String password) {

        UserApi.LoginData loginData = new UserApi.LoginData();
        loginData.username = username;
        loginData.password = password;


        Single<Response<UserApi.TokenData>> call = api.login(loginData);

        return call.map(response -> {
            if (response.isSuccessful()) {
                UserApi.TokenData data = response.body();

                Objects.requireNonNull(data);

                token = data.token;

                return true;
            } else {
                return false;
            }
        });
    }
}
