package com.example.ool_mobile.service;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.service.api.EmployeeApi;
import com.example.ool_mobile.service.api.UserApi;
import com.example.ool_mobile.service.api.setup.ApiInfo;
import com.example.ool_mobile.service.api.setup.ResponseException;
import com.example.ool_mobile.service.api.setup.TokenStorage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.CheckReturnValue;
import io.reactivex.rxjava3.core.Completable;
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
    @CheckReturnValue
    public Single<Boolean> login(@NonNull String username, @NonNull String password) {

        return Single.defer(() -> {

            UserApi.LoginData loginData = new UserApi.LoginData();
            loginData.login = username;
            loginData.password = password;


            Single<Response<UserApi.TokenData>> call = api.login(loginData);

            return call.map(response -> {
                if (response.isSuccessful()) {
                    UserApi.TokenData data = response.body();

                    Objects.requireNonNull(data);

                    this.tokenStorage.setToken(data.token);

                    return true;
                } else if (response.code() == 401 || response.code() == 403) {

                    this.tokenStorage.setToken(null);

                    return false;
                } else {
                    throw new ResponseException(response);
                }
            });

        });
    }

    @NonNull
    @CheckReturnValue
    public Completable logout() {

        return Completable.fromAction(() -> {
            this.tokenStorage.setToken(null);
        });
    }

    @NonNull
    @CheckReturnValue
    public Maybe<Employee> getCurrentEmployee() {

        return Maybe.defer(() -> {

            String token = tokenStorage.getToken();

            if (token == null) {
                return Maybe.empty();
            }

            return fetchEmployee();
        });
    }

    @CheckReturnValue
    private Maybe<Employee> fetchEmployee() {

        return Maybe.defer(() -> {

            Single<Response<Employee>> call = employeeApi.getCurrentEmployeeInfo();

            return call.flatMapMaybe(response -> {
                if (response.isSuccessful()) {
                    return Maybe.just(response.body());
                } else if (response.code() == 401 || response.code() == 403) {

                    return Maybe.empty();

                } else {
                    throw new ResponseException(response);
                }
            });

        });
    }

    @NonNull
    public URL getCurrentEmployeePictureURL() {

        String link = ApiInfo.API_BASE_URL + "user/picture";

        try {
            return new URL(link);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }


}
