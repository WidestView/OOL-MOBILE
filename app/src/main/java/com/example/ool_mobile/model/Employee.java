package com.example.ool_mobile.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.immutables.value.Value;

import java.util.Date;

@Model
@Value.Immutable
public abstract class Employee {

    @NonNull
    public abstract String cpf();

    @NonNull
    public abstract String name();

    @Nullable
    public abstract String socialName();

    @NonNull
    public abstract Date birthDate();

    @NonNull
    public abstract String phone();

    @NonNull
    public abstract String email();

    public abstract int accessLevel();

    public abstract int occupationId();

    @Nullable
    public abstract Occupation occupation();

    @NonNull
    public abstract String gender();

    @NonNull
    public abstract String rg();
}
