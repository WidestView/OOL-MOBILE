package com.example.ool_mobile.service.log;

import android.util.Log;

import androidx.annotation.NonNull;

import org.immutables.value.Value;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Value.Immutable
public interface LogEntry {

    @NonNull
    String priority();

    @NonNull
    Date date();

    @Nullable
    String tag();

    @NonNull
    String text();

    @Nullable
    String exceptionMessage();

    @NonNull
    static String priorityString(int priority) {

        switch (priority) {

            case Log.DEBUG:
                return "DEBUG";

            case Log.ERROR:
                return "ERROR";

            case Log.INFO:
                return "INFO";

            case Log.VERBOSE:
                return "VERBOSE";

            case Log.WARN:
                return "WARN";

            case Log.ASSERT:
                return "ASSERT";

            default:
                throw new IllegalArgumentException("Invalid priority " + priority);
        }
    }
}
