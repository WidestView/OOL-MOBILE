package com.example.ool_mobile.ui.form.equipment_withdraw;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.EquipmentWithdraw;
import com.example.ool_mobile.model.ImmutableEquipmentWithdraw;
import com.example.ool_mobile.ui.form.equipment_withdraw.WithdrawViewModel.Event;
import com.example.ool_mobile.ui.util.form.FormCheck;
import com.example.ool_mobile.ui.util.form.ValidationResult;

import java.util.Date;
import java.util.Objects;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

public class WithdrawValidation {

    private final Observer<Event> events;

    public WithdrawValidation(@NonNull Observer<Event> events) {

        Objects.requireNonNull(events);

        this.events = events;
    }

    @NonNull
    public Maybe<EquipmentWithdraw> validate(
            @NonNull WithdrawInput input,
            @NonNull WithdrawInput.ListFields fields) {

        Objects.requireNonNull(input, "input is null");
        Objects.requireNonNull(fields, "fields is null");

        return FormCheck.validate(getChecks(input), events::onNext)
                .flatMapMaybe(result -> {
                    if (result == ValidationResult.Failure) {
                        return Maybe.empty();
                    } else {

                        Objects.requireNonNull(input.expectedDevolutionTime.get());
                        Objects.requireNonNull(input.expectedDevolutionDate.get());

                        return Maybe.just((EquipmentWithdraw) ImmutableEquipmentWithdraw
                                .builder()
                                .withdrawDate(new Date())
                                .expectedDevolutionDate(
                                        input.expectedDevolutionTime
                                                .get()
                                                .addToDate(input.expectedDevolutionDate.get())
                                )
                                .effectiveDevolutionDate(null)
                                .photoshootId(fields.getPhotoshoots().get(
                                        input.photoshootSelection.get()
                                ).resourceId())
                                .equipmentId(fields.getEquipments().get(
                                        input.equipmentSelection.get()
                                ).getId())
                                .employeeId(fields.getEmployees().get(
                                        input.employeeSelection.get()
                                ).cpf())
                                .build());
                    }
                });

    }

    private Observable<FormCheck<Event>> getChecks(WithdrawInput input) {

        return Observable.fromArray(
                FormCheck.failIf(
                        () -> input.employeeSelection.get() == -1,
                        Event.MissingEmployee
                ),
                FormCheck.failIf(
                        () -> input.photoshootSelection.get() == -1,
                        Event.MissingPhotoshoot
                ),
                FormCheck.failIf(
                        () -> input.equipmentSelection.get() == -1,
                        Event.MissingEquipment
                ),
                FormCheck.failIf(
                        () -> input.expectedDevolutionDate.get() == null,
                        Event.MissingDevolutionDate
                ),
                FormCheck.failIf(
                        () -> input.expectedDevolutionTime.get() == null,
                        Event.MissingDevolutionTime
                )
        );
    }
}
