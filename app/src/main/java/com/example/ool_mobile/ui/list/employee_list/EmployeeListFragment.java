package com.example.ool_mobile.ui.list.employee_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ool_mobile.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmployeeListFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_list_employee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton button = getView().findViewById(R.id.listEmployee_addButton);

        button.setOnClickListener(v -> {

            NavController controller = Navigation.findNavController(getView());

            controller.navigate(R.id.action_navigation_employees_to_addEmployeeActivity);
        });
    }
}
