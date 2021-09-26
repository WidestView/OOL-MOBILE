package com.example.ool_mobile.service.api.setup;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Response;

public class ResponseException extends RuntimeException {

    public ResponseException(@NonNull Response<?> response) {
        super(getMessage(response));
    }

    @NonNull
    public static <T> retrofit2.Response<T> requireOk(@NonNull retrofit2.Response<T> response) {
        if (response.isSuccessful()) {
            throw new ResponseException(response);
        }

        return response;
    }

    private static String getMessage(@NonNull Response<?> response) {

        if (response.isSuccessful()) {
            return "Invalid response. code: " + response.code();
        } else {
            String message = "Invalid response. code: " + response.code() + ". Body:\n";

            try {
                return message + Objects.requireNonNull(response.errorBody()).string();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
