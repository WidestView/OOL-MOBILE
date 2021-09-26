package com.example.ool_mobile.service.api.setup;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {

    @NonNull
    private final TokenStorage tokenStorage;

    public JwtInterceptor(@NonNull TokenStorage tokenStorage) {
        this.tokenStorage = tokenStorage;
    }

    @Override
    @NonNull
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {

        Request original = chain.request();

        String header = "Bearer " + tokenStorage.getToken();

        Request.Builder builder = original.newBuilder()
                .header("Authorization", header);

        Request request = builder.build();

        return chain.proceed(request);
    }
}
