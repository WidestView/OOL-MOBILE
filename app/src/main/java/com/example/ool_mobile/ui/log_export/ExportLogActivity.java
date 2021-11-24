package com.example.ool_mobile.ui.log_export;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.ActivityExportLogBinding;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;

import org.jetbrains.annotations.Nullable;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;

public class ExportLogActivity extends AppCompatActivity implements
        LogExportViewModel.Event.Visitor {

    private LogExportViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityExportLogBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_export_log);


        binding.setActivity(this);

        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(
                this,
                LogExportViewModel.create(Dependencies.from(this))
        ).get(LogExportViewModel.class);

        binding.setViewModel(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.getEvents()
                .to(DisposedFromLifecycle.of(this))
                .subscribe(event -> event.accept(this));
    }

    @Override
    public void visitError() {
        snack(this, R.string.error_operation_failed);
    }

    @Override
    public void visitSuccess() {
        finish();
    }
}