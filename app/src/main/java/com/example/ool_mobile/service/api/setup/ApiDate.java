package com.example.ool_mobile.service.api.setup;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class ApiDate {

    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss";

    @SuppressLint("SimpleDateFormat")
    @NonNull
    public static String format(@NonNull Date date) {
        return new SimpleDateFormat(DATE_FORMAT_STRING).format(date);
    }

    @NonNull
    @SuppressLint("SimpleDateFormat")
    public static Date parse(@NonNull String text) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_STRING);

        try {
            return Objects.requireNonNull(format.parse(text));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
