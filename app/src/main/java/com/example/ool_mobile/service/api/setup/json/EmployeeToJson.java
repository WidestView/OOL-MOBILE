package com.example.ool_mobile.service.api.setup.json;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.immutables.value.Value;

import java.util.Date;

@Value.Immutable
public interface EmployeeToJson {

    @NonNull
    String cpf();

    @NonNull
    String name();

    @Nullable
    String socialName();

    @NonNull
    Date birthDate();

    @NonNull
    String phone();

    @NonNull
    String email();

    @Nullable
    String password();

    int accessLevel();

    @NonNull
    String gender();

    @NonNull
    String rg();

    int occupationId();
}
