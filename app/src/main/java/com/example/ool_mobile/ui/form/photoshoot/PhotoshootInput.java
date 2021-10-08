package com.example.ool_mobile.ui.form.photoshoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.ui.util.form.FormTime;

import org.immutables.value.Value;

@Value.Immutable(copy = false)
interface PhotoshootInput {

    @NonNull
    String orderId();

    @NonNull
    String address();

    @Nullable
    FormTime startTime();

    @Nullable
    FormTime endTime();

    @Nullable
    Long date();
}
