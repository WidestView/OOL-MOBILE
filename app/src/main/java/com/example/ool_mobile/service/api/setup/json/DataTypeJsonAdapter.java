package com.example.ool_mobile.service.api.setup.json;

import androidx.annotation.NonNull;

import com.example.ool_mobile.service.api.setup.ApiDate;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.Date;
import java.util.UUID;

public class DataTypeJsonAdapter {

    @FromJson
    @NonNull
    public UUID UUIDFromJson(@NonNull String text) {
        return UUID.fromString(text);
    }

    @ToJson
    @NonNull
    public String UUIDtoJson(@NonNull UUID uuid) {
        return uuid.toString();
    }

    @FromJson
    @NonNull
    public Date dateFromJson(@NonNull String text) {
        return ApiDate.parse(text);
    }

    @ToJson
    @NonNull
    public String dateToJson(@NonNull Date date) {
        return ApiDate.format(date);
    }

}
