package com.example.ool_mobile.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.R;

import static com.example.ool_mobile.ui.components.ContentRow.setupText;
import static java.util.Objects.requireNonNull;

public class ContentRowField extends LinearLayout {

    @NonNull
    private final TextView labelTextView;

    @NonNull
    private final TextView valueTextView;

    public ContentRowField(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.content_row_field, this);

        labelTextView = requireNonNull(findViewById(R.id.contentRowField_labelTextView));
        valueTextView = requireNonNull(findViewById(R.id.contentRowField_valueTextView));

        setupArguments(context, attrs);
    }

    private void setupArguments(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(
                attrs, new int[]{R.attr.label, R.attr.value}
        );

        try {
            setupText(array, this.labelTextView, 0);
            setupText(array, this.valueTextView, 1);
        }
        finally {
            array.recycle();
        }
    }


    @NonNull
    public TextView getLabelTextView() {
        return labelTextView;
    }

    @NonNull
    public TextView getValueTextView() {
        return valueTextView;
    }
}
