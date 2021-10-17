package com.example.ool_mobile.ui.form.equipment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.ActivityEquipmentFormBinding;
import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;

public class EquipmentFormActivity extends AppCompatActivity implements
        EquipmentFormViewModel.Event.Visitor {

    private ActivityEquipmentFormBinding binding;

    private EquipmentFormViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_equipment_form);

        binding.setActivity(this);

        setupViewModel();
    }

    private void setupViewModel() {
        EquipmentFormActivityArgs args = EquipmentFormActivityArgs
                .fromBundle(getIntent().getExtras());

        viewModel = new ViewModelProvider(this,
                EquipmentFormViewModel.create(
                        FormModeValue.convert(args.getFormMode()),
                        Dependencies.from(this).getEquipmentApi(),
                        args.getResourceId() == -1 ? null : args.getResourceId()
                )
        ).get(EquipmentFormViewModel.class);

        viewModel.getInitialEquipment().observe(this, this::displayEquipment);

        viewModel.getFormMode().observe(this, this::displayFormMode);
    }

    private void displayFormMode(@NonNull FormMode formMode) {
        binding.setFormMode(formMode);
    }

    private void displayEquipment(@NonNull Equipment equipment) {
        binding.setEquipment(equipment);
    }

    private Disposable subscription;

    @Override
    protected void onStart() {
        super.onStart();

        subscription = viewModel.getEvents().subscribe(event -> {
            event.accept(this);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        subscription.dispose();
    }

    public void onLeave() {
        onBackPressed();
    }

    public void onSubmit() {

        viewModel.saveEquipment(
                ImmutableEquipmentInput.builder()
                        .detailsId(
                                Objects.requireNonNull(
                                        binding.equipmentFormDetailsIdEditText.getText()
                                ).toString()
                        )
                        .isAvailable(binding.equipmentFormIsAvailableCheckBox.isChecked())
                        .build()
        );

    }

    @Override
    public void visitEmptyDetailsId() {
        binding.equipmentFormDetailsIdEditText.setError(getString(R.string.error_fill_this_field));
    }

    @Override
    public void visitInvalidDetailsId() {
        binding.equipmentFormDetailsIdEditText.setError(getString(R.string.error_invalid_numeric_id));
    }

    @Override
    public void visitError() {
        snack(this, R.string.error_operationFailed);
    }

    @Override
    public void visitSuccess() {
        finish();
    }
}