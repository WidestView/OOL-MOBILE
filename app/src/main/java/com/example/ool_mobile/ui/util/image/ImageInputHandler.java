package com.example.ool_mobile.ui.util.image;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public interface ImageInputHandler {
    @NonNull
    Observable<Bitmap> getBitmapResults();

    @NonNull
    @CheckResult
    Completable requestGallery();

    @NonNull
    @CheckResult
    Completable requestCamera();

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    @NonNull
    static Single<byte[]> formatBitmap(@NonNull Bitmap bitmap) {

        return Single.fromSupplier(() -> {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            return stream.toByteArray();
        }).subscribeOn(Schedulers.io());
    }
}
