package com.example.ool_mobile.ui.list.package_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.databinding.FragmentListPackageBinding;
import com.example.ool_mobile.model.PackageModel;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;
import com.google.android.material.snackbar.Snackbar;


public class PackageListFragment extends Fragment {

    private FragmentListPackageBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentListPackageBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PackageListViewModel viewModel = new ViewModelProvider(this, PackageListViewModel.create(
                Dependencies.from(this)
        )).get(PackageListViewModel.class);

        viewModel.getPackages().observe(getViewLifecycleOwner(), packages -> {

            binding.setAdapter(new PackageRowAdapter(
                    new AdapterParameters.Builder<PackageModel>()
                            .items(packages)
                            .build()
            ));

        });
    }

    public void onAddButtonClick() {

        Snackbar.make(
                requireView().findViewById(android.R.id.content),
                "Not implemented yet", Snackbar.LENGTH_LONG
        ).show();
    }
}
