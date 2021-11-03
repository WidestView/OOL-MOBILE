package com.example.ool_mobile.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.immutables.value.Value;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Model
@Value.Immutable
public abstract class Photoshoot {

    @NonNull
    public abstract UUID resourceId();

    @NonNull
    public abstract String address();

    public abstract int orderId();

    // @Nullable
    // public abstract Order order();

    @NonNull
    public abstract Date startTime();

    public abstract int durationMinutes();

    @Nullable
    public abstract List<PhotoshootImage> images();
}
