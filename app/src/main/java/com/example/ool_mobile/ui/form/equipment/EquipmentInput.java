package com.example.ool_mobile.ui.form.equipment;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;

import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.model.EquipmentDetails;

import java.util.List;

public class EquipmentInput {

    public final ObservableInt detailsSelection = new ObservableInt(-1);
    public final ObservableBoolean isAvailable = new ObservableBoolean();

    public EquipmentInput() {

    }

    public EquipmentInput(@NonNull Equipment equipment, List<EquipmentDetails> details) {

        for (int i = 0; i < details.size(); ++i) {
            if (details.get(i).getId() == equipment.getDetailsId()) {
                detailsSelection.set(i);
            }
        }

        isAvailable.set(equipment.isAvailable());
    }
}
