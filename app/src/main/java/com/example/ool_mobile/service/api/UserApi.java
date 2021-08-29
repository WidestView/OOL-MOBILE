package com.example.ool_mobile.service.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

    @NonNull
    @POST("user/login")
    Single<Response<TokenData>> login(@NonNull @Body LoginData login);

    class TokenData {
        @Nullable
        public String token;
    }

    class LoginData {
        @Nullable
        public String username;

        @Nullable
        public String password;
    }
}
