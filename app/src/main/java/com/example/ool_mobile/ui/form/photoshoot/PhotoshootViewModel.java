package com.example.ool_mobile.ui.form.photoshoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.view_model.ViewModelFactory;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Observable;

public abstract class PhotoshootViewModel extends SubscriptionViewModel {

    @NonNull
    public abstract Observable<Event> getEvents();

    @NonNull
    public abstract LiveData<PhotoshootInput> getInput();

    @NonNull
    public abstract LiveData<FormMode> getFormMode();

    public abstract void savePhotoshoot();

    interface Event {

        Event Success = Visitor::visitSuccess;
        Event Error = Visitor::visitError;
        Event EmptyAddress = Visitor::visitEmptyAddress;
        Event InvalidTimeRange = Visitor::visitInvalidTimeRange;
        Event EmptyOrder = Visitor::visitEmptyOrder;
        Event EmptyStartTime = Visitor::visitEmptyStartTime;
        Event EmptyEndTime = Visitor::visitEmptyEndTime;
        Event InvalidOrder = Visitor::visitInvalidOrder;
        Event EmptyDate = Visitor::visitEmptyDate;

        void accept(@NonNull Visitor visitor);

        interface Visitor {
            void visitSuccess();

            void visitError();

            void visitEmptyAddress();

            void visitInvalidTimeRange();

            void visitEmptyOrder();

            void visitEmptyStartTime();

            void visitEmptyEndTime();

            void visitInvalidOrder();

            void visitEmptyDate();
        }
    }

    @NonNull
    static ViewModelProvider.Factory create(
            @NonNull FormMode formMode,
            @NonNull PhotoshootApi photoshootApi,
            @Nullable UUID resourceId
    ) {

        return ViewModelFactory.create(PhotoshootViewModel.class, () -> {

            AtomicReference<PhotoshootViewModel> result = new AtomicReference<>();

            formMode.accept(new FormMode.Visitor() {
                @Override
                public void visitAdd() {
                    result.set(new AddPhotoshootViewModel(photoshootApi));
                }

                @Override
                public void visitUpdate() {
                    result.set(new UpdatePhotoshootViewModel(photoshootApi, resourceId));
                }
            });

            if (result.get() == null) {
                throw new UnsupportedOperationException();
            }

            return result.get();
        });

    }

}
