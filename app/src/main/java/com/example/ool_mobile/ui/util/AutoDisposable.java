package com.example.ool_mobile.ui.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class AutoDisposable implements LifecycleObserver {

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private AutoDisposable(@NonNull Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        subscriptions.clear();
    }

    public void add(@NonNull Disposable disposable) {
        subscriptions.add(disposable);
    }

    @NonNull
    public static AutoDisposable onStop(@NonNull Lifecycle lifecycle) {
        return new AutoDisposable(lifecycle);
    }
}
