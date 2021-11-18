package com.example.ool_mobile.ui.log_export;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.ActivityExportLogBinding;
import com.example.ool_mobile.service.Dependencies;

import org.jetbrains.annotations.Nullable;

public class ExportLogActivity extends AppCompatActivity {

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

    int DIRECTORY_REQUEST = 16;

    public void onPathEditTextClick() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        //noinspection deprecation
        startActivityForResult(intent, DIRECTORY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DIRECTORY_REQUEST && resultCode == RESULT_OK) {

            if (data != null && data.getData() != null) {
                viewModel.getChosenDirectoryPath().setValue(data.getData());
            }

        }
    }
}