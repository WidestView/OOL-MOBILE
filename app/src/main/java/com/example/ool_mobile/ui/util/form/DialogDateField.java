package com.example.ool_mobile.ui.util.form;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.ool_mobile.ui.util.UiDate;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class DialogDateField {

    private MaterialDatePicker<Long> picker;

    @NonNull
    private final AtomicBoolean activated = new AtomicBoolean();

    @NonNull
    private final EditText editText;

    private final int title;

    private final FragmentManager fragmentManager;

    public DialogDateField(@NonNull EditText editText, int message, @NonNull FragmentManager manager) {

        Objects.requireNonNull(editText, "editText is null");
        Objects.requireNonNull(manager, "manager is null");

        this.editText = editText;
        this.title = message;
        this.fragmentManager = manager;

        picker = setupTimePicker();
    }

    @NonNull
    public MaterialDatePicker<Long> setupTimePicker() {
        return setupDatePicker(MaterialDatePicker.todayInUtcMilliseconds());
    }

    private MaterialDatePicker<Long> setupDatePicker(long initialDate) {

        Objects.requireNonNull(editText, "editText is null");

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder
                .datePicker()
                .setSelection(initialDate)
                .setTitleText(title)
                .build();

        picker.addOnPositiveButtonClickListener(dialog -> editText.setText(picker.getHeaderText()));

        picker.addOnDismissListener(dialog -> {
            editText.setText(picker.getHeaderText());
            editText.setError(null);
        });

        editText.setOnClickListener(v -> {
            if (!picker.isAdded()) {
                picker.show(fragmentManager, "date");
                activated.set(true);
            }
        });

        return picker;
    }


    @Nullable
    public Long getDate() {
        if (activated.get()) {
            return picker.getSelection();
        } else {
            return null;
        }
    }

    @NonNull
    public EditText getEditText() {
        return editText;
    }

    public void setDate(@NonNull Date startTime) {

        editText.setText(new UiDate(editText.getContext()).formatDateName(startTime));

        picker = setupDatePicker(startTime.getTime());
    }
}
