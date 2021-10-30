package com.example.ool_mobile.ui.component.date_dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

public class Util {

    @NonNull
    public static FragmentManager fragmentManagerFromContext(@NonNull Context context) {

        Objects.requireNonNull(context, "context is null");

        if (context instanceof FragmentActivity) {

            return ((FragmentActivity) context)
                    .getSupportFragmentManager();
        } else {
            throw new IllegalStateException(
                    "The DateDialogView context has to be a FragmentActivity"
            );
        }

    }
}
