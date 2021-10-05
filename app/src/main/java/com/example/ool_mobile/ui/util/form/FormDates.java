package com.example.ool_mobile.ui.util.form;

import android.content.Context;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.example.ool_mobile.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class FormDates {

    @NonNull
    private final Context context;

    @NonNull
    private final FragmentManager fragmentManager;
    private final AtomicBoolean startPickerActivated = new AtomicBoolean(false);
    private final AtomicBoolean endPickerActivated = new AtomicBoolean(false);
    private final AtomicBoolean datePickerActivated = new AtomicBoolean(false);
    private MaterialTimePicker startTimePicker;
    private MaterialTimePicker endTimePicker;
    private MaterialDatePicker<Long> datePicker;

    public FormDates(@NonNull Context context, @NonNull FragmentManager fragmentManager) {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(fragmentManager, "fragmentManager is null");

        this.context = context.getApplicationContext();
        this.fragmentManager = fragmentManager;
    }

    public void setupStartTime(@NonNull EditText editText) {

        startTimePicker = setupTimePicker(
                R.string.label_select_start_time,
                editText,
                startPickerActivated
        );
    }

    public void setupEndTime(@NonNull EditText editText) {

        endTimePicker = setupTimePicker(
                R.string.label_select_end_time,
                editText,
                endPickerActivated
        );
    }

    public void setupDate(@NonNull EditText editText) {
        datePicker = setupDatePicker(editText);
    }

    @Nullable
    public FormTime getStartTime() {
        if (startPickerActivated.get()) {
            return ImmutableFormTime.of(
                    startTimePicker.getHour(),
                    startTimePicker.getMinute()
            );
        } else {
            return null;
        }
    }


    @Nullable
    public FormTime getEndTime() {
        if (endPickerActivated.get()) {
            return ImmutableFormTime.of(
                    endTimePicker.getHour(),
                    endTimePicker.getMinute()
            );
        } else {
            return null;
        }
    }

    @NonNull
    private MaterialTimePicker setupTimePicker(
            @StringRes int title,
            @NonNull EditText editText,
            @NonNull AtomicBoolean activated
    ) {

        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setTitleText(title)
                .setMinute(12)
                .build();

        picker.addOnDismissListener(dialog -> {
            editText.setText(
                    String.format(
                            context.getString(R.string.format_hour),
                            picker.getHour(), picker.getMinute()
                    )
            );
            editText.setError(null);
        });

        editText.setOnClickListener(v -> {

            if (!picker.isAdded()) {
                picker.show(fragmentManager, "start");
                activated.set(true);
            }
        });

        return picker;
    }

    private MaterialDatePicker<Long> setupDatePicker(@NonNull EditText editText) {

        Objects.requireNonNull(editText, "editText is null");

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder
                .datePicker()
                .setSelection(
                        MaterialDatePicker.todayInUtcMilliseconds()
                )
                .setTitleText(R.string.label_select_end_time)
                .build();

        picker.addOnPositiveButtonClickListener(dialog -> editText.setText(picker.getHeaderText()));

        picker.addOnDismissListener(dialog -> {
            editText.setText(picker.getHeaderText());
            editText.setError(null);
        });


        editText.setOnClickListener(v -> {
            if (!picker.isAdded()) {
                picker.show(fragmentManager, "date");
                datePickerActivated.set(true);
            }
        });
        return picker;
    }

    @Nullable
    public Long getDate() {
        if (datePickerActivated.get()) {
            return datePicker.getSelection();
        } else {
            return null;
        }
    }


}
