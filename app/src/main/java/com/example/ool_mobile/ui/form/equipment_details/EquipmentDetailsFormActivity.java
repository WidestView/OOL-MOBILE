package com.example.ool_mobile.ui.form.equipment_details;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.ActivityAddEquipmentDetailsBinding;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.ImageInputHandler;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class EquipmentDetailsFormActivity extends AppCompatActivity {

    private ActivityAddEquipmentDetailsBinding binding;

    private ImageInputHandler cameraHandler;

    private EquipmentDetailsFormViewModel viewModel;

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_add_equipment_details);

        binding.setActivity(this);

        binding.setLifecycleOwner(this);

        setupViewModel();

        cameraHandler = new ImageInputHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        subscriptions.add(
                cameraHandler
                        .getBitmapResults()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bitmap -> viewModel.setImageBitmap(bitmap))
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


        viewModel = new ViewModelProvider(this, EquipmentDetailsFormViewModel.create(
                FormModeValue.convert(args.getFormMode()),
                Dependencies.from(this).getEquipmentApi(),
                args.getResourceId() == -1 ? null : args.getResourceId()
        )).get(EquipmentDetailsFormViewModel.class);

        binding.setViewModel(viewModel);

        viewModel.getImageBitmap().observe(this, bitmap -> {
            binding.equipmentDetailsFormEquipmentImageView.setImageBitmap(bitmap);
        });
    }
}