package com.example.ool_mobile.ui.log_export

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.CheckResult
import com.example.ool_mobile.service.EmployeeRepository
import com.example.ool_mobile.service.log.LogEntry
import com.squareup.moshi.JsonAdapter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class LogWriter(
        private val context: Context,
        private val employeeRepository: EmployeeRepository,
        private val adapter: JsonAdapter<LogExport>
) {
    @CheckResult
    fun writeLogEntries(entries: List<LogEntry>): Completable {

        return employeeRepository.currentEmployee
                .toSingle()
                .map { it.cpf() }
                .map<LogExport> { id ->
                    ImmutableLogExport.builder()
                            .id(UUID.randomUUID())
                            .allegedAuthorId(id)
                            .deviceInfo(getDeviceInfo())
                            .date(Date())
                            .entries(entries)
                            .build()
                }
                .flatMapCompletable { export ->
                    writeLogExport(export)
                }


    }

    private fun getDeviceInfo(): LogExport.DeviceInfo {
        return ImmutableLogExport.DeviceInfo.builder()
                .androidRelease(Build.VERSION.RELEASE)
                .sdkVersion(Build.VERSION.SDK_INT)
                .buildModel(Build.MODEL)
                .build()
    }

    @SuppressLint("SimpleDateFormat")
    private fun writeLogExport(export: LogExport): Completable = Completable.fromAction {

        val file = resolveFile(export)

        ensureNewFile(file)

        val output: OutputStream = FileOutputStream(file)

        output.write(adapter.toJson(export).toByteArray())

    }.subscribeOn(Schedulers.io())

    private fun ensureNewFile(file: File) {
        if (file.exists()) {
            if (!file.delete()) {
                throw IOException("Failed to delete file")
            }
        }

        if (!file.createNewFile()) {
            throw IOException("Failed to create new file")
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun resolveFile(export: LogExport): File {

        val filesDir = context.getExternalFilesDir(null)
                ?: throw IOException("The android external directory is not available")

        val date = SimpleDateFormat("yyyy-mm-dd").format(export.date())

        val fileName = "ool-log-$date.json"

        return File(filesDir, fileName)
    }

}