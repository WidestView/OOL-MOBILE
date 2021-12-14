package com.example.ool_mobile.ui.list.package_model;

import androidx.annotation.NonNull;

import com.example.ool_mobile.databinding.RowPackageBinding;
import com.example.ool_mobile.model.PackageModel;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;
import com.example.ool_mobile.ui.util.adapter.RowAdapter;

public class PackageRowAdapter extends RowAdapter<PackageModel, RowPackageBinding> {

    public PackageRowAdapter(@NonNull AdapterParameters<PackageModel> parameters) {
        super(parameters);
    }

    @NonNull
    @Override
    public Inflater<RowPackageBinding> getInflater() {
        return RowPackageBinding::inflate;
    }

    @Override
    protected void bind(@NonNull RowPackageBinding binding, @NonNull PackageModel packageModel) {
        binding.setPackageModel(packageModel);
        binding.setUtil(new RowPackageUtil());
        binding.setEvents(parameters.asRowEvents(packageModel));
    }
}
