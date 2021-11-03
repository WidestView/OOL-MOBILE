package com.example.ool_mobile.ui.util.form;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class FormModeValue {

    private static final int MODE_ADD = 1;
    private static final int MODE_UPDATE = 2;

    public static int of(@NonNull FormMode formMode) {
        Objects.requireNonNull(formMode, "formMode is null");

        AtomicInteger result = new AtomicInteger();

        formMode.accept(new FormMode.Visitor() {
            @Override
            public void visitAdd() {
                result.set(MODE_ADD);
            }

            @Override
            public void visitUpdate() {
                result.set(MODE_UPDATE);
            }
        });

        if (result.get() == 0) {
            throw new UnsupportedOperationException("Unsupported form mode " + formMode);
        }

        return result.get();
    }

    @NonNull
    public static FormMode convert(int value) {

        switch (value) {
            case MODE_ADD:
                return FormMode.Add;
            case MODE_UPDATE:
                return FormMode.Update;
            default:
                throw new UnsupportedOperationException("Unsupported value " + value);
        }
    }
}
