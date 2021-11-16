package com.example.ool_mobile.service.log;

import androidx.annotation.NonNull;

public interface LogDatabase extends AutoCloseable {

    void addEntry(@NonNull LogEntry entry);
}
