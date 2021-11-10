package com.example.ool_mobile.ui.form.employee;

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

import java.util.List;
import java.util.stream.Collectors;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;

public class EmployeeFormActivity extends AppCompatActivity
        implements EmployeeViewModel.Event.Visitor {

    private ActivityEmployeeFormBinding binding;

    private EmployeeViewModel employeeViewModel;

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
    }

    @Override
    protected void onStart() {
        super.onStart();


        employeeViewModel.getEvents()
                .to(DisposedFromLifecycle.of(this))
                .subscribe(event -> {
                    event.accept(this);
                });
    }

    public void onGalleryClick() {
        throw new UnsupportedOperationException();
    }

    public void onCameraClick() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public List<String> formatAccessLevels(@NonNull List<AccessLevel> accessLevels) {

        if (accessLevels == null) {
            return null;
        }

        return accessLevels.stream()
                .map(accessLevel -> String.format(getString(R.string.format_id_name),
                        accessLevel.getId(), accessLevel.getName()))
                .collect(Collectors.toList());
    }

    @Nullable
    public List<String> formatOccupations(@NonNull List<Occupation> occupations) {

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
        snack(this, R.string.error_operation_failed);
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
    public void visitMissingSocialName() {
        throw new UnsupportedOperationException();
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
}
