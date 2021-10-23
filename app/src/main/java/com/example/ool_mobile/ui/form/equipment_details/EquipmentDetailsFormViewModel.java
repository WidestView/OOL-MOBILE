package com.example.ool_mobile.ui.form.equipment_details;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.EquipmentKind;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.ViewModelFactory;
import com.example.ool_mobile.ui.util.form.FormMode;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import static io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread;

public class EquipmentDetailsFormViewModel extends SubscriptionViewModel {

    @NonNull
    private final EquipmentApi api;

    @NonNull
    private final FormMode formMode;

    private final EquipmentDetailsValidation validation;

    private final Subject<Event> events = PublishSubject.create();

    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();

    private Single<List<EquipmentKind>> kinds;

    public EquipmentDetailsFormViewModel(
            @NonNull EquipmentApi api,
            @NonNull FormMode formMode) {
        this.api = api;
        this.formMode = formMode;

        this.validation = new EquipmentDetailsValidation(events);
    }

    @NonNull
    public LiveData<FormMode> getFormMode() {
        return new MutableLiveData<>(formMode);
    }


    @NonNull
    public Observable<Event> getEvents() {
        return events;
    }


    @NonNull
    private Single<List<EquipmentKind>> fetchKinds() {

        return Single.defer(() -> {

            if (kinds == null) {
                kinds = api.listKinds().cache();
            }

            return kinds;
        });
    }


    private MutableLiveData<List<String>> kindNames;

    @NonNull
    public LiveData<List<String>> getKindNames() {

        if (kindNames == null) {
            kindNames = new MutableLiveData<>();

            subscriptions.add(
                    fetchKinds()
                            .flatMapObservable(Observable::fromIterable)
                            .map(EquipmentKind::getName)
                            .toList()
                            .observeOn(mainThread())
                            .subscribe(
                                    kindNames::setValue,
                                    this::handleError
                            )
            );
        }

        return kindNames;
    }

    @Nullable
    private MutableLiveData<EquipmentDetailsInput> input;

    @NonNull
    public LiveData<EquipmentDetailsInput> getInput() {

        if (input == null) {
            input = new MutableLiveData<>();

            subscriptions.add(
                    api.getDetailsById(1)
                            .observeOn(AndroidSchedulers.mainThread())
                            .zipWith(
                                    fetchKinds(),
                                    EquipmentDetailsInput::new
                            )
                            .subscribe(this.input::setValue)
            );
        }

        return input;
    }


    @NonNull
    public LiveData<Bitmap> getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(@NonNull Bitmap bitmap) {
        Objects.requireNonNull(bitmap, "bitmap is null");

        imageBitmap.setValue(bitmap);
    }


    public void onSave() {

        System.out.println(input.getValue());

        subscriptions.add(
                fetchKinds().flatMapMaybe(
                        kinds -> validation.validate(input.getValue(), kinds)
                                .flatMapSingle(api::addDetails)
                ).subscribe(result -> {
                    events.onNext(Event.Success);
                })
        );
    }

    private void handleError(Throwable error) {

        error.printStackTrace();

        events.onNext(Event.Error);
    }

    @NonNull
    public static ViewModelProvider.Factory create(
            @NonNull FormMode formMode,
            @NonNull EquipmentApi api,
            @Nullable Integer initialId
    ) {
        Objects.requireNonNull(api, "api is null");

        return ViewModelFactory.create(
                EquipmentDetailsFormViewModel.class,
                () -> new EquipmentDetailsFormViewModel(api, formMode)
        );
    }

    public interface Event {

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
