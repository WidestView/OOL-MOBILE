package com.example.ool_mobile.ui.form.equipment;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.ViewModelFactory;
import com.example.ool_mobile.ui.util.form.FormMode;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Observable;

public abstract class EquipmentFormViewModel extends SubscriptionViewModel {

    @NonNull
    public abstract LiveData<Equipment> getInitialEquipment();

    @NonNull
    public abstract LiveData<FormMode> getFormMode();

    public abstract void saveEquipment(@NonNull EquipmentInput equipment);

    @NonNull
    public abstract Observable<Event> getEvents();

    @NonNull
    @CheckResult
    public static ViewModelProvider.Factory create(
            @NonNull FormMode formMode,
            @NonNull EquipmentApi api,
            @Nullable Integer initialId) {

        Objects.requireNonNull(formMode, "formMode is null");
        Objects.requireNonNull(api, "api is null");

        return ViewModelFactory.create(EquipmentFormViewModel.class, () -> {

            AtomicReference<EquipmentFormViewModel> result = new AtomicReference<>();

            formMode.accept(new FormMode.Visitor() {
                @Override
                public void visitAdd() {
                    result.set(new AddEquipmentViewModel(api));
                }

                @Override
                public void visitUpdate() {
                    Objects.requireNonNull(initialId, "initialId is null");

                    result.set(new UpdateEquipmentViewModel(api, initialId));
                }
            });

            if (result.get() == null) {
                throw new UnsupportedOperationException("Unsupported FormMode " + formMode);
            }

            return result.get();
        });
    }

    public interface Event {

        void accept(@NonNull Visitor visitor);

        Event EmptyDetailsId = Visitor::visitEmptyDetailsId;

        Event InvalidDetailsId = Visitor::visitInvalidDetailsId;

        Event NotFoundDetailsId = Visitor::visitNotFoundDetailsId;

        Event Error = Visitor::visitError;

        Event Success = Visitor::visitSuccess;

        interface Visitor {
            void visitEmptyDetailsId();

            void visitInvalidDetailsId();

            void visitError();

            void visitSuccess();

            void visitNotFoundDetailsId();
        }
    }
}