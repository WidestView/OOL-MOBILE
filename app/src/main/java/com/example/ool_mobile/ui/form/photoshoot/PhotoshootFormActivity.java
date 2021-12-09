package com.example.ool_mobile.ui.form.photoshoot;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.ActivityFormPhotoshootBinding;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.form.photoshoot.viewmodel.PhotoshootViewModel;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import java.util.UUID;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

import static com.example.ool_mobile.ui.util.SnackMessage.swalError;

public class PhotoshootFormActivity extends AppCompatActivity implements
        PhotoshootViewModel.Event.Visitor,
        FormMode.Visitor {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PhotoshootViewModel viewModel;

    private ActivityFormPhotoshootBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_form_photoshoot);

        binding.setActivity(this);

        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(
                this,
                PhotoshootViewModel.create(
                        getFormMode(), Dependencies.from(this).getPhotoshootApi(),
                        getResourceId()
                )
        ).get(PhotoshootViewModel.class);

        viewModel.getFormMode().accept(this);

        binding.setViewModel(viewModel);

        binding.setLifecycleOwner(this);
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
                FormModeValue.of(FormMode.Add)
        );

        return FormModeValue.convert(value);
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

    @Override
    public void visitSuccess() {
        finish();
    }

    @Override
    public void visitError() {
        swalError(this);
    }

    @Override
    public void visitEmptyAddress() {
        binding.addPhotoshootActivityAddressEditText.setError(getString(R.string.error_empty_address));
    }

    @Override
    public void visitInvalidTimeRange() {
        binding.addPhotoShootStartTimeInputLayout.getEditText()
                .setError(getString(R.string.error_invalid_time_range));

        binding.addPhotoShootEndTimeInputLayout
                .getEditText().setError(getString(R.string.error_invalid_time_range));
    }

    @Override
    public void visitEmptyOrder() {
        binding.addPhotoshootActivityOrderEditText
                .setError(getString(R.string.error_empty_order_id));
    }

    @Override
    public void visitEmptyStartTime() {
        binding.addPhotoShootStartTimeInputLayout
                .getEditText().setError(getString(R.string.error_empty_start_time));
    }

    @Override
    public void visitEmptyEndTime() {
        binding.addPhotoShootEndTimeInputLayout
                .getEditText().setError(getString(R.string.error_empty_end_time));
    }

    @Override
    public void visitInvalidOrder() {
        binding.addPhotoshootActivityOrderEditText
                .setError(getString(R.string.error_invalid_order));
    }

    @Override
    public void visitEmptyDate() {
        binding.addPhotoshootActivityDateDialogView
                .getEditText().setError(getString(R.string.error_empty_date));
    }


    @Override
    public void visitAdd() {
        binding.addPhotoshootActivityTitleTextView
                .setText(R.string.label_add_photo_shoot);
    }

    @Override
    public void visitUpdate() {
        binding.addPhotoshootActivityTitleTextView
                .setText(R.string.label_update_photoshoot);
    }
}