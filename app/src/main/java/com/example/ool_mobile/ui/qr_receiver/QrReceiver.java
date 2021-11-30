package com.example.ool_mobile.ui.qr_receiver;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ool_mobile.R;
import com.example.ool_mobile.service.QrMessageHandler;
import com.example.ool_mobile.ui.form.equipment.EquipmentFormActivity;
import com.example.ool_mobile.ui.form.equipment.EquipmentFormActivityArgs;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import timber.log.Timber;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;

public class QrReceiver extends AppCompatActivity implements QrMessageHandler.Result.Visitor{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = getQrIntent();

        if (intent == null) { return; }

        String data = intent.getDataString();

        QrMessageHandler messageHandler = new QrMessageHandler();

        QrMessageHandler.Result result = messageHandler.parseQrString(data);

        result.accept(this);
    }

    private Intent getQrIntent() {

        Intent intent = getIntent();

        if (intent == null || intent.getDataString() == null) {

            Timber.i("QrReceiver received with no intent");

            snack(this, R.string.error_missing_qr_intent);
            finish();

            return null;
        }

        return intent;
    }

    @Override
    public void visitInvalidQr() {
        Timber.i("QrReceiver received with invalid qr intent");

        snack(this, R.string.error_invalid_qr);

        finish();
    }

    @Override
    public void visitUnsupportedQr() {
        visitInvalidQr();
    }

    @Override
    public void visitSuccess(int equipmentId) {

        EquipmentFormActivityArgs args = new EquipmentFormActivityArgs.Builder(
                FormModeValue.of(FormMode.Update)
        ).setResourceId(equipmentId)
                .build();

        Intent intent = new Intent(this, EquipmentFormActivity.class)
                .putExtras(args.toBundle());

        startActivity(intent);
    }
}