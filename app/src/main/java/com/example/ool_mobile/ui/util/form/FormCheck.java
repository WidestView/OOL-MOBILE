package com.example.ool_mobile.ui.util.form;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.function.Supplier;

public class FormCheck<Error> {

    @NonNull
    private final Supplier<CheckResult> check;

    private final Error error;

    private FormCheck(@NonNull Supplier<CheckResult> check, @NonNull Error error) {

        Objects.requireNonNull(check, "check is null");
        Objects.requireNonNull(error, "error is null");

        this.check = check;
        this.error = error;
    }

    @NonNull
    public static <Error> FormCheck<Error> failIf(@NonNull Supplier<Boolean> check, @NonNull Error error) {
        return new FormCheck<>(() -> check.get() ? CheckResult.Failure : CheckResult.Success, error);
    }

    @NonNull
    public static <Error> FormCheck<Error> succeedIf(@NonNull Supplier<Boolean> check, @NonNull Error error) {
        return new FormCheck<>(() -> check.get() ? CheckResult.Success : CheckResult.Failure, error);
    }

    @NonNull
    public CheckResult performCheck() {
        return check.get();
    }

    @NonNull
    public Error getError() {
        return error;
    }

}
