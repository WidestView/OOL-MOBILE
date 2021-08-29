package com.example.ool_mobile.ui.list.equipment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ool_mobile.R;

public class EquipmentDetailsListFragment extends Fragment {

    public EquipmentDetailsListFragment() {
        super(R.layout.fragment_list_equipment_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getView().findViewById(R.id.listEquipmentDetails_fab).setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(getView());

            controller.navigate(R.id.action_navigation_equipmentDetails_to_addEquipmentDetails);
        });
    }
}
