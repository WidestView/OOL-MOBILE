package com.example.ool_mobile.ui.component.date_dialog;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.R;
import com.example.ool_mobile.ui.util.form.TimeDialogContent;

import java.util.Objects;

public class TimeDialogView extends LinearLayout {

    private TimeDialogContent content;

    private EditText editText;

    private DialogArguments arguments;


    public TimeDialogView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(
                Objects.requireNonNull(context, "context is null"),
                Objects.requireNonNull(attrs, "attrs is null")
        );

        setupViews();

        setupArguments(context, attrs);

        if (!isInEditMode()) {
            setupContent();
        }
    }


    private void setupViews() {

        inflate(getContext(), R.layout.component_date_field, this);

        editText = findViewById(R.id.dateDialogView_editText);

        Objects.requireNonNull(editText, "inflated editText is null");
    }

    private void setupContent() {

        this.content = new TimeDialogContent(
                editText,
                arguments.getTitle(),
                Util.fragmentManagerFromContext(getContext())
        );
    }

    private void setupArguments(@NonNull Context context, @Nullable AttributeSet attrs) {
        arguments = DialogArguments.from(context, attrs);

        setHint(arguments.getHint());
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
        return arguments.getTitle();
    }

    @Nullable
    public CharSequence getHint() {
        return editText.getHint();
    }

    @NonNull
    public TimeDialogContent getContent() {
        return content;
    }
}
