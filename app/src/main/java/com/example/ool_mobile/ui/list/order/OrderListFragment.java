package com.example.ool_mobile.ui.list.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.databinding.FragmentListPackageBinding;
import com.example.ool_mobile.model.Order;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;
import com.example.ool_mobile.ui.util.SnackMessage;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;

public class OrderListFragment extends Fragment {

    private OrderListViewModel viewModel;

    private FragmentListPackageBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        binding = FragmentListPackageBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this,
                OrderListViewModel.create(Dependencies.from(this))
        ).get(OrderListViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {

            binding.setAdapter(new OrderRowAdapter(
                    new AdapterParameters.Builder<Order>()
                            .items(orders)
                            .build()
            ));

        });

        viewModel.getEvents().to(DisposedFromLifecycle.of(this))
                .subscribe(error -> {
                    SnackMessage.swalError(this);
                });
    }

}
