package com.example.ool_mobile.ui.form.employee;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.AccessLevel;
import com.example.ool_mobile.model.Occupation;

import java.util.List;

public interface EmployeeFormActivity {

    void onGalleryClick();

    void onCameraClick();

    @NonNull
    List<String> formatAccessLevels(@NonNull List<AccessLevel> accessLevels);

    @NonNull
    List<String> formatOccupations(@NonNull List<Occupation> occupations);
}