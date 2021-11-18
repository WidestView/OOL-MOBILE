package com.example.ool_mobile.service.log;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface LogDatabase extends AutoCloseable {

    void addEntry(@NonNull LogEntry entry);

    @NonNull
    Single<List<LogEntry>> listEntries();
}
