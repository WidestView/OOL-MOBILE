package com.example.ool_mobile.ui.util.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import static android.app.Activity.RESULT_OK;

public class LegacySelectionHandler implements ImageSelectionHandler {

    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_CAMERA = 1;

    private String lastCameraPath;

    private final Subject<Bitmap> results = PublishSubject.create();

    private final Activity activity;

    public LegacySelectionHandler(@NonNull Activity activity) {
        Objects.requireNonNull(activity, "activity is null");

        this.activity = activity;
    }

    @Override
    @NonNull
    public Observable<Bitmap> getBitmapResults() {
        return results;
    }

    @Override
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

    @Override
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {

            decodeResultBitmap(lastCameraPath).toObservable()
                    .compose(neverComplete())
                    .subscribe(results);
        }

        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {

            readFile(data.getData()).toObservable()
                    .compose(neverComplete())
                    .subscribe(results);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

    }

    private <T> ObservableTransformer<T, T> neverComplete() {
        return upstream -> upstream.concatWith(Observable.never());
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
            File storageDirectory = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            return File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDirectory
            );
        }).subscribeOn(Schedulers.io());
    }

    private Single<Bitmap> readFile(Uri path) {

        return Single.fromSupplier(() -> {

            InputStream stream = activity.getContentResolver().openInputStream(path);

            return BitmapFactory.decodeStream(stream);
        }).subscribeOn(Schedulers.io());
    }

    private Single<Bitmap> decodeResultBitmap(String filePath) {

        return Single.fromSupplier(() -> scaleAndDecode(filePath))
                .map(bitmap ->
                        rotateIfNecessary(filePath, bitmap)
                )
                .subscribeOn(Schedulers.io());

    }

    private Bitmap rotateIfNecessary(String filePath, Bitmap bitmap) throws IOException {
        float rotation = getRotation(filePath);

        if (rotation != 0) {
            return rotate(bitmap, rotation);
        } else {
            return bitmap;
        }
    }


    private float getRotation(String path) throws IOException {

        int orientation = new androidx.exifinterface.media.ExifInterface(path).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);


        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;

            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;

            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;

            default:
                return 0;
        }
    }

    private Bitmap scaleAndDecode(String filePath) {

        // Get the dimensions of the bitmap
        Pair<Integer, Integer> size = getFileDimensions(filePath);

        int scaleFactor = calculateScaleFactor(size);

        BitmapFactory.Options options = new BitmapFactory.Options();

        // Decode the image file into a Bitmap sized to fill the View
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(filePath, options);

    }

    private int calculateScaleFactor(Pair<Integer, Integer> dimensions) {

        int targetW = 512;
        int targetH = 512;

        int photoW = dimensions.first;
        int photoH = dimensions.second;

        // Determine how much to scale down the image
        return Math.max(1, Math.min(photoW / targetW, photoH / targetH));
    }

    private Pair<Integer, Integer> getFileDimensions(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        return Pair.create(options.outWidth, options.outHeight);
    }

    private Bitmap rotate(Bitmap bitmap, float angle) {

        try {

            Matrix matrix = new Matrix();
            matrix.postRotate(angle);

            return Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    matrix,
                    true);
        } finally {
            bitmap.recycle();
        }

    }
}
