package com.example.ool_mobile.service.api;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.Order;

import java.util.List;

import io.reactivex.rxjava3.annotations.CheckReturnValue;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface OrderApi {

    @NonNull
    @CheckReturnValue
    @GET("order/all")
    Single<List<Order>> listAllOrders();
}
