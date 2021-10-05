package com.example.ool_mobile.ui.form.add_photo_shoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.ui.util.form.FormTime;

import org.immutables.value.Value;

@Value.Immutable(copy = false)
public interface PhotoshootInput {

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
