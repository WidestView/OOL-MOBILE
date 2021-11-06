package com.example.ool_mobile.ui.list.employee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.FragmentListEmployeeBinding;
import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;

public class EmployeeListFragment extends Fragment {


    private FragmentListEmployeeBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentListEmployeeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setFragment(this);

        EmployeeListViewModel viewModel = new ViewModelProvider(
                this,
                EmployeeListViewModel.create(
                        Dependencies.from(this).getApiProvider().getEmployeeApi()
                )).get(EmployeeListViewModel.class);

        viewModel.getEmployees().observe(getViewLifecycleOwner(), employees -> {

            binding.setAdapter(
                    new EmployeeRowAdapter(
                            new AdapterParameters.Builder<Employee>()
                                    .items(employees)
                                    .build()
                    )
            );
        });

    }

    public void onAddButtonClick() {
        NavController controller = NavHostFragment.findNavController(this);

        controller.navigate(R.id.action_navigation_employees_to_addEmployeeActivity);
    }
}
