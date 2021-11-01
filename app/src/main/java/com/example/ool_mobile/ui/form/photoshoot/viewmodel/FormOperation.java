package com.example.ool_mobile.ui.form.photoshoot.viewmodel;

import androidx.annotation.NonNull;

import com.example.ool_mobile.ui.util.form.FormMode;

import io.reactivex.rxjava3.annotations.CheckReturnValue;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface FormOperation<Input> {

    @NonNull
    FormMode getFormMode();

    @NonNull
    @CheckReturnValue
    Single<Input> getInput();

    @NonNull
    @CheckReturnValue
    Completable savePhotoshoot();
}
