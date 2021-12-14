package com.example.ool_mobile.ui.list.package_model;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ool_mobile.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PackageTypeListFragment extends Fragment {
    public PackageTypeListFragment() {
        super(R.layout.fragment_list_package_type);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton button = view.findViewById(R.id.listPackage_floatingActionButton);

        button.setOnClickListener(v -> {

            NavController controller = Navigation.findNavController(view);

            controller.navigate(R.id.action_navigation_package_types_to_addPackageTypeActivity);
        });
    }
}
