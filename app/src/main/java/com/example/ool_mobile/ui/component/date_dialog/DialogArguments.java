package com.example.ool_mobile.ui.component.date_dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.R;

import org.immutables.value.Value;

import java.util.Objects;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PRIVATE)
interface DialogArguments {

    @Nullable
    CharSequence getTitle();

    @Nullable
    CharSequence getHint();

    @NonNull
    static DialogArguments from(@NonNull Context context, @Nullable AttributeSet attributeSet) {

        Objects.requireNonNull(context, "context is null");

        if (attributeSet == null) {
            return new DialogArgumentsBuilder().build();
        }

        CharSequence hint;
        CharSequence title;

        TypedArray array = context.obtainStyledAttributes(
                attributeSet, new int[]{
                        R.attr.hint,
                        R.attr.title
                }
        );

        try {
            hint = array.getText(0);
            title = array.getText(1);

            return new DialogArgumentsBuilder()
                    .hint(hint)
                    .title(title)
                    .build();
        } finally {
            array.recycle();
        }
    }
}
