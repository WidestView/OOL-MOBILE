package com.example.ool_mobile.ui.util;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class SnackMessage {

    public static void snack(@NonNull Activity activity, @StringRes int message) {
        snack(activity, message, Snackbar.LENGTH_LONG);
    }

    public static void snack(
            @NonNull Activity activity,
            @StringRes int message,
            @BaseTransientBottomBar.Duration int length) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, length).show();
    }
}
