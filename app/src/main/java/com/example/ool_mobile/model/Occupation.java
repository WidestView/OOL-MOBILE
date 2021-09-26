package com.example.ool_mobile.model;

import androidx.annotation.NonNull;

import org.immutables.value.Value;


@Model
@Value.Immutable
public abstract class Occupation {

    public abstract int id();

    @NonNull
    public abstract String name();

    @NonNull
    public abstract String description();
}
