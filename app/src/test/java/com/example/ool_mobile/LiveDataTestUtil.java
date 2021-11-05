package com.example.ool_mobile;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class LiveDataTestUtil {

    @Nullable
    public static <T> T observeWithTimeout(LiveData<T> liveData) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        AtomicReference<T> result = new AtomicReference<>();

        liveData.observeForever(value -> {
            result.set(value);
            latch.countDown();
        });

        int timeout = 5;
        TimeUnit unit = TimeUnit.SECONDS;

        if (!latch.await(timeout, unit)) {
            throw new IllegalStateException("Timeout out " + timeout + " " + unit);
        }

        return result.get();
    }
}
