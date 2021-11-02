package com.example.ool_mobile.ui.form.equipment_withdraw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.view_model.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Observable;

public interface WithdrawViewModel {

    @Nullable
    Integer getInitialId();

    @NonNull
    FormMode getFormMode();

    @NonNull
    Observable<Event> getEvents();

    @NonNull
    LiveData<WithdrawInput> getInput();

    @NonNull
    LiveData<WithdrawInput.ListFields> getLists();

    @NonNull
    LiveData<Boolean> getCanBeFinished();

    void finishWithdraw();

    void saveInput();

    void handleReceivedQr(@NonNull String code);

    @NonNull
    LiveData<Boolean> isLoading();


    interface Event {

        void accept(@NonNull Visitor visitor);

        Event Success = Visitor::visitSuccess;

        Event Error = Visitor::visitError;

        Event MissingEmployee = Visitor::visitMissingEmployee;
        Event MissingPhotoshoot = Visitor::visitMissingPhotoshoot;
        Event MissingEquipment = Visitor::visitMissingEquipment;
        Event MissingDevolutionDate = Visitor::visitMissingDevolutionDate;
        Event MissingDevolutionTime = Visitor::visitMissingDevolutionTime;

        Event UnsupportedQr = Visitor::visitUnsupportedQr;
        Event UnknownQr = Visitor::visitUnknownQr;

        Event WithdrawFinished = Visitor::visitWithdrawFinished;

        interface Visitor {
            void visitError();

            void visitMissingEmployee();

            void visitMissingPhotoshoot();

            void visitMissingEquipment();

            void visitMissingDevolutionDate();

            void visitMissingDevolutionTime();

            void visitSuccess();

            void visitWithdrawFinished();

            void visitUnknownQr();

            void visitUnsupportedQr();
        }
    }

    @NonNull
    static WithdrawViewModel create(
            @NonNull ViewModelStoreOwner owner,
            @NonNull FormMode formMode,
            @NonNull ApiProvider api,
            @Nullable Integer initialId) {

        return new ViewModelProvider(
                owner,
                create(formMode, api, initialId)
        ).get(CommonWithdrawViewModel.class);
    }

    @NotNull
    static ViewModelProvider.Factory create(
            @NonNull FormMode formMode,
            @NonNull ApiProvider provider,
            @Nullable Integer initialId) {
        return ViewModelFactory.create(
                CommonWithdrawViewModel.class,
                () -> {
                    AtomicReference<CommonWithdrawViewModel> result = new AtomicReference<>();

                    formMode.accept(new FormMode.Visitor() {
                        @Override
                        public void visitAdd() {
                            result.set(new AddWithdrawViewModel(provider));
                        }

                        @Override
                        public void visitUpdate() {
                            Objects.requireNonNull(initialId, "initialId is null");

                            result.set(new UpdateWithdrawViewModel(provider, initialId));

                        }
                    });

                    if (result.get() == null) {
                        throw new UnsupportedOperationException("invalid formMode " + formMode);
                    }

                    return result.get();
                }
        );
    }
}
