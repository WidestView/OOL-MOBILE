package com.example.ool_mobile.ui.util.form;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.ool_mobile.ui.component.date_dialog.DialogContent;
import com.example.ool_mobile.ui.util.UiDate;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class DateDialogContent implements DialogContent<Date> {

    private MaterialDatePicker<Long> picker;

    @NonNull
    private final AtomicBoolean activated = new AtomicBoolean();

    @NonNull
    private Consumer<Date> listener = v -> {
    };

    private final EditText editText;

    private final CharSequence title;

    private final FragmentManager fragmentManager;

    public DateDialogContent(
            @NonNull EditText editText,
            @Nullable CharSequence title,
            @NonNull FragmentManager manager
    ) {

        Objects.requireNonNull(editText, "editText is null");
        Objects.requireNonNull(manager, "manager is null");

        this.editText = editText;
        this.title = title;
        this.fragmentManager = manager;

        picker = setupDefaultDatePicker();
    }

    @NonNull
    private MaterialDatePicker<Long> setupDefaultDatePicker() {
        return setupDatePicker(MaterialDatePicker.todayInUtcMilliseconds());
    }

    private MaterialDatePicker<Long> setupDatePicker(long initialDate) {

        Objects.requireNonNull(editText, "editText is null");

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder
                .datePicker()
                .setSelection(initialDate)
                .setTitleText(title)
                .build();

        picker.addOnPositiveButtonClickListener(dialog -> {
            editText.setText(picker.getHeaderText());
            editText.setError(null);

            if (picker.getSelection() == null) {
                listener.accept(null);
            } else {
                listener.accept(new Date(picker.getSelection()));
            }
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
    public Date getSelection() {
        if (activated.get() && picker.getSelection() != null) {
            return new Date(picker.getSelection());
        } else {
            return null;
        }
    }

    @NonNull
    public EditText getEditText() {
        return editText;
    }

    public void setSelection(@Nullable Date startTime) {

        if (startTime == null) {
            editText.setText(null);

            picker = setupDefaultDatePicker();
        } else {
            editText.setText(new UiDate(editText.getContext()).formatDateName(startTime));

            picker = setupDatePicker(startTime.getTime());
        }
    }

    public void setSelectionListener(@NonNull Consumer<Date> listener) {
        this.listener = listener;
    }
}
