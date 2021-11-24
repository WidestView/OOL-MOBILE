package com.example.ool_mobile.ui.log_export;

import androidx.annotation.NonNull;

import com.example.ool_mobile.service.log.LogEntry;

import org.immutables.value.Value;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Value.Immutable
@Value.Enclosing
public interface LogExport {

    @NonNull
    UUID id();

    @NonNull
    String allegedAuthorId();

    @NonNull
    DeviceInfo deviceInfo();

    @NonNull
    Date date();

    @NonNull
    List<LogEntry> entries();

    @Value.Immutable
    interface DeviceInfo {

        @NonNull
        String androidRelease();

        @NonNull
        String buildModel();

        int sdkVersion();
    }
}

