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


        TypedArray array = context.obtainStyledAttributes(
                attributeSet, new int[]{
                        R.attr.hint,
                        android.R.attr.hint,
                        R.attr.title
                }
        );

        try {

            CharSequence hint = array.getText(0);
            CharSequence androidHint = array.getText(1);
            CharSequence title = array.getText(2);

            return new DialogArgumentsBuilder()
                    .hint(hint == null ? androidHint : hint)
                    .title(title)
                    .build();
        } finally {
            array.recycle();
        }
    }
}
