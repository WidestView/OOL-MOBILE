package com.example.ool_mobile.service.log;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LogDatabaseTreeTest {

    @Mock
    public LogDatabase database;

    @Captor
    public ArgumentCaptor<LogEntry> captor;

    private LogDatabaseTree tree;

    @Before
    public void setUp() {
        tree = new LogDatabaseTree(database);
    }

    @Test
    public void addsEntriesWhenLogs() throws Exception {

        Date startDate = new Date();

        String debugMessage = "A debug message";

        Thread.sleep(10);

        tree.d(debugMessage);

        verify(database).addEntry(captor.capture());

        LogEntry entry = captor.getValue();

        assertThat(entry.exceptionMessage())
                .isNull();

        assertThat(entry.date())
                .isGreaterThan(startDate);

        assertThat(entry.priority())
                .isEqualTo(LogEntry.priorityString(Log.DEBUG));

        assertThat(entry.text())
                .contains(debugMessage);
    }

    public void closesDatabase() {

    }

}