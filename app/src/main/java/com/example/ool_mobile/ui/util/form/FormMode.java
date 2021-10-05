package com.example.ool_mobile.ui.util.form;

import androidx.annotation.NonNull;

public interface FormMode {

    FormMode Add = new FormMode() {

        @Override
        public void accept(@NonNull Visitor visitor) {
            visitor.visitAdd();
        }

        @Override
        public int asInteger() {
            return FormModeValues.MODE_ADD;
        }
    };

    FormMode Update = new FormMode() {

        @Override
        public void accept(@NonNull Visitor visitor) {
            visitor.visitUpdate();
        }

        @Override
        public int asInteger() {
            return FormModeValues.MODE_UPDATE;
        }
    };

    FormMode Display = new FormMode() {
        @Override
        public void accept(@NonNull Visitor visitor) {
            if (visitor instanceof ExtendedVisitor) {
                ((ExtendedVisitor) visitor).visitDisplay();
            }
        }

        @Override
        public int asInteger() {
            return 0;
        }
    };

    @NonNull
    String BUNDLE_KEY = "form-mode";

    @NonNull
    static FormMode fromInteger(int value) {
        switch (value) {
            case FormModeValues.MODE_ADD:
                return FormMode.Add;
            case FormModeValues.MODE_UPDATE:
                return FormMode.Update;
            case FormModeValues.MODE_DISPLAY:
                return FormMode.Display;
            default:
                throw new IllegalArgumentException(value + " is not a valid FormMode value");
        }
    }

    void accept(@NonNull Visitor visitor);

    int asInteger();

    interface Visitor {
        void visitAdd();

        void visitUpdate();
    }

    interface ExtendedVisitor extends Visitor {
        void visitDisplay();
    }
}

class FormModeValues {
    static final int MODE_ADD = 330;
    static final int MODE_UPDATE = 770;
    static final int MODE_DISPLAY = 346;
}

