package com.example.ool_mobile.ui.util.form;

import android.content.Context;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.ool_mobile.R;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class DialogTimeField {

    private MaterialTimePicker picker;

    @NonNull
    private final AtomicBoolean activated = new AtomicBoolean();

    private final Context context;

    @NonNull
    private final EditText editText;

    private final int title;

    private final FragmentManager fragmentManager;

    public DialogTimeField(@NonNull EditText editText, int message, @NonNull FragmentManager manager) {

        Objects.requireNonNull(editText, "editText is null");
        Objects.requireNonNull(manager, "manager is null");

        this.editText = editText;
        this.title = message;
        this.fragmentManager = manager;

        context = editText.getContext().getApplicationContext();

        picker = setupTimePicker();
    }

    @NonNull
    public MaterialTimePicker setupTimePicker() {
        return setupTimePicker(ImmutableFormTime.of(12, 12));
    }

    @NonNull
    private MaterialTimePicker setupTimePicker(@NonNull FormTime initialTime) {

        Objects.requireNonNull(initialTime, "initialTime is null");

        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour((int) initialTime.getHour())
                .setTitleText(title)
                .setMinute((int) initialTime.getMinute())
                .build();

        picker.addOnDismissListener(dialog -> {

            FormTime time = getFormTime();

            editText.setText(formatFormTime(time));

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

    @NotNull
    private String formatFormTime(FormTime time) {
        return String.format(
                context.getString(R.string.format_hour),
                time.getHour(), time.getMinute()
        );
    }

    @Nullable
    public FormTime getFormTime() {
        if (activated.get()) {
            return ImmutableFormTime.of(
                    picker.getHour(),
                    picker.getMinute()
            );
        } else {
            return null;
        }
    }

    @NonNull
    public EditText getEditText() {
        return editText;
    }

    public void setTime(@NonNull FormTime formTime) {

        picker = setupTimePicker(formTime);

        editText.setText(formatFormTime(formTime));
    }
}
