package com.example.ool_mobile.ui.list.firing;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ool_mobile.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListFiringFragment extends Fragment {

    public ListFiringFragment() {
        super(R.layout.fragment_list_firing);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton button = view.findViewById(R.id.listFiring_fab);

        button.setOnClickListener(v -> {

            NavController controller = Navigation.findNavController(getView());

            controller.navigate(R.id.action_navigation_firings_to_addFiringActivity);
        });

    }

}
