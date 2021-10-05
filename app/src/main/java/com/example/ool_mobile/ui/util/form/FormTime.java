package com.example.ool_mobile.ui.util.form;

import androidx.annotation.NonNull;

import org.immutables.value.Value;

import java.util.Calendar;
import java.util.Date;

@Value.Immutable
public interface FormTime {

    @Value.Parameter
    long getHour();

    @Value.Parameter
    long getMinute();

    @NonNull
    static FormTime fromDate(@NonNull Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return ImmutableFormTime.of(
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE)
        );
    }

    @NonNull
    static FormTime fromDateSpan(@NonNull Date date, int minutes) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.MINUTE, minutes);

        return ImmutableFormTime.of(
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE)
        );

    }
}
