package com.example.ool_mobile.ui.form.equipment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.model.ImmutableEquipment;
import com.example.ool_mobile.ui.form.equipment.EquipmentFormViewModel.Event;
import com.example.ool_mobile.ui.util.form.FormCheck;
import com.example.ool_mobile.ui.util.form.ValidationResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observer;

public class EquipmentValidation {

    @NonNull
    private final Observer<Event> events;

    public EquipmentValidation(@NonNull Observer<Event> events) {
        Objects.requireNonNull(events);
        this.events = events;
    }

    @Nullable
    public Equipment validate(@NonNull EquipmentInput input) {
        Objects.requireNonNull(input);

        EquipmentInput normalized = ImmutableEquipmentInput.builder()
                .from(input)
                .detailsId(input.getDetailsId().trim())
                .build();

        if (FormCheck.validate(getChecks(normalized), events::onNext) == ValidationResult.Success) {

            return ImmutableEquipment.builder()
                    .detailsId(Integer.parseInt(normalized.getDetailsId()))
                    .isAvailable(normalized.isAvailable())
                    .build();
        } else {
            return null;
        }
    }

    private List<FormCheck<Event>> getChecks(EquipmentInput input) {

        return Arrays.asList(
                FormCheck.failIf(
                        () -> input.getDetailsId().isEmpty(),
                        Event.EmptyDetailsId
                ),
                FormCheck.succeedIf(
                        () -> {
                            try {
                                Integer.parseInt(input.getDetailsId());
                                return true;
                            } catch (NumberFormatException ex) {
                                return false;
                            }
                        },
                        Event.InvalidDetailsId
                )
        );


    }


}
