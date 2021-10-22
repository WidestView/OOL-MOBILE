package com.example.ool_mobile.ui.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import static android.app.Activity.RESULT_OK;

public class ImageInputHandler {

    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_CAMERA = 1;

    private final Activity activity;

    public ImageInputHandler(@NonNull Activity activity) {
        Objects.requireNonNull(activity, "activity is null");

        this.activity = activity;
    }

    @NonNull
    @CheckResult
    public Completable requestGallery() {

        return Completable.fromAction(() -> {

            Intent galleryIntent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


            activity.startActivityForResult(galleryIntent, REQUEST_GALLERY);
        });
    }

    private String lastCameraPath;

    @NonNull
    @CheckResult
    public Completable requestCamera() {

        return Completable.defer(() -> {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (cameraIntent.resolveActivity(activity.getPackageManager()) == null) {
                throw new UnsupportedOperationException("No camera available");
            }

            return createImageFile().flatMapCompletable(file -> {

                lastCameraPath = file.getAbsolutePath();

                Uri fileUri = getFileUri(file);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                activity.startActivityForResult(cameraIntent, REQUEST_CAMERA);

                return Completable.complete();
            });

        });

    }

    private Uri getFileUri(File file) {

        return FileProvider.getUriForFile(
                activity,
                "com.example.ool_mobile.fileprovider",
                file);

    }

    private Single<File> createImageFile() {

        return Single.fromSupplier(() -> {

            @SuppressLint("SimpleDateFormat")
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            return File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        });
    }

    private final Subject<Bitmap> results = PublishSubject.create();

    @NonNull
    public Observable<Bitmap> getBitmapResults() {
        return results;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {

            decodeResultBitmap(lastCameraPath)
                    .toObservable()
                    .concatWith(Observable.never())
                    .subscribe(results);
        }

        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {

            readFile(data.getData())
                    .toObservable()
                    .concatWith(Observable.never())
                    .subscribe(results);
        }
    }

    private Single<Bitmap> readFile(Uri path) {

        return Single.fromSupplier(() -> {

            InputStream stream = activity.getContentResolver().openInputStream(path);

            return BitmapFactory.decodeStream(stream);
        }).subscribeOn(Schedulers.io());
    }

    private Single<Bitmap> decodeResultBitmap(String filePath) {

        return Single.fromSupplier(() -> {

            int targetW = 512;
            int targetH = 512;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(filePath, bmOptions);

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            return BitmapFactory.decodeFile(filePath, bmOptions);
        }).subscribeOn(Schedulers.io());


    }
}
