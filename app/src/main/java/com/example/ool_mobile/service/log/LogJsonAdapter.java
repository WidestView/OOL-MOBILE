package com.example.ool_mobile.service.log;

import androidx.annotation.NonNull;

import com.example.ool_mobile.ui.log_export.ImmutableLogExport;
import com.example.ool_mobile.ui.log_export.LogExport;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

public class LogJsonAdapter {

    @NonNull
    @ToJson
    public ImmutableLogExport logExportToJson(@NonNull LogExport logExport) {
        return ImmutableLogExport.builder().from(logExport).build();
    }

    @NonNull
    @FromJson
    public LogExport logExportToJson(@NonNull ImmutableLogExport export) {
        return export;
    }

    @NonNull
    @ToJson
    public ImmutableLogExport.DeviceInfo deviceInfoToJson(@NonNull LogExport.DeviceInfo deviceInfo) {
        return ImmutableLogExport.DeviceInfo.builder().from(deviceInfo).build();
    }

    @NonNull
    @FromJson
    public LogExport.DeviceInfo deviceInfoFromJson(@NonNull ImmutableLogExport.DeviceInfo deviceInfo) {
        return deviceInfo;
    }

    @NonNull
    @ToJson
    public ImmutableLogEntry logEntryToJson(@NonNull LogEntry logEntry) {
        return ImmutableLogEntry.builder().from(logEntry).build();
    }

    @NonNull
    @FromJson
    public LogEntry logEntryFromJson(@NonNull ImmutableLogEntry logEntry) {
        return logEntry;
    }

}
