package com.example.ool_mobile.ui.qr_spawn;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ool_mobile.ui.form.equipment_withdraw.EquipmentWithdrawFormActivity;
import com.example.ool_mobile.ui.form.equipment_withdraw.EquipmentWithdrawFormActivityArgs;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

public class QrSpawnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, EquipmentWithdrawFormActivity.class);

        EquipmentWithdrawFormActivityArgs args =
                new EquipmentWithdrawFormActivityArgs.Builder(FormModeValue.of(FormMode.Add))
                        .setStartWithQr(true)
                        .build();

        intent.putExtras(args.toBundle());

        startActivity(intent);

    }
}