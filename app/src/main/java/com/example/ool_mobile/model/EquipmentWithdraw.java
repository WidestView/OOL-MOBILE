package com.example.ool_mobile.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.immutables.value.Value;

import java.util.Date;
import java.util.UUID;

import retrofit2.http.POST;

@Value.Immutable
@Model
public abstract class EquipmentWithdraw {

    @Value.Default
    public int getId() {
        return 0;
    }

    @NonNull
    @POST
    public abstract Date getWithdrawDate();

    @NonNull
    public abstract Date getExpectedDevolutionDate();

    @Nullable
    public abstract Date getEffectiveDevolutionDate();

    @NonNull
    public abstract UUID getPhotoshootId();

    @Nullable
    public abstract Photoshoot getPhotoshoot();

    @NonNull
    public abstract String getEmployeeId();

    @Nullable
    public abstract Employee getEmployee();

    public abstract int getEquipmentId();

    @Nullable
    public abstract Equipment getEquipment();
}
