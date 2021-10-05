package com.example.ool_mobile.ui.form.add_photo_shoot;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.form.FormDates;
import com.example.ool_mobile.ui.util.form.FormMode;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;

public class PhotoshootFormActivity extends AppCompatActivity implements
        PhotoshootFormViewModel.Event.Visitor {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView titleTextView;
    private EditText startTimeEditText;
    private EditText endTimeEditText;
    private EditText dateEditText;
    private EditText addressEditText;
    private EditText orderEditText;
    private PhotoshootFormViewModel viewModel;
    private FormDates dates;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_shoot);

        setupViews();

        setupDateViews();

        viewModel = new ViewModelProvider(
                this,
                PhotoshootFormViewModel.create(
                        loadFormMode(),
                        Dependencies.from(this).getPhotoshootApi()
                )
        ).get(PhotoshootFormViewModel.class);
    }

    private FormMode loadFormMode() {
        int value = getIntent().getIntExtra(
                FormMode.BUNDLE_KEY,
                FormMode.Add.asInteger()
        );

        FormMode formMode = FormMode.fromInteger(value);

        if (formMode == FormMode.Update) {
            titleTextView.setText(R.string.label_update_photoshoot);
        }

        return formMode;
    }

    @Override
    protected void onStart() {
        super.onStart();

        compositeDisposable.add(
                viewModel.getEvents().subscribe(
                        event -> event.accept(this)
                )
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }


    private void setupDateViews() {

        dates = new FormDates(this, getSupportFragmentManager());

        dates.setupStartTime(startTimeEditText);

        dates.setupEndTime(endTimeEditText);

        dates.setupDate(dateEditText);
    }

    private void setupViews() {
        startTimeEditText = findViewById(R.id.addPhotoShoot_startTimeEditText);
        endTimeEditText = findViewById(R.id.addPhotoShoot_endTimeEditText);
        dateEditText = findViewById(R.id.addPhotoShoot_dateEditText);
        titleTextView = findViewById(R.id.addPhotoshootActivity_titleTextView);
        addressEditText = findViewById(R.id.addPhotoshootActivity_addressEditText);
        orderEditText = findViewById(R.id.addPhotoshootActivity_orderEditText);

        findViewById(R.id.addPhotoShoot_backImageView).setOnClickListener(v ->
                onBackPressed()
        );


        findViewById(R.id.addPhotoShoot_saveButton).setOnClickListener(v ->
                onSaveButtonClick()
        );
    }

    private void onSaveButtonClick() {

        viewModel.savePhotoshoot(
                ImmutablePhotoshootInput.builder()
                        .address(addressEditText.getText().toString())
                        .startTime(dates.getStartTime())
                        .endTime(dates.getEndTime())
                        .date(dates.getDate())
                        .orderId(orderEditText.getText().toString())
                        .build()
        );
    }

    @Override
    public void visitSuccess() {
        finish();
    }

    @Override
    public void visitError() {
        snack(PhotoshootFormActivity.this, R.string.error_operationFailed);
    }

    @Override
    public void visitEmptyAddress() {
        addressEditText.setError(getString(R.string.error_empty_address));
    }

    @Override
    public void visitInvalidTimeRange() {
        startTimeEditText.setError(getString(R.string.error_invalidTimeRange));
        endTimeEditText.setError(getString(R.string.error_invalidTimeRange));
    }

    @Override
    public void visitEmptyOrder() {
        orderEditText.setError(getString(R.string.error_emptyOrderId));
    }

    @Override
    public void visitEmptyStartTime() {
        startTimeEditText.setError(getString(R.string.error_emptyStartTime));
    }

    @Override
    public void visitEmptyEndTime() {
        endTimeEditText.setError(getString(R.string.error_emptyEndTime));
    }

    @Override
    public void visitInvalidOrder() {
        orderEditText.setError(getString(R.string.error_invalidOrder));
    }

    @Override
    public void visitEmptyDate() {
        dateEditText.setError(getString(R.string.error_empty_date));
    }


}