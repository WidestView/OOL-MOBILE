package com.example.ool_mobile.service.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

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


}
