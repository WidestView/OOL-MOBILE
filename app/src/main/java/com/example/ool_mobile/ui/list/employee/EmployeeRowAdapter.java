package com.example.ool_mobile.ui.list.employee;

import androidx.annotation.NonNull;

import com.example.ool_mobile.databinding.RowEmployeeBinding;
import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.ui.util.UiDate;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;
import com.example.ool_mobile.ui.util.adapter.RowAdapter;

public class EmployeeRowAdapter extends RowAdapter<Employee, RowEmployeeBinding> {

    public EmployeeRowAdapter(@NonNull AdapterParameters<Employee> parameters) {
        super(parameters);
    }

    @NonNull
    @Override
    public Inflater<RowEmployeeBinding> getInflater() {
        return RowEmployeeBinding::inflate;
    }

    @Override
    protected void bind(@NonNull RowEmployeeBinding binding, @NonNull Employee employee) {

        binding.setUiDate(new UiDate(binding.getRoot().getContext()));

        binding.setEmployee(employee);

        binding.setEvents(parameters.asRowEvents(employee));
    }

}
