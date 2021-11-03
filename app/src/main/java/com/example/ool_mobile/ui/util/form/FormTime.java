package com.example.ool_mobile.ui.util.form;

import androidx.annotation.NonNull;

import org.immutables.value.Value;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Value.Immutable
public abstract class FormTime {

    @Value.Parameter
    public abstract long getHour();

    @Value.Parameter
    public abstract long getMinute();

    @NonNull
    public Date addToDate(@NonNull Date date) {

        Objects.requireNonNull(date, "date is null");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.HOUR, (int) getHour());
        calendar.add(Calendar.MINUTE, (int) getMinute());

        return calendar.getTime();
    }

    public long totalMinutes() {
        return getHour() * 60 + getMinute();
    }

    @NonNull
    public static FormTime fromDate(@NonNull Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return ImmutableFormTime.of(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
        );
    }

    @NonNull
    public static FormTime fromDateSpan(@NonNull Date date, int minutes) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.MINUTE, minutes);

        return ImmutableFormTime.of(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
        );

    }
}
