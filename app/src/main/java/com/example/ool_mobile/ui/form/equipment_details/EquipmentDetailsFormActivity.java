package com.example.ool_mobile.ui.form.equipment_details;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;
import com.example.ool_mobile.ui.util.form.FormModeValue;
import com.example.ool_mobile.ui.util.image.ImageSelectionHandler;
import com.example.ool_mobile.ui.util.image.LegacySelectionHandler;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;
import static com.example.ool_mobile.ui.util.SnackMessage.swalError;

public class EquipmentDetailsFormActivity extends AppCompatActivity
        implements DetailsViewModel.Event.Visitor {

    private EquipmentDetailsFormBinding binding;

    private ImageSelectionHandler imageHandler;

    private DetailsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_equipment_details_form);

        binding.setActivity(this);

        binding.setLifecycleOwner(this);

        setupViewModel();

        imageHandler = new LegacySelectionHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        imageHandler
                .getBitmapResults()
                .observeOn(AndroidSchedulers.mainThread())
                .to(DisposedFromLifecycle.of(this))
                .subscribe(viewModel::setSelectedBitmap);

        viewModel
                .getEvents()
                .to(DisposedFromLifecycle.of(this))
                .subscribe(event -> {
                    event.accept(this);
                });
    }

    public void onCameraButtonClick() {

        imageHandler
                .requestCamera()
                .to(DisposedFromLifecycle.of(this))
                .subscribe();
    }

    public void onGalleryButtonClick() {

        imageHandler
                .requestGallery()
                .to(DisposedFromLifecycle.of(this))
                .subscribe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imageHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupViewModel() {
        EquipmentDetailsFormActivityArgs args = EquipmentDetailsFormActivityArgs
                .fromBundle(getIntent().getExtras());


        viewModel = new ViewModelProvider(this, DetailsViewModel.create(
                FormModeValue.convert(args.getFormMode()),
                Dependencies.from(this).getEquipmentApi(),
                args.getResourceId() == -1 ? null : args.getResourceId()
        )).get(CommonDetailsViewModel.class);

        binding.setViewModel(viewModel);
    }

    @Override
    public void visitError() {
        swalError(this);
    }

    @Override
    public void visitMissingName() {
        snack(this, R.string.error_empty_name);
    }

    @Override
    public void visitInvalidEquipmentKind() {
        snack(this, R.string.error_invalid_equipment_kind);
    }

    @Override
    public void visitMissingEquipmentKind() {
        snack(this, R.string.error_missing_equipment_kind);
    }

    @Override
    public void visitMissingPrice() {
        snack(this, R.string.error_missing_price);
    }

    @Override
    public void visitInvalidPrice() {
        snack(this, R.string.error_invalid_price);
    }

    @Override
    public void visitNotPositivePrice() {
        snack(this, R.string.error_not_negative_price);
    }

    @Override
    public void visitSuccess() {
        finish();
    }
}