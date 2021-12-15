package com.example.ool_mobile.ui.list.occupation_list;

import androidx.annotation.NonNull;

import com.example.ool_mobile.databinding.RowOccupationBinding;
import com.example.ool_mobile.model.Occupation;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;
import com.example.ool_mobile.ui.util.adapter.RowAdapter;

public class OccupationRowAdapter extends RowAdapter<Occupation, RowOccupationBinding> {

    public OccupationRowAdapter(@NonNull AdapterParameters<Occupation> parameters) {
        super(parameters);
    }

    @NonNull
    @Override
    public Inflater<RowOccupationBinding> getInflater() {
        return RowOccupationBinding::inflate;
    }

    @Override
    protected void bind(@NonNull RowOccupationBinding binding, @NonNull Occupation occupation) {

        binding.setOccupation(occupation);
        binding.setEvents(parameters.asRowEvents(occupation));
    }
}
