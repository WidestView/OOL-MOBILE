package com.example.ool_mobile.model;

import androidx.annotation.NonNull;

import org.immutables.value.Value;

@Model
@Value.Immutable
public abstract class AccessLevel {

    public abstract int getId();

    @NonNull
    public abstract String getName();

}
