package com.example.ool_mobile.ui.form.equipment_details;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.model.EquipmentKind;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

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

    public EquipmentDetailsInput(
            @NonNull EquipmentDetails details,
            @NonNull List<EquipmentKind> kinds
    ) {
        this(details);

        Observable.fromIterable(kinds)
                .zipWith(Observable.range(0, kinds.size()), Pair::create)
                .filter(kind -> kind.first.getId() == details.getKindId())
                .map(kind -> kind.second)
                .firstElement()
                .subscribe(this.kindPosition::set)
                .dispose();

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
