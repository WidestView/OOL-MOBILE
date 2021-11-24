package com.example.ool_mobile.service.log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;

import timber.log.Timber;

public class LogDatabaseTree extends Timber.Tree {

    private final LogDatabase logDatabase;

    public LogDatabaseTree(@NonNull LogDatabase logDatabase) {

        Objects.requireNonNull(logDatabase);

        this.logDatabase = logDatabase;
    }

    @Override
    protected void log(
            int priority,
            @Nullable String tag,
            @NotNull String text,
            @Nullable Throwable throwable
    ) {
        logDatabase.addEntry(ImmutableLogEntry.builder()
                .priority(LogEntry.priorityString(priority))
                .date(new Date())
                .text(text)
                .tag(tag)
                .exceptionMessage(throwable == null ? null : throwable.getMessage())
                .build());

    }

}
