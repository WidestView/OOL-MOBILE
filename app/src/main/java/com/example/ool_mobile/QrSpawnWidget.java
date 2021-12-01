package com.example.ool_mobile;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.example.ool_mobile.ui.form.equipment_withdraw.EquipmentWithdrawFormActivity;
import com.example.ool_mobile.ui.form.equipment_withdraw.EquipmentWithdrawFormActivityArgs;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 */
public class QrSpawnWidget extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        Intent intent = new Intent(context, EquipmentWithdrawFormActivity.class);

        EquipmentWithdrawFormActivityArgs args =
                new EquipmentWithdrawFormActivityArgs.Builder(FormModeValue.of(FormMode.Add))
                        .setStartWithQr(true)
                        .build();

        intent.putExtras(args.toBundle());

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.qr_spawn_widget);

        views.setOnClickPendingIntent(R.id.qrSpawnWidget_imageView, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(
            @Nullable Context context,
            @Nullable AppWidgetManager appWidgetManager,
            @Nullable int[] appWidgetIds) {

        Objects.requireNonNull(context);
        Objects.requireNonNull(appWidgetManager);
        Objects.requireNonNull(appWidgetIds);

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(@Nullable Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(@Nullable Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}