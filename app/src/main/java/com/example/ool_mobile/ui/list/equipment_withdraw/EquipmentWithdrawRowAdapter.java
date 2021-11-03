package com.example.ool_mobile.ui.list.equipment_withdraw;

import androidx.annotation.NonNull;

import com.example.ool_mobile.databinding.RowEquipmentWithdrawBinding;
import com.example.ool_mobile.model.EquipmentWithdraw;
import com.example.ool_mobile.ui.util.UiDate;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;
import com.example.ool_mobile.ui.util.adapter.RowAdapter;

public class EquipmentWithdrawRowAdapter extends RowAdapter<EquipmentWithdraw, RowEquipmentWithdrawBinding> {

    public EquipmentWithdrawRowAdapter(@NonNull AdapterParameters<EquipmentWithdraw> parameters) {
        super(parameters);
    }

    @Override
    @NonNull
    public Inflater<RowEquipmentWithdrawBinding> getInflater() {
        return RowEquipmentWithdrawBinding::inflate;
    }

    @Override
    protected void bind(RowEquipmentWithdrawBinding binding, EquipmentWithdraw withdraw) {

        binding.setUiDate(new UiDate(binding.getRoot().getContext()));

        binding.setWithdraw(withdraw);

        binding.setEvents(parameters.asRowEvents(withdraw));
    }
}
