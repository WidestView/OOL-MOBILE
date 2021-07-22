package com.example.ool_mobile.ui.add_photo_shoot;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.ool_mobile.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class AddPhotoShootActivity extends AppCompatActivity {

    TextView startTimeEditText;
    TextView endTimeEditText;
    TextView dateEditText;

    MaterialTimePicker startTimePicker;
    MaterialTimePicker endTimePicker;
    MaterialDatePicker<Pair<Long, Long>> datePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_shoot);

        setupViews();

        startTimePicker = setupTimePicker(R.string.select_start_time, startTimeEditText);
        endTimePicker = setupTimePicker(R.string.select_end_time, endTimeEditText);

        datePicker = setupDatePicker();
    }

    private void setupViews() {
        startTimeEditText = findViewById(R.id.addPhotoShoot_startTimeEditText);
        endTimeEditText = findViewById(R.id.addPhotoShoot_endTimeEditText);
        dateEditText = findViewById(R.id.addPhotoShoot_dateEditText);


        findViewById(R.id.addPhotoShoot_saveButton).setOnClickListener(v ->
                onSaveButtonClick()
        );
    }

    private MaterialTimePicker setupTimePicker(@StringRes int title, TextView textView) {

        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setTitleText(title)
                .setMinute(12)
                .build();

        picker.addOnDismissListener(dialog -> textView.setText(
                String.format(
                        getString(R.string.hour_format),
                        picker.getHour(), picker.getMinute()
                )
        ));

        textView.setOnClickListener(v ->
                picker.show(getSupportFragmentManager(), "start")
        );

        return picker;
    }

    private MaterialDatePicker<Pair<Long, Long>> setupDatePicker() {

        MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setSelection(
                        new Pair<>(
                                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                                MaterialDatePicker.todayInUtcMilliseconds()
                        )
                )
                .setTitleText(R.string.select_end_time)
                .build();

        picker.addOnDismissListener(dialog -> {

            dateEditText.setText(picker.getHeaderText());

        });


        dateEditText.setOnClickListener(v ->
                picker.show(getSupportFragmentManager(), "date")
        );

        return picker;
    }

    private void onSaveButtonClick() {
        int startTimeMinutes = startTimePicker.getHour() * 60 + startTimePicker.getMinute();
        int endTimeMinutes = endTimePicker.getHour() * 60 + endTimePicker.getMinute();

        if (endTimeMinutes < startTimeMinutes) {
            startTimeEditText.setError(getString(R.string.error_insert_valid_time_range));
            endTimeEditText.setError(getString(R.string.error_insert_valid_time_range));
        }
    }

}