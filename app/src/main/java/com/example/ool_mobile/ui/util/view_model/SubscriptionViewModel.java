package com.example.ool_mobile.ui.util.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.CompletableSubject;

public abstract class SubscriptionViewModel extends ViewModel {

    protected final CompositeDisposable subscriptions = new CompositeDisposable();

    private final CompletableSubject clearedEvent = CompletableSubject.create();

    @NonNull
    public <T> AutoDisposeConverter<T> disposedWhenCleared() {
        return AutoDispose.autoDisposable(clearedEvent);
    }

    @Override
    protected final void onCleared() {
        super.onCleared();
        subscriptions.clear();
        clearedEvent.onComplete();
    }
}
