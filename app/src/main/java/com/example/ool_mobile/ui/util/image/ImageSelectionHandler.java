package com.example.ool_mobile.ui.util.image;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface ImageSelectionHandler {
    @NonNull
    Observable<Bitmap> getBitmapResults();

    @NonNull
    @CheckResult
    Completable requestGallery();

    @NonNull
    @CheckResult
    Completable requestCamera();

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults);
}
