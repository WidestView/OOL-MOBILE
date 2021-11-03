package com.example.ool_mobile.ui.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.ool_mobile.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UiDate {

    @NonNull
    private final Context context;

    public UiDate(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    public String formatDate(@NonNull Date date) {

        // q/9235934
        Format dateFormat = android.text.format.DateFormat.getDateFormat(context);

        return dateFormat.format(date);
    }

    @NonNull
    public String formatTime(@NonNull Date date) {

        Format dateFormat = android.text.format.DateFormat.getTimeFormat(context);

        return dateFormat.format(date);
    }

    @NonNull
    public String formatDateName(@NonNull Date date) {
        Context viewContext = Objects.requireNonNull(
                context,
                "itemView context cannot be null"
        );

        Locale locale = Locale.getDefault();


        String week = new SimpleDateFormat("EEEE", locale).format(date);
        String day = new SimpleDateFormat("dd", locale).format(date);
        String month = new SimpleDateFormat("MM", locale).format(date);

        return String.format(
                viewContext.getString(R.string.format_short_date),
                week,
                day,
                month
        )
                ;
    }


}
