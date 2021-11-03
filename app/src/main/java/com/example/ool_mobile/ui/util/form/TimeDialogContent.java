package com.example.ool_mobile.ui.util.form;

import android.content.Context;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.ool_mobile.R;
import com.example.ool_mobile.ui.component.date_dialog.DialogContent;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class TimeDialogContent implements DialogContent<FormTime> {

    private MaterialTimePicker picker;

    @NonNull
    private final AtomicBoolean activated = new AtomicBoolean();

    private final Context context;

    @NonNull
    private final EditText editText;

    private Consumer<FormTime> listener = v -> {
    };

    private final CharSequence title;

    private final FragmentManager fragmentManager;

    public TimeDialogContent(
            @NonNull EditText editText,
            @Nullable CharSequence title,
            @NonNull FragmentManager manager
    ) {

        Objects.requireNonNull(editText, "editText is null");
        Objects.requireNonNull(manager, "manager is null");

        this.editText = editText;
        this.title = title;
        this.fragmentManager = manager;

        context = editText.getContext().getApplicationContext();

        picker = setupTimePicker();
    }

    @NonNull
    public MaterialTimePicker setupTimePicker() {
        return setupTimePicker(ImmutableFormTime.of(12, 12));
    }

    @NonNull
    private MaterialTimePicker setupTimePicker(@Nullable FormTime initialTime) {

        Objects.requireNonNull(initialTime, "initialTime is null");

        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour((int) initialTime.getHour())
                .setTitleText(title)
                .setMinute((int) initialTime.getMinute())
                .build();

        picker.addOnPositiveButtonClickListener(dialog -> {

            FormTime time = getSelection();

            if (time != null) {
                editText.setText(formatFormTime(time));

                editText.setError(null);
            }

            listener.accept(time);
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
    @Override
    public FormTime getSelection() {
        if (activated.get()) {
            return ImmutableFormTime.of(
                    picker.getHour(),
                    picker.getMinute()
            );
        } else {
            return null;
        }
    }

    @Override
    public void setSelection(@Nullable FormTime formTime) {

        if (formTime == null) {
            picker = setupTimePicker();

            editText.setText(null);
        } else {
            picker = setupTimePicker(formTime);

            editText.setText(formatFormTime(formTime));
        }
    }

    @NonNull
    @Override
    public EditText getEditText() {
        return editText;
    }

    @Override
    public void setSelectionListener(@NonNull Consumer<FormTime> listener) {
        this.listener = listener;
    }
}