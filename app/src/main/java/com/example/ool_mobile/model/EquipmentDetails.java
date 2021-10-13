package com.example.ool_mobile.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.immutables.value.Value;

import java.util.Objects;

@Model
@Value.Immutable
public abstract class EquipmentDetails {

    @Value.Default
    public int getId() {
        return 0;
    }

    @NonNull
    public abstract String getName();

    public abstract double getPrice();

    public abstract int getKindId();

    @Nullable
    public abstract EquipmentKind getKind();

    @NonNull
    public EquipmentKind requireKind() {
        return Objects.requireNonNull(getKind(), "Equipment kind is not set");
    }
}
