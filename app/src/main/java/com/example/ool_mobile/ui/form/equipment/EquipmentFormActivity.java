package com.example.ool_mobile.ui.form.equipment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.ActivityEquipmentFormBinding;
import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.ool_mobile.ui.util.SnackMessage.swalError;

public class EquipmentFormActivity extends AppCompatActivity implements
        EquipmentFormViewModel.Event.Visitor {

    private ActivityEquipmentFormBinding binding;

    private EquipmentFormViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_equipment_form);

        binding.setActivity(this);

        setupViewModel();
    }

    private void setupViewModel() {
        EquipmentFormActivityArgs args = EquipmentFormActivityArgs
                .fromBundle(getIntent().getExtras());

        viewModel = new ViewModelProvider(this,
                EquipmentFormViewModel.create(
                        FormModeValue.convert(args.getFormMode()),
                        Dependencies.from(this).getEquipmentApi(),
                        args.getResourceId() == -1 ? null : args.getResourceId()
                )
        ).get(EquipmentFormViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.getEvents()
                .to(DisposedFromLifecycle.of(this))
                .subscribe(event -> {
                    event.accept(this);
                });
    }

    @Nullable
    public List<String> displayDetails(@Nullable List<EquipmentDetails> details) {

        if (details == null) {
            return null;
        }

        return details.stream()
                .map(data -> String.format(
                        getString(R.string.format_name_id), data.getName(), data.getId())
                )
                .collect(Collectors.toList());
    }

    @Override
    public void visitEmptyDetailsId() {
        binding.equipmentFormDetailsIdEditText.setError(getString(R.string.error_fill_this_field));
    }

    @Override
    public void visitError() {
        swalError(this);
    }

    @Override
    public void visitSuccess() {
        finish();
    }
}