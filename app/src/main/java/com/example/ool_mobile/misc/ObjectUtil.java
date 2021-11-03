package com.example.ool_mobile.misc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ObjectUtil {

    @NonNull
    public static <T> T coalesce(@Nullable T first, @NonNull T second) {
        if (first == null) {
            return second;
        } else {
            return first;
        }
    }
}
