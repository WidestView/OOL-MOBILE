package com.example.ool_mobile.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

public class Employee {

    @NonNull
    private final String cpf;

    @NonNull
    private final String name;

    @Nullable
    private final String socialName;

    @NonNull
    private final Date birthDate;

    @NonNull
    private final String phone;

    @NonNull
    private final String email;

    private final boolean active;

    public Employee(
            @NonNull String cpf,
            @NonNull String name,
            @Nullable String socialName,
            @NonNull Date birthDate,
            @NonNull String phone,
            @NonNull String email,
            boolean active) {
        this.cpf = cpf;
        this.name = name;
        this.socialName = socialName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
        this.active = active;
    }

    @NonNull
    public String getCpf() {
        return cpf;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getSocialName() {
        return socialName;
    }

    @NonNull
    public Date getBirthDate() {
        return birthDate;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return active;
    }
}
