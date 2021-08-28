package com.example.ool_mobile.model;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.api.EmployeeApi;
import com.example.ool_mobile.model.api.TokenStorage;
import com.example.ool_mobile.model.api.UserApi;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;

public class EmployeeRepository {

    @NonNull
    private final UserApi api;

    @NonNull
    private final EmployeeApi employeeApi;

    @NonNull
    private final TokenStorage tokenStorage;


    public EmployeeRepository(
            @NonNull UserApi api,
            @NonNull EmployeeApi employeeApi,
            @NonNull TokenStorage token
    ) {
        this.api = api;
        this.employeeApi = employeeApi;
        this.tokenStorage = token;
    }

    @NonNull
    public Single<Boolean> login(@NonNull String username, @NonNull String password) {

        UserApi.LoginData loginData = new UserApi.LoginData();
        loginData.username = username;
        loginData.password = password;


        Single<Response<UserApi.TokenData>> call = api.login(loginData);

        return call.map(response -> {
            if (response.isSuccessful()) {
                UserApi.TokenData data = response.body();

                Objects.requireNonNull(data);

                this.tokenStorage.setToken(data.token);

                return true;
            } else {

                this.tokenStorage.setToken(null);

                return false;
            }
        });
    }

    @NonNull
    public Maybe<Employee> getCurrentEmployee() {

        String token = tokenStorage.getToken();

        if (token == null) {
            return null;
        }

        return fetchEmployee();
    }

    private Maybe<Employee> fetchEmployee() {

        Single<Response<EmployeeApi.EmployeeData>> call = employeeApi.getCurrentEmployeeInfo();

        return call.flatMapMaybe(response -> {
            if (response.isSuccessful()) {

                return convertEmployee(response.body());
            } else {
                return Maybe.empty();
            }
        });
    }

    @NotNull
    private Maybe<Employee> convertEmployee(EmployeeApi.EmployeeData output) {

        // todo: get data from response body

        Objects.requireNonNull(output);

        return Maybe.just(
                new Employee(
                        "11111111111",
                        "bob",
                        null,
                        new Date(),
                        "11912341234",
                        "bob@bob.com",
                        true
                )
        );
    }


}
