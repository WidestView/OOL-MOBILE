package com.example.ool_mobile.ui.form.equipment_withdraw;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
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
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;
import static com.example.ool_mobile.ui.util.SnackMessage.swalError;

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

        if (args.getStartWithQr()) {
            startQr();
        }
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

    private final ActivityResultLauncher<ScanOptions> launcher = registerForActivityResult(
            new ScanContract(), result -> {
                if (result != null && result.getContents() != null) {
                    viewModel.handleReceivedQr(result.getContents());
                }
            }
    );

    public void startQr() {

        ScanOptions options = new ScanOptions();
        options.setPrompt(getString(R.string.label_scan_qr));
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setOrientationLocked(true);

        launcher.launch(options);
    }

    @Nullable
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

    @Nullable
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

    @Nullable
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
        swalError(this);
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

    @Override
    public void visitSuccess() {
        finish();
    }

    @Override
    public void visitWithdrawFinished() {
        snack(this, R.string.message_withdraw_finished);

    }

    @Override
    public void visitUnknownQr() {
        snack(this, R.string.error_unknown_qr);
    }

    @Override
    public void visitUnsupportedQr() {
        snack(this, R.string.error_unsupported_qr);
    }
}