package com.example.ool_mobile.model.api;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TokenStorage {

    private static final String TOKEN_KEY = "token";
    private static final String PREFERENCES_NAME = "TokenStorage";

    private final SharedPreferences preferences;

    private String tokenCache;

    private boolean hasTokenCache;


    public TokenStorage(@NonNull Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    public String getToken() {

        if (!hasTokenCache) {
            tokenCache = preferences.getString(TOKEN_KEY, null);
            hasTokenCache = true;
        }

        return tokenCache;
    }

    public void setToken(@Nullable String token) {

        tokenCache = token;

        hasTokenCache = true;

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(TOKEN_KEY, token);

        editor.apply();
    }

}
