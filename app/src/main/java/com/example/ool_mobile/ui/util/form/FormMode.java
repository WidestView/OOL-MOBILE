package com.example.ool_mobile.ui.util.form;

import androidx.annotation.NonNull;

import java.io.Serializable;

public interface FormMode extends Serializable {

    FormMode Add = Visitor::visitAdd;

    FormMode Update = Visitor::visitUpdate;

    @NonNull
    String BUNDLE_KEY = "form-mode";

    void accept(@NonNull Visitor visitor);

    interface Visitor {
        void visitAdd();

        void visitUpdate();
    }
}


