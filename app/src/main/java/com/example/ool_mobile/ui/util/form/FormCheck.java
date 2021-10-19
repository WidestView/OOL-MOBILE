package com.example.ool_mobile.ui.util.form;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class FormCheck<Error> {

    @NonNull
    private final Single<ValidationResult> check;

    private final Error error;

    private FormCheck(@NonNull Single<ValidationResult> check, @NonNull Error error) {

        Objects.requireNonNull(check, "check is null");
        Objects.requireNonNull(error, "error is null");

        this.check = check;
        this.error = error;
    }

    @NonNull
    public static <Error> FormCheck<Error> failIf(
            @NonNull Supplier<Boolean> check,
            @NonNull Error error
    ) {
        return new FormCheck<>(
                Single.fromSupplier(() ->
                        check.get()
                                ? ValidationResult.Failure
                                : ValidationResult.Success
                )
                , error
        );
    }

    @NonNull
    public static <Error> FormCheck<Error> succeedIf(
            @NonNull Supplier<Boolean> check,
            @NonNull Error error
    ) {
        return new FormCheck<>(
                Single.fromSupplier(() ->
                        check.get()
                                ? ValidationResult.Success
                                : ValidationResult.Failure
                )
                , error
        );
    }

    @NonNull
    public static <Error> FormCheck<Error> failIf(
            @NonNull Single<Boolean> check,
            @NonNull Error error
    ) {
        return new FormCheck<>(
                check.map(failure -> failure ? ValidationResult.Failure : ValidationResult.Success),
                error
        );
    }

    @NonNull
    public static <Error> FormCheck<Error> succeedIf(
            @NonNull Single<Boolean> check,
            @NonNull Error error
    ) {
        return new FormCheck<>(
                check.map(success -> success ? ValidationResult.Success : ValidationResult.Failure),
                error
        );
    }

    @NonNull
    public Single<ValidationResult> getCheck() {
        return check;
    }

    @NonNull
    public Error getError() {
        return error;
    }

    @NonNull
    public static <Error> Single<ValidationResult> validate(
            @NonNull List<FormCheck<Error>> checks,
            @NonNull Consumer<Error> consumer) {

        return validate(
                Observable.fromIterable(checks)
                        .observeOn(AndroidSchedulers.mainThread())
                , consumer
        );
    }

    @NonNull
    public static <Error> Single<ValidationResult> validate(
            @NonNull Observable<FormCheck<Error>> checks,
            @NonNull Consumer<Error> consumer
    ) {

        return checks.flatMapSingle(FormCheck::getCheck)
                .zipWith(checks.map(FormCheck::getError), Pair::create)
                .doOnNext(result -> {
                    if (result.first == ValidationResult.Failure) {
                        consumer.accept(result.second);
                    }
                })
                .map(result -> result.first)
                .reduce((a, b) ->
                        a == ValidationResult.Success && b == ValidationResult.Success
                                ? ValidationResult.Success
                                : ValidationResult.Failure
                )
                .switchIfEmpty(Maybe.just(ValidationResult.Success))
                .toSingle();


    }
}
