package com.example.ool_mobile.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.immutables.value.Value;

import java.util.Objects;

@Model
@Value.Immutable
public abstract class Equipment {

    @Value.Default
    public int getId() {
        return 0;
    }

    public abstract boolean isAvailable();

    public abstract int getDetailsId();

    @Nullable
    public abstract EquipmentDetails getDetails();

    @NonNull
    public EquipmentDetails requireDetails() {
        return Objects.requireNonNull(getDetails(), "Equipment details is not set");
    }
}
