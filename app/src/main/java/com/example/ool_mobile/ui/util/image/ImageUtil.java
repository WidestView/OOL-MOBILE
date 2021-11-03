package com.example.ool_mobile.ui.util.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.exifinterface.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ImageUtil {

    @NonNull
    public static Single<byte[]> decodeBitmap(@NonNull Bitmap bitmap) {

        return Single.fromSupplier(() -> {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            return stream.toByteArray();
        }).subscribeOn(Schedulers.io());
    }

    @NonNull
    public static Bitmap scaleAndDecode(@NonNull String filePath) {

        // Get the dimensions of the bitmap
        Pair<Integer, Integer> size = getFileDimensions(filePath);

        int scaleFactor = calculateScaleFactor(size);

        BitmapFactory.Options options = new BitmapFactory.Options();

        // Decode the image file into a Bitmap sized to fill the View
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(filePath, options);

    }

    private static int calculateScaleFactor(Pair<Integer, Integer> dimensions) {

        int targetW = 512;
        int targetH = 512;

        int photoW = dimensions.first;
        int photoH = dimensions.second;

        // Determine how much to scale down the image
        return Math.max(1, Math.min(photoW / targetW, photoH / targetH));
    }

    private static Pair<Integer, Integer> getFileDimensions(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        return Pair.create(options.outWidth, options.outHeight);
    }

    private static Bitmap rotate(Bitmap bitmap, float angle) {

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

    public static Bitmap rotateIfNecessary(String filePath, Bitmap bitmap) throws IOException {
        float rotation = getRotation(filePath);

        if (rotation != 0) {
            return rotate(bitmap, rotation);
        } else {
            return bitmap;
        }
    }


    private static float getRotation(String path) throws IOException {

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
}
