package com.example.ool_mobile.ui.form.equipment;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.model.ImmutableEquipment;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.form.equipment.EquipmentFormViewModel.Event;
import com.example.ool_mobile.ui.util.form.FormCheck;
import com.example.ool_mobile.ui.util.form.ValidationResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;

public class EquipmentValidation {

    @NonNull
    private final EquipmentApi api;

    @NonNull
    private final Observer<Event> events;

    public EquipmentValidation(
            @NonNull EquipmentApi api,
            @NonNull Observer<Event> events) {

        Objects.requireNonNull(api);
        Objects.requireNonNull(events);

        this.api = api;
        this.events = events;
    }

    @NonNull
    public Maybe<Equipment> validate(@NonNull EquipmentInput input) {
        Objects.requireNonNull(input);

        EquipmentInput normalized = ImmutableEquipmentInput.builder()
                .from(input)
                .detailsId(input.getDetailsId().trim())
                .build();

        return FormCheck.validate(getChecks(normalized), events::onNext)
                .flatMapMaybe(result -> {
                    if (result == ValidationResult.Success) {
                        return Maybe.just(ImmutableEquipment.builder()
                                .detailsId(Integer.parseInt(normalized.getDetailsId()))
                                .isAvailable(normalized.isAvailable())
                                .build());
                    } else {
                        return Maybe.empty();
                    }
                });
    }

    private List<FormCheck<Event>> getChecks(EquipmentInput input) {

        AtomicReference<Integer> id = new AtomicReference<>();

        return Arrays.asList(
                FormCheck.succeedIf(
                        () -> {
                            try {
                                id.set(Integer.parseInt(input.getDetailsId()));
                                return true;
                            } catch (NumberFormatException ex) {
                                return false;
                            }
                        },
                        Event.InvalidDetailsId
                ),
                FormCheck.failIf(
                        () -> input.getDetailsId().isEmpty(),
                        Event.EmptyDetailsId
                ),
                FormCheck.succeedIf(Single.defer(() -> {
                    if (id.get() == null) {
                        return Single.just(true);
                    } else {
                        return api.getDetailsById(id.get())
                                .map(success -> true)
                                .onErrorReturn(error -> false);
                    }
                }), Event.NotFoundDetailsId)
        );


    }


}
