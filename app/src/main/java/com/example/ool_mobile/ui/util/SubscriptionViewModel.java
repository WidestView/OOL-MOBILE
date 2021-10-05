package com.example.ool_mobile.ui.util;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public abstract class SubscriptionViewModel extends ViewModel {

    protected final CompositeDisposable subscriptions = new CompositeDisposable();

    @Override
    protected void onCleared() {
        super.onCleared();
        subscriptions.clear();
    }
}
