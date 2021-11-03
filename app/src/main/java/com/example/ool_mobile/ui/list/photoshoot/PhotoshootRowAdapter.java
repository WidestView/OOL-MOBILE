package com.example.ool_mobile.ui.list.photoshoot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.databinding.RowPhotoshootBinding;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.ui.util.UiDate;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PhotoshootRowAdapter extends RecyclerView.Adapter<PhotoshootRowAdapter.ViewHolder> {

    @NonNull
    private final AdapterParameters<Photoshoot> parameters;

    public PhotoshootRowAdapter(@NonNull AdapterParameters<Photoshoot> parameters) {
        this.parameters = parameters;
    }

    @NonNull
    @Override
    public PhotoshootRowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowPhotoshootBinding binding = RowPhotoshootBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new PhotoshootRowAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoshootRowAdapter.ViewHolder holder, int position) {
        holder.bind(parameters.getItems().get(position));
    }

    @Override
    public int getItemCount() {
        return parameters.getItems().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final RowPhotoshootBinding binding;

        public ViewHolder(@NonNull RowPhotoshootBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bind(@NonNull Photoshoot photoshoot) {

            binding.setUiDate(new UiDate(requireContext()));

            binding.setPhotoshoot(photoshoot);

            binding.setEvents(parameters.asRowEvents(photoshoot));
        }

        @NotNull
        private Context requireContext() {
            return Objects.requireNonNull(
                    itemView.getContext(),
                    "itemView context must not be null"
            );
        }

    }
}
