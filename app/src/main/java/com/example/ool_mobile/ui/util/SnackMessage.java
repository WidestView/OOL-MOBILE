package com.example.ool_mobile.ui.util;

import android.view.View;

import androidx.annotation.NonNull;

import java.util.Objects;

public class SnackMessage {

    @NonNull
    public static View rootView(@NonNull View anyView) {
        Objects.requireNonNull(anyView, "anyView is null");

        return anyView;
    }
}
