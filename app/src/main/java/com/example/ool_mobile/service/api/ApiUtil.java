package com.example.ool_mobile.service.api;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.example.ool_mobile.ui.util.image.ImageUtil;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ApiUtil {

    @NonNull
    public static Single<MultipartBody.Part> multiPartFromBitmap(@NonNull Bitmap bitmap) {

        return ImageUtil.decodeBitmap(bitmap).map(bytes -> {

            RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), bytes);

            return MultipartBody.Part.createFormData(
                    "file",
                    "image.jpg",
                    body
            );
        });
    }
}
