package com.example.ool_mobile.ui.list.equipment_withdraw;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.FragmentListEquipmentWithdrawBinding;
import com.example.ool_mobile.model.EquipmentWithdraw;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;

public class EquipmentWithdrawListFragment extends Fragment {

    public EquipmentWithdrawListFragment() {
        super(R.layout.fragment_list_equipment_withdraw);
    }

    private FragmentListEquipmentWithdrawBinding binding;

    private WithdrawListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        binding = FragmentListEquipmentWithdrawBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setFragment(this);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        setupViewModel();
    }

    private void setupViewModel() {

        viewModel = new ViewModelProvider(this,
                WithdrawListViewModel.create(
                        Dependencies.from(this).getWithdrawApi()
                ))
                .get(WithdrawListViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.getEvents()
                .to(DisposedFromLifecycle.of(this))
                .subscribe(event -> snack(this, R.string.error_operation_failed));

        viewModel.fetchWithdraws().observe(getViewLifecycleOwner(), withdraws -> {

            binding.setAdapter(new EquipmentWithdrawRowAdapter(
                    new AdapterParameters.Builder<EquipmentWithdraw>()
                            .items(withdraws)
                            .build()
            ));
        });
    }

    public void onAddButtonClick() {

        NavDirections action = EquipmentWithdrawListFragmentDirections
                .actionWithdrawListToForm(FormModeValue.of(FormMode.Add));

        NavHostFragment
                .findNavController(this)
                .navigate(action);
    }


}
