package com.example.ool_mobile.ui.form.equipment;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.model.ImmutableEquipment;
import com.example.ool_mobile.ui.form.equipment.EquipmentFormViewModel.Event;
import com.example.ool_mobile.ui.util.form.FormCheck;
import com.example.ool_mobile.ui.util.form.ValidationResult;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observer;

import static com.example.ool_mobile.misc.ObjectUtil.coalesce;

public class EquipmentValidation {


    @NonNull
    private final Observer<Event> events;

    public EquipmentValidation(@NonNull Observer<Event> events) {
        Objects.requireNonNull(events);

        this.events = events;
    }

    @NonNull
    public Maybe<Equipment> validate(@NonNull EquipmentInput input, List<EquipmentDetails> details) {

        Objects.requireNonNull(input);


        return FormCheck.validate(getChecks(input), events::onNext)
                .flatMapMaybe(result -> {

                    if (result == ValidationResult.Success) {

                        int selection = input.detailsSelection.get();
                        boolean isAvailable = input.isAvailable.get();

                        return Maybe.just(ImmutableEquipment.builder()
                                .detailsId(details.get(selection).getId())
                                .isAvailable(isAvailable)
                                .build());
                    } else {
                        return Maybe.empty();
                    }
                });
    }

    private List<FormCheck<Event>> getChecks(EquipmentInput input) {

        int selection = coalesce(input.detailsSelection.get(), -1);

        return Collections.singletonList(
                FormCheck.failIf(
                        () -> selection < 0,
                        Event.EmptyDetailsId
                )
        );


    }


}
