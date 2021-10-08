package com.example.ool_mobile.ui.form.photoshoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.ui.util.form.FormTime;

import org.immutables.value.Value;

@Value.Immutable(copy = false)
interface PhotoshootInput {

    @NonNull
    String getOrderId();

    @NonNull
    String getAddress();

    @Nullable
    FormTime getStartTime();

    @Nullable
    FormTime getEndTime();

    @Nullable
    Long getDate();
}
