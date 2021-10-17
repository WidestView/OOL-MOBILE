package com.example.ool_mobile.ui.form.equipment;

import androidx.annotation.NonNull;

import org.immutables.value.Value;

@Value.Immutable
public interface EquipmentInput {

    @NonNull
    String getDetailsId();

    boolean isAvailable();
}
