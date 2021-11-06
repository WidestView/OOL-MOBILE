package com.example.ool_mobile.ui.util.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public abstract class RowAdapter<Model, Binding extends ViewDataBinding> extends RecyclerView.Adapter<RowAdapter.ViewHolder<Binding>> {

    @NonNull
    protected AdapterParameters<Model> parameters;

    public RowAdapter(@NonNull AdapterParameters<Model> parameters) {
        Objects.requireNonNull(parameters);
        this.parameters = parameters;
    }

    public interface Inflater<Binding> {

        @NonNull
        Binding inflate(
                @NonNull LayoutInflater layoutInflater,
                @NonNull ViewGroup parent,
                boolean attachToRoot);
    }

    @NonNull
    public abstract Inflater<Binding> getInflater();

    protected abstract void bind(@NonNull Binding binding, @NonNull Model model);

    @NonNull
    @Override
    public ViewHolder<Binding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder<>(getInflater().inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
        ));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Binding> holder, int position) {

        Model model = parameters.getItems().get(position);

        bind(holder.getBinding(), model);
    }

    @Override
    public int getItemCount() {
        return parameters.getItems().size();
    }

    public static class ViewHolder<Binding extends ViewDataBinding> extends RecyclerView.ViewHolder {

        private final Binding binding;

        public ViewHolder(@NonNull Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @NonNull
        public Binding getBinding() {
            return binding;
        }
    }
}
