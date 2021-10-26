package com.example.ool_mobile.ui.form.equipment_details;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.view_model.ViewModelFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Observable;

interface DetailsViewModel {

    @NonNull
    Observable<Event> getEvents();

    @NonNull
    FormMode getFormMode();

    void onSave();

    @NonNull
    LiveData<EquipmentDetailsInput> getInput();

    @NonNull
    LiveData<Bitmap> getImageBitmap();

    void setImageBitmap(@NonNull Bitmap bitmap);

    @NonNull
    LiveData<List<String>> getKindNames();

    @NonNull
    static ViewModelProvider.Factory create(
            @NonNull FormMode formMode,
            @NonNull EquipmentApi api,
            @Nullable Integer initialId
    ) {
        Objects.requireNonNull(api, "api is null");

        return ViewModelFactory.create(
                CommonDetailsViewModel.class,
                () -> {

                    AtomicReference<CommonDetailsViewModel> result = new AtomicReference<>();

                    formMode.accept(new FormMode.Visitor() {
                        @Override
                        public void visitAdd() {
                            result.set(new AddDetailsViewModel(api));
                        }

                        @Override
                        public void visitUpdate() {

                            Objects.requireNonNull(initialId, "initialId is null");

                            result.set(new UpdateDetailsViewModel(api, initialId));
                        }
                    });

                    if (result.get() == null) {
                        throw new UnsupportedOperationException("Invalid form mode");
                    }

                    return result.get();
                }
        );
    }

    interface Event {

        Event Error = Visitor::visitError;

        Event MissingName = Visitor::visitMissingName;
        Event InvalidEquipmentKind = Visitor::visitInvalidEquipmentKind;
        Event MissingEquipmentKind = Visitor::visitMissingEquipmentKind;
        Event MissingPrice = Visitor::visitMissingPrice;
        Event InvalidPrice = Visitor::visitInvalidPrice;
        Event Success = Visitor::visitSuccess;

        void accept(@NonNull Visitor visitor);

        interface Visitor {
            void visitError();

            void visitMissingName();

            void visitInvalidEquipmentKind();

            void visitMissingEquipmentKind();

            void visitMissingPrice();

            void visitInvalidPrice();

            void visitSuccess();
        }
    }

}
