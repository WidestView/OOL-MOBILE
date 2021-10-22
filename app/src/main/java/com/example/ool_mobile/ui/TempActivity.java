package com.example.ool_mobile.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.service.api.setup.ApiInfo;
import com.example.ool_mobile.ui.form.equipment_details.EquipmentDetailsFormActivity;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dependencies.from(this)
                .getEmployeeRepository()
                .login(ApiInfo.DEFAULT_USER_LOGIN, ApiInfo.DEFAULT_USER_PASSWORD)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        Intent intent = new Intent(this, EquipmentDetailsFormActivity.class);

        intent.putExtra("form-mode", FormModeValue.of(FormMode.Add));

        startActivity(intent);
    }
}