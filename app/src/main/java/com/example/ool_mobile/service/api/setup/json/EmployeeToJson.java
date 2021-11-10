package com.example.ool_mobile.service.api.setup.json;

import androidx.annotation.NonNull;

import org.immutables.value.Value;

import java.util.Date;

@Value.Immutable
public interface EmployeeToJson {

    @NonNull
    String cpf();

    @NonNull
    String name();

    @NonNull
    String socialName();

    @NonNull
    Date birthDate();

    @NonNull
    String phone();

    @NonNull
    String email();

    @NonNull
    String password();

    int accessLevel();

    @NonNull
    String gender();

    @NonNull
    String rg();

    int occupationId();
}
