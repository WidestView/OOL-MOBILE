package com.example.ool_mobile.service;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.service.api.setup.ApiInfo;

public class ModelUrls {

    final String base = ApiInfo.API_BASE_URL;

    @NonNull
    public Uri imageUrlFromDetailsId(int id) {
        return Uri.parse(base + "equipment/details/image/" + id);
    }

    @Nullable
    public Uri getImageOf(@Nullable EquipmentDetails details) {

        if (details == null) {
            return null;
        }

        return imageUrlFromDetailsId(details.getId());
    }
}
