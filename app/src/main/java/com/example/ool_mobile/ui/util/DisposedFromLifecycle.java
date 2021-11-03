package com.example.ool_mobile.ui.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.Objects;

import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

import static autodispose2.AutoDispose.autoDisposable;

public class DisposedFromLifecycle {

    @NonNull
    public static AndroidLifecycleScopeProvider disposedFrom(@NonNull LifecycleOwner owner) {

        Objects.requireNonNull(owner, "owner is null");

        return AndroidLifecycleScopeProvider.from(owner);
    }

    @NonNull
    public static <T> AutoDisposeConverter<T> of(@NonNull LifecycleOwner owner) {

        Objects.requireNonNull(owner);

        return autoDisposable(disposedFrom(owner));
    }
}
