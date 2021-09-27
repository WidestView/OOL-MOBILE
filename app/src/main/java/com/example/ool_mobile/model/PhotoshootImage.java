package com.example.ool_mobile.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public abstract class PhotoshootImage {

    @NonNull
    public abstract UUID id();

    @NonNull
    public abstract UUID photoshootId();

    @Nullable
    public abstract Photoshoot photoshoot();

    public abstract boolean onPortifolio();
}
