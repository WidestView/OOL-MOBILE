package com.example.ool_mobile.ui.form.equipment_details;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.model.EquipmentKind;
import com.example.ool_mobile.model.ImmutableEquipmentDetails;
import com.example.ool_mobile.ui.form.equipment_details.DetailsViewModel.Event;
import com.example.ool_mobile.ui.util.form.FormCheck;
import com.example.ool_mobile.ui.util.form.ValidationResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observer;

class EquipmentDetailsValidation {

    @NonNull
    private final Observer<Event> eventObserver;

    public EquipmentDetailsValidation(@NonNull Observer<Event> eventConsumer) {
        Objects.requireNonNull(eventConsumer, "eventConsumer is null");
        this.eventObserver = eventConsumer;
    }

    @NonNull
    @CheckResult
    public Maybe<EquipmentDetails> validate(
            @Nullable EquipmentDetailsInput input,
            @Nullable List<EquipmentKind> kinds
    ) {
        return Maybe.defer(() -> {

            if (input == null || kinds == null) {
                return Maybe.empty();
            }

            List<FormCheck<Event>> checks = getChecks(input);

            return FormCheck.validate(checks, eventObserver::onNext).flatMapMaybe(validationResult -> {
                if (validationResult == ValidationResult.Failure) {
                    return Maybe.empty();
                } else {

                    int kindId = kinds.get(input.kindPosition.get()).getId();


                    return Maybe.just(
                            ImmutableEquipmentDetails.builder()
                                    .kindId(kindId)
                                    .name(input.name.get())
                                    .price(Double.parseDouble(
                                            Objects.requireNonNull(input.price.get()))
                                    )
                                    .build()
                    );
                }
            });

        });
    }

    private Double priceValue;

    @NonNull
    private List<FormCheck<Event>> getChecks(EquipmentDetailsInput input) {

        String price = coalesce(input.price.get(), "").trim();
        String name = coalesce(input.name.get(), "").trim();
        String kindName = coalesce(input.kindName.get(), "").trim();
        int kindPosition = coalesce(input.kindPosition.get(), -1);

        return Arrays.asList(
                FormCheck.failIf(price::isEmpty, Event.MissingPrice),

                FormCheck.succeedIf(() -> {

                    try {
                        priceValue = Double.parseDouble(price);
                        return true;
                    } catch (NumberFormatException ex) {
                        return false;
                    }
                }, Event.InvalidPrice),

                FormCheck.succeedIf(() -> {
                    return coalesce(priceValue, 1.0) > 0;
                }, Event.NotPositivePrice),

                FormCheck.failIf(name::isEmpty, Event.MissingName),

                FormCheck.failIf(() -> kindPosition == -1, Event.InvalidEquipmentKind)
        );
    }

    @NonNull
    private <T> T coalesce(@Nullable T first, @NonNull T second) {
        if (first == null) {
            return second;
        } else {
            return first;
        }
    }
}
