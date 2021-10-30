package com.example.ool_mobile.ui.form.equipment_withdraw;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.ActivityEquipmentWithdrawFormBinding;
import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;
import com.example.ool_mobile.ui.util.UiDate;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;

public class EquipmentWithdrawFormActivity extends AppCompatActivity implements WithdrawViewModel.Event.Visitor {

    private ActivityEquipmentWithdrawFormBinding binding;

    private UiDate uiDate;

    private WithdrawViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_equipment_withdraw_form
        );

        binding.setActivity(this);
        binding.setLifecycleOwner(this);

        uiDate = new UiDate(this);

        setupViewModel();
    }

    private void setupViewModel() {

        EquipmentWithdrawFormActivityArgs args = EquipmentWithdrawFormActivityArgs.fromBundle(
                getIntent().getExtras()
        );

        viewModel = WithdrawViewModel.create(
                this,
                FormModeValue.convert(args.getFormMode()),
                Dependencies.from(this).getApiProvider(),
                args.getResourceId() == -1 ? null : args.getResourceId()
        );

        binding.setViewModel(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.getEvents()
                .to(DisposedFromLifecycle.of(this))
                .subscribe(event -> {
                    event.accept(this);
                });

    }

    public List<String> formatEquipments(@Nullable List<Equipment> equipments) {

        if (equipments == null) {
            return null;
        }

        return equipments.stream()
                .map(equipment ->
                        String.format(
                                getString(R.string.format_id_name),
                                equipment.getId(),
                                equipment.requireDetails().getName()
                        )

                ).collect(Collectors.toList());
    }

    public List<String> formatPhotoshoots(@Nullable List<Photoshoot> photoshoots) {

        if (photoshoots == null) {
            return null;
        }

        return photoshoots.stream()
                .map(photoshoot ->
                        String.format(
                                getString(R.string.format_string_id_name),
                                photoshoot.resourceId().toString().substring(0, 8),
                                uiDate.formatDate(photoshoot.startTime())
                        )
                ).collect(Collectors.toList());
    }

    public List<String> formatEmployees(@Nullable List<Employee> employees) {

        if (employees == null) {
            return null;
        }

        return employees.stream()
                .map(employee ->
                        String.format(
                                getString(R.string.format_string_id_name),
                                employee.cpf(),
                                employee.name()
                        )
                ).collect(Collectors.toList());

    }

    @Override
    public void visitError() {
        snack(this, R.string.error_operation_failed);
    }

    @Override
    public void visitMissingEmployee() {
        snack(this, R.string.error_missing_employee);
    }

    @Override
    public void visitMissingPhotoshoot() {
        snack(this, R.string.error_missing_photoshoot);
    }

    @Override
    public void visitMissingEquipment() {
        snack(this, R.string.error_missing_equipment);
    }

    @Override
    public void visitMissingDevolutionDate() {
        snack(this, R.string.missing_devolution_date);
    }

    @Override
    public void visitMissingDevolutionTime() {
        snack(this, R.string.missing_devolution_time);
    }
}