package com.example.ool_mobile.ui.list.equipment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.databinding.RowEquipmentDetailsBinding;
import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;

import java.util.Objects;

public class EquipmentDetailsRowAdapter extends RecyclerView.Adapter<EquipmentDetailsRowAdapter.ViewHolder> {

    @NonNull
    private final AdapterParameters<EquipmentDetails> parameters;

    public EquipmentDetailsRowAdapter(@NonNull AdapterParameters<EquipmentDetails> parameters) {
        Objects.requireNonNull(parameters, "parameters is null");
        this.parameters = parameters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowEquipmentDetailsBinding binding = RowEquipmentDetailsBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        EquipmentDetails details = parameters.items().get(position);

        holder.binding.setEvents(parameters.asRowEvents(details));
        holder.binding.setDetails(details);
    }

    @Override
    public int getItemCount() {
        return parameters.items().size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final RowEquipmentDetailsBinding binding;

        public ViewHolder(@NonNull RowEquipmentDetailsBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
