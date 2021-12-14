package com.example.ool_mobile.ui.list.order;

import androidx.annotation.NonNull;

import com.example.ool_mobile.databinding.RowOrderBinding;
import com.example.ool_mobile.model.Order;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;
import com.example.ool_mobile.ui.util.adapter.RowAdapter;

public class OrderRowAdapter extends RowAdapter<Order, RowOrderBinding> {

    public OrderRowAdapter(@NonNull AdapterParameters<Order> parameters) {
        super(parameters);
    }

    @NonNull
    @Override
    public Inflater<RowOrderBinding> getInflater() {
        return RowOrderBinding::inflate;
    }

    @Override
    protected void bind(@NonNull RowOrderBinding binding, @NonNull Order order) {
        binding.setOrder(order);
        binding.setEvents(parameters.asRowEvents(order));
    }
}
