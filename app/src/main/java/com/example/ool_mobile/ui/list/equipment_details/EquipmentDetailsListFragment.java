package com.example.ool_mobile.ui.list.equipment_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.ool_mobile.databinding.FragmentListEquipmentDetailsBinding;
import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import static com.example.ool_mobile.ui.list.equipment_details.EquipmentDetailsListFragmentDirections.ActionEquipmentDetailsToForm;
import static com.example.ool_mobile.ui.list.equipment_details.EquipmentDetailsListFragmentDirections.actionEquipmentDetailsToForm;
import static com.example.ool_mobile.ui.util.SnackMessage.swalError;

public class EquipmentDetailsListFragment extends Fragment {

    private FragmentListEquipmentDetailsBinding binding;

    private EquipmentDetailsListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentListEquipmentDetailsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setFragment(this);

        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this,
                EquipmentDetailsListViewModel.create(
                        Dependencies.from(this).getEquipmentApi()
                )
        ).get(EquipmentDetailsListViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.getDetails().observe(getViewLifecycleOwner(), details -> {
            binding.setAdapter(new EquipmentDetailsRowAdapter(
                    new AdapterParameters.Builder<EquipmentDetails>()
                            .onEdit(this::onEditButtonClick)
                            .onDelete(this::onDeleteClick)
                            .items(details)
                            .build()
            ));
        });

        viewModel.getEvents()
                .to(DisposedFromLifecycle.of(this))
                .subscribe(error -> {
                    swalError(this);
                });
    }

    private void onDeleteClick(EquipmentDetails details) {
        viewModel.archiveDetails(details);
    }

    private void onEditButtonClick(EquipmentDetails details) {

        ActionEquipmentDetailsToForm directions = actionEquipmentDetailsToForm(
                FormModeValue.of(FormMode.Update)
        );

        directions.setResourceId(details.getId());

        Navigation
                .findNavController(requireView())
                .navigate(directions);
    }

    public void onAddButtonClick() {

        NavDirections directions = actionEquipmentDetailsToForm(
                FormModeValue.of(FormMode.Add)
        );

        NavController controller = Navigation.findNavController(requireView());

        controller.navigate(directions);
    }
}
