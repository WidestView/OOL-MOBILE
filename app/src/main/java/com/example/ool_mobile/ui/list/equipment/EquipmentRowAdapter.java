package com.example.ool_mobile.ui.list.equipment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.databinding.RowEquipmentBinding;
import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;

import java.util.Objects;

public class EquipmentRowAdapter extends RecyclerView.Adapter<EquipmentRowAdapter.ViewHolder> {

    private final AdapterParameters<Equipment> parameters;

    public EquipmentRowAdapter(@NonNull AdapterParameters<Equipment> parameters) {

        Objects.requireNonNull(parameters, "parameters is null");

        this.parameters = parameters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowEquipmentBinding binding = RowEquipmentBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Equipment equipment = parameters.items().get(position);

        holder.binding.setEquipment(equipment);

        holder.binding.setEvents(parameters.asRowEvents(equipment));
    }

    @Override
    public int getItemCount() {
        return parameters.items().size();
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final RowEquipmentBinding binding;

        public ViewHolder(@NonNull RowEquipmentBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

    }
}
