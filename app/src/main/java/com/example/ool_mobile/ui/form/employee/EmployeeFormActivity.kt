package com.example.ool_mobile.ui.form.employee;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.ActivityEmployeeFormBinding;
import com.example.ool_mobile.model.AccessLevel;
import com.example.ool_mobile.model.Occupation;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;
import com.example.ool_mobile.ui.util.image.ImageSelectionHandler;
import com.example.ool_mobile.ui.util.image.LegacySelectionHandler;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;
import static com.example.ool_mobile.ui.util.SnackMessage.swalError;

public class EmployeeFormActivity extends AppCompatActivity
        implements EmployeeViewModel.Event.Visitor {

    private ActivityEmployeeFormBinding binding;

    private EmployeeViewModel employeeViewModel;

    private ImageSelectionHandler imageSelectionHandler;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_form);

        employeeViewModel = EmployeeViewModelImpl.create(
                this,
                Dependencies.from(this)
        );

        binding.setActivity(this);

        binding.setLifecycleOwner(this);

        binding.setErrors(new EmployeeFormErrors());

        binding.setViewModel(employeeViewModel);

        imageSelectionHandler = new LegacySelectionHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        employeeViewModel.getEvents()
                .to(DisposedFromLifecycle.of(this))
                .subscribe(event -> {
                    event.accept(this);
                });

        imageSelectionHandler.getBitmapResults()
                .observeOn(AndroidSchedulers.mainThread())
                .to(DisposedFromLifecycle.of(this))
                .subscribe(bitmap -> {
                    employeeViewModel.getImageBitmap().setValue(bitmap);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageSelectionHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imageSelectionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void onGalleryClick() {
        imageSelectionHandler.requestGallery()
                .to(DisposedFromLifecycle.of(this))
                .subscribe();
    }

    public void onCameraClick() {
        imageSelectionHandler.requestCamera()
                .to(DisposedFromLifecycle.of(this))
                .subscribe();
    }

    @Nullable
    public List<String> formatAccessLevels(@Nullable List<AccessLevel> accessLevels) {

        if (accessLevels == null) {
            return null;
        }

        return accessLevels.stream()
                .map(accessLevel -> String.format(getString(R.string.format_id_name),
                        accessLevel.getId(), accessLevel.getName()))
                .collect(Collectors.toList());
    }

    @Nullable
    public List<String> formatOccupations(@Nullable List<Occupation> occupations) {

        if (occupations == null) {
            return null;
        }

        return occupations.stream()
                .map(occupation -> String.format(getString(R.string.format_id_name),
                        occupation.id(), occupation.name()))
                .collect(Collectors.toList());
    }

    @Override
    public void visitError() {
        swalError(this);
    }

    @Override
    public void visitSuccess() {
        finish();
    }

    @Override
    public void visitMissingName() {
        binding.getErrors()
                .getNameError()
                .set(getString(R.string.error_missing_employee_name));
    }

    @Override
    public void visitMissingBirthDate() {
        binding.getErrors()
                .getBirthDateError()
                .set(getString(R.string.error_missing_employee_birth_date));

        snack(this, R.string.error_missing_employee_birth_date);
    }

    @Override
    public void visitMissingPhone() {
        binding.getErrors()
                .getPhoneError()
                .set(getString(R.string.error_missing_employee_phone));

    }

    @Override
    public void visitMissingEmail() {

        binding.getErrors()
                .getEmailError()
                .set(getString(R.string.error_missing_employee));
    }

    @Override
    public void visitMissingPassword() {

        binding.getErrors()
                .getPasswordError()
                .set(getString(R.string.error_missing_employee_password));
    }

    @Override
    public void visitMissingPasswordConfirmation() {

        binding.getErrors()
                .getPasswordConfirmationError()
                .set(getString(R.string.error_missing_employee_password_confirmation));
    }

    @Override
    public void visitMissingAccessLevel() {

        binding.getErrors()
                .getAccessLevelError()
                .set(getString(R.string.error_missing_employee_access_level));
    }

    @Override
    public void visitMissingGender() {

        binding.getErrors()
                .getGenderError()
                .set(getString(R.string.error_missing_employee_gender));
    }

    @Override
    public void visitMissingOccupation() {

        binding.getErrors()
                .getOccupationError()
                .set(getString(R.string.error_missing_employee_occupation));
    }

    @Override
    public void visitPasswordsDoNotMatch() {
        binding.getErrors()
                .getPasswordConfirmationError()
                .set(getString(R.string.error_passwords_not_match));
    }

    @Override
    public void visitInvalidPhone() {

        binding.getErrors()
                .getPhoneError()
                .set(getString(R.string.error_invalid_employeee_phone));
    }

    @Override
    public void visitMissingRg() {

        binding.getErrors()
                .getRgError()
                .set(getString(R.string.invalid_employee_rg));
    }
}
