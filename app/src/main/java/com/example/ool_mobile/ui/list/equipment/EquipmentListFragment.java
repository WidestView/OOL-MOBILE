package com.example.ool_mobile.ui.list.equipment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.ool_mobile.databinding.FragmentListEquipmentBinding;
import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

public class EquipmentListFragment extends Fragment {

    private FragmentListEquipmentBinding binding;

    private EquipmentListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        binding = FragmentListEquipmentBinding.inflate(inflater, container, false);

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
                EquipmentListViewModel.create(
                        Dependencies.from(this).getEquipmentApi()
                )).get(EquipmentListViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.getEquipments().observe(getViewLifecycleOwner(), equipments ->
                binding.setAdapter(new EquipmentRowAdapter(
                        new AdapterParameters.Builder<Equipment>()
                                .onEdit(this::onEdit)
                                .items(equipments)
                                .build()
                )));
    }

    private void onEdit(Equipment equipment) {

        NavDirections directions = EquipmentListFragmentDirections
                .actionEquipmentListToEquipmentForm(
                        FormModeValue.of(FormMode.Update)
                )
                .setResourceId(
                        equipment.getId()
                );

        Navigation.findNavController(requireView())
                .navigate(directions);
    }

    public void onAddButtonClick() {

        NavDirections directions = EquipmentListFragmentDirections
                .actionEquipmentListToEquipmentForm(
                        FormModeValue.of(FormMode.Add)
                );

        Navigation.findNavController(requireView())
                .navigate(directions);
    }
}
