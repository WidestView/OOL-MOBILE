package com.example.ool_mobile.ui.component.date_dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.R;
import com.example.ool_mobile.ui.util.form.DateDialogContent;

import java.util.Objects;

public class DateDialogView extends LinearLayout {

    private DateDialogContent content;

    private EditText editText;

    private DialogArguments arguments;


    public DateDialogView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(
                Objects.requireNonNull(context, "context is null"),
                attrs
        );

        setupViews();

        setupArguments(context, attrs);

        if (!isInEditMode()) {
            Objects.requireNonNull(attrs, "attrs is null");

            setupContent();
        }
    }


    private void setupViews() {

        inflate(getContext(), R.layout.component_date_field, this);

        editText = findViewById(R.id.dateDialogView_editText);

        Objects.requireNonNull(editText, "inflated editText is null");
    }

    private void setupArguments(@NonNull Context context, @Nullable AttributeSet attrs) {

        arguments = DialogArguments.from(context, attrs);

        setHint(arguments.getHint());
    }


    private void setupContent() {

        content = new DateDialogContent(
                editText,
                arguments.getHint(),
                Util.fragmentManagerFromContext(getContext())
        );
    }

    @NonNull
    public EditText getEditText() {
        return editText;
    }

    public void setHint(@Nullable CharSequence sequence) {
        editText.setHint(sequence);
    }

    @Nullable
    public CharSequence getTitle() {
        editText.setHint(null);
        return arguments.getTitle();
    }

    @Nullable
    public CharSequence getHint() {
        return editText.getHint();
    }

    @NonNull
    public DateDialogContent getContent() {
        return content;
    }
}
