package com.example.ool_mobile.ui.form.add_photo_shoot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.form.DialogDateField;
import com.example.ool_mobile.ui.util.form.DialogTimeField;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormTime;

import java.util.UUID;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;

public class PhotoshootFormActivity extends AppCompatActivity implements
        PhotoshootViewModel.Event.Visitor,
        FormMode.Visitor {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView titleTextView;
    private EditText addressEditText;
    private EditText orderEditText;
    private PhotoshootViewModel viewModel;

    private DialogTimeField startTimeField;
    private DialogTimeField endTimeField;
    private DialogDateField dateField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_photoshoot);

        setupViews();

        setupViewModel();
    }

    private void setupViews() {

        startTimeField = new DialogTimeField(
                findViewById(R.id.addPhotoShoot_startTimeEditText),
                R.string.label_select_start_time,
                getSupportFragmentManager()
        );

        endTimeField = new DialogTimeField(
                findViewById(R.id.addPhotoShoot_endTimeEditText),
                R.string.label_select_end_time,
                getSupportFragmentManager()
        );

        dateField = new DialogDateField(
                findViewById(R.id.addPhotoShoot_dateEditText),
                R.string.label_select_end_time,
                getSupportFragmentManager()
        );

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

    private void setupViewModel() {
        viewModel = new ViewModelProvider(
                this,
                PhotoshootViewModel.create(
                        getFormMode(), Dependencies.from(this).getPhotoshootApi(),
                        getResourceId()
                )
        ).get(PhotoshootViewModel.class);

        viewModel.getFormMode().observe(this, formMode -> formMode.accept(this));

        viewModel.getInitialPhotoshoot().observe(this, this::displayPhotoshoot);
    }


    @Nullable
    private UUID getResourceId() {

        Intent intent = getIntent();
        String id = intent.getStringExtra("resource-id");

        if (id == null) {
            return null;
        } else {
            return UUID.fromString(id);
        }
    }

    private FormMode getFormMode() {

        int value = getIntent().getIntExtra(
                FormMode.BUNDLE_KEY,
                FormMode.Add.asInteger()
        );

        return FormMode.fromInteger(value);
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


    private void onSaveButtonClick() {

        viewModel.savePhotoshoot(
                ImmutablePhotoshootInput.builder()
                        .address(addressEditText.getText().toString())
                        .startTime(startTimeField.getFormTime())
                        .endTime(endTimeField.getFormTime())
                        .date(dateField.getDate())
                        .orderId(orderEditText.getText().toString())
                        .build()
        );
    }

    private void displayPhotoshoot(Photoshoot photoshoot) {
        orderEditText.setText(String.valueOf(photoshoot.orderId()));
        addressEditText.setText(photoshoot.address());

        startTimeField.setTime(FormTime.fromDate(photoshoot.startTime()));

        endTimeField.setTime(
                FormTime.fromDateSpan(
                        photoshoot.startTime(),
                        photoshoot.durationMinutes()
                )
        );

        dateField.setDate(photoshoot.startTime());
    }

    @Override
    public void visitSuccess() {
        finish();
    }

    @Override
    public void visitError() {
        snack(this, R.string.error_operationFailed);
    }

    @Override
    public void visitEmptyAddress() {
        addressEditText.setError(getString(R.string.error_empty_address));
    }

    @Override
    public void visitInvalidTimeRange() {
        startTimeField.getEditText().setError(getString(R.string.error_invalidTimeRange));
        endTimeField.getEditText().setError(getString(R.string.error_invalidTimeRange));
    }

    @Override
    public void visitEmptyOrder() {
        orderEditText.setError(getString(R.string.error_emptyOrderId));
    }

    @Override
    public void visitEmptyStartTime() {
        startTimeField.getEditText().setError(getString(R.string.error_emptyStartTime));
    }

    @Override
    public void visitEmptyEndTime() {
        endTimeField.getEditText().setError(getString(R.string.error_emptyEndTime));
    }

    @Override
    public void visitInvalidOrder() {
        orderEditText.setError(getString(R.string.error_invalidOrder));
    }

    @Override
    public void visitEmptyDate() {
        dateField.getEditText().setError(getString(R.string.error_empty_date));
    }

    @Override
    public void visitAdd() {
        titleTextView.setText(R.string.label_add_photo_shoot);
    }

    @Override
    public void visitUpdate() {
        titleTextView.setText(R.string.label_update_photoshoot);
    }
}