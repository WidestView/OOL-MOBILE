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

import java.util.List;

public class EmployeeFormActivity extends AppCompatActivity {

    ActivityEmployeeFormBinding binding;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_form);
    }

    public void onGalleryClick() {
        throw new UnsupportedOperationException();
    }

    public void onCameraClick() {
        throw new UnsupportedOperationException();
    }

    @NonNull
    public List<String> formatAccessLevels(@NonNull List<AccessLevel> accessLevels) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    public List<String> formatOccupations(@NonNull List<Occupation> occupations) {
        throw new UnsupportedOperationException();
    }
}
