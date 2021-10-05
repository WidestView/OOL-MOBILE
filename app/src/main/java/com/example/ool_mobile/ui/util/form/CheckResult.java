package com.example.ool_mobile.ui.util.form;

import androidx.annotation.NonNull;

public interface CheckResult {

    CheckResult Success = Visitor::success;
    CheckResult Failure = Visitor::failure;

    void accept(@NonNull Visitor visitor);

    interface Visitor {
        void success();

        void failure();
    }
}
