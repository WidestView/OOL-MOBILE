package com.example.ool_mobile.ui.form.equipment_details;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.example.ool_mobile.model.EquipmentDetails;

public class EquipmentDetailsInput {

    @NonNull
    public final ObservableField<String> name = new ObservableField<>();

    @NonNull
    public final ObservableField<String> kindName = new ObservableField<>();

    @NonNull
    public final ObservableField<Integer> kindPosition = new ObservableField<>();

    @NonNull
    public final ObservableField<String> price = new ObservableField<>();

    public EquipmentDetailsInput() {

    }

    public EquipmentDetailsInput(@NonNull EquipmentDetails details) {
        name.set(details.getName());
        kindName.set(details.requireKind().getName());
        price.set(String.valueOf(details.getPrice()));
    }

    @Override
    public String toString() {
        return "EquipmentDetailsInput{" +
                "name='" + name.get() + '\'' +
                ", kindName='" + kindName.get() + '\'' +
                ", kindPosition=" + kindPosition.get() +
                ", price='" + price.get() + '\'' +
                '}';
    }
}
