package com.example.ool_mobile.model;

import androidx.annotation.NonNull;

import org.immutables.value.Value;

import java.util.Date;

@Model
@Value.Immutable
public abstract class Order {

    public abstract int getId();

    public abstract int getPackageId();

    @NonNull
    public abstract PackageModel getPackageModel();

    public abstract int getImageQuantity();

    public abstract double getPrice();

    @NonNull
    public abstract Date getPurchaseDate();

    @NonNull
    public abstract String getCustomerId();

    @NonNull
    public abstract String getCustomerName();

    public abstract boolean isDelivered();
}
