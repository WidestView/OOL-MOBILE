package com.example.ool_mobile.ui.form.equipment_details;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.form.FormModeValue;
import com.example.ool_mobile.ui.util.image.DefaultImageInputHandler;
import com.example.ool_mobile.ui.util.image.ImageInputHandler;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;

public class EquipmentDetailsFormActivity extends AppCompatActivity
        implements DetailsViewModel.Event.Visitor {

    private EquipmentDetailsFormBinding binding;

    private ImageInputHandler cameraHandler;

    private DetailsViewModel viewModel;

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_add_equipment_details);

        binding.setActivity(this);

        binding.setLifecycleOwner(this);

        setupViewModel();

        cameraHandler = new DefaultImageInputHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        subscriptions.addAll(
                cameraHandler
                        .getBitmapResults()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bitmap -> viewModel.setImageBitmap(bitmap)),

                viewModel.getEvents().subscribe(event -> {
                    event.accept(this);
                })
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscriptions.clear();
    }

    public void onCameraButtonClick() {

        subscriptions.add(
                cameraHandler.requestCamera().subscribe()
        );
    }

    public void onGalleryButtonClick() {

        subscriptions.add(
                cameraHandler.requestGallery().subscribe()
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        cameraHandler.onActivityResult(requestCode, resultCode, data);
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

        viewModel.getImageBitmap().observe(this, bitmap -> {
            binding.equipmentDetailsFormEquipmentImageView.setImageBitmap(bitmap);
        });
    }

    @Override
    public void visitError() {
        snack(this, R.string.error_operationFailed);
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
    public void visitSuccess() {
        finish();
    }
}