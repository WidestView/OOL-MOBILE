package com.example.ool_mobile.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Model
@Value.Immutable
public abstract class PackageModel {

    public abstract int getId();

    @NonNull
    public abstract String getName();

    @NonNull
    public abstract String getDescription();

    public abstract double getBaseValue();

    public abstract double getPricePerPhoto();

    @Nullable
    public abstract Integer getImageQuantity();

    @Nullable
    public abstract Integer getQuantityMultiplier();

    @Nullable
    public abstract Integer getMaxIterations();

    public abstract boolean isAvailable();

    @NonNull
    @Value.Derived
    public List<Integer> getImageQuantities() {

        if (getImageQuantity() != null) {
            return Collections.singletonList(getImageQuantity());
        }

        if (getQuantityMultiplier() != null && getMaxIterations() != null) {

            List<Integer> result = new ArrayList<>(getMaxIterations());

            for (int i = 1; i <= getMaxIterations(); i++) {

                result.add(i * getQuantityMultiplier());
            }

            return result;
        }

        throw new IllegalStateException("Invalid package quantity state");
    }

}
