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

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeFormActivity extends AppCompatActivity {

    ActivityEmployeeFormBinding binding;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_form);

        EmployeeViewModel employeeViewModel = EmployeeViewModelImpl.create(
                this,
                Dependencies.from(this)
        );

        binding.setActivity(this);

        binding.setLifecycleOwner(this);

        binding.setViewModel(employeeViewModel);
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
}
