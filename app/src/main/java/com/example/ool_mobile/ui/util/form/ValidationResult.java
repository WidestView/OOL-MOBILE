package com.example.ool_mobile.ui.util.form;

import androidx.annotation.NonNull;

public interface ValidationResult {

    ValidationResult Success = Visitor::success;
    ValidationResult Failure = Visitor::failure;

    void accept(@NonNull Visitor visitor);

    interface Visitor {
        void success();

        void failure();
    }
}
