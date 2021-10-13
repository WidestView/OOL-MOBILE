package com.example.ool_mobile.model;

import androidx.annotation.NonNull;

import org.immutables.value.Value;

@Model
@Value.Immutable
public abstract class EquipmentKind {

    @Value.Default
    public int getId() {
        return 0;
    }

    @NonNull
    public abstract String getName();

    @NonNull
    public abstract String getDescription();
}
