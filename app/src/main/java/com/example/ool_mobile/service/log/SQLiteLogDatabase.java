package com.example.ool_mobile.service.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class SQLiteLogDatabase extends SQLiteOpenHelper implements LogDatabase {

    private final SQLiteDatabase database;

    public SQLiteLogDatabase(@NonNull Context context) {
        super(context, "ool_logs", null, 2);

        this.database = getWritableDatabase();
    }

    @Override
    public void onCreate(@Nullable SQLiteDatabase database) {

        Objects.requireNonNull(database);

        database.execSQL(
                "" +
                        "create table Logs (" +
                        "   priority text," +
                        "   epochMilliseconds number," +
                        "   tag text," +
                        "   message text," +
                        "   exceptionText text" +
                        ");"
        );

    }

    @Override
    public void onUpgrade(@Nullable SQLiteDatabase database, int oldVersion, int newVersion) {

        Objects.requireNonNull(database);

        database.execSQL("drop table if exists Logs");

        onCreate(database);
    }


    @Override
    public void addEntry(@NonNull LogEntry entry) {

        Objects.requireNonNull(entry);

        ContentValues values = new ContentValues();

        values.put("priority", entry.priority());
        values.put("epochMilliseconds", entry.date().getTime());
        values.put("tag", entry.tag());
        values.put("message", entry.text());
        values.put("exceptionText", entry.exceptionMessage());

        database.insert("Logs", null, values);

    }

    @NonNull
    @Override
    public Single<List<LogEntry>> listEntries() {

        return Observable.<LogEntry>create(emitter -> {

            try (Cursor cursor = database.rawQuery("select * from Logs", null)) {

                while (cursor.moveToNext()) {

                    emitter.onNext(
                            ImmutableLogEntry.builder()
                                    .priority(cursor.getString(0))
                                    .date(new Date(cursor.getLong(1)))
                                    .tag(cursor.getString(2))
                                    .text(cursor.getString(3))
                                    .exceptionMessage(cursor.getString(4))
                                    .build()
                    );

                }
            }

        }).toList();
    }


}
