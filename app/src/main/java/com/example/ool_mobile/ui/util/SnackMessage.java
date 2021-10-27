package com.example.ool_mobile.ui.util;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class SnackMessage {

    public static void snack(@NonNull Fragment fragment, @StringRes int message) {
        snack(fragment, message, Snackbar.LENGTH_LONG);
    }

    public static void snack(@NonNull Fragment fragment, @StringRes int message, int length) {
        snack(fragment.getView(), message, length);
    }

    public static void snack(@NonNull Activity activity, @StringRes int message) {
        snack(activity, message, Snackbar.LENGTH_LONG);
    }

    public static void snack(
            @NonNull Activity activity,
            @StringRes int message,
            @BaseTransientBottomBar.Duration int length) {
        snack(activity.findViewById(android.R.id.content), message, length);
    }

    private static void snack(View view, @StringRes int message, @BaseTransientBottomBar.Duration int length) {
        Snackbar.make(view, message, length).show();
    }
}
