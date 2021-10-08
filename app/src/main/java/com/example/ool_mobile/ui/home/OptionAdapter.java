package com.example.ool_mobile.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.databinding.RowOptionItemBinding;

import java.util.List;
import java.util.Objects;

class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {

    private final List<OptionItem> items;

    public OptionAdapter(@NonNull List<OptionItem> items) {
        Objects.requireNonNull(items);
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowOptionItemBinding binding = RowOptionItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OptionItem item = items.get(position);

        holder.binding.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final RowOptionItemBinding binding;

        public ViewHolder(@NonNull RowOptionItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

}
