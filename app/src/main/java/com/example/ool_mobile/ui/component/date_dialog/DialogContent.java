package com.example.ool_mobile.ui.component.date_dialog;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.function.Consumer;

public interface DialogContent<Selection> {

    @Nullable
    Selection getSelection();

    void setSelection(@Nullable Selection selection);

    @NonNull
    EditText getEditText();

    void setSelectionListener(@NonNull Consumer<Selection> listener);
}
