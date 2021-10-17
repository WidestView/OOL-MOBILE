package com.example.ool_mobile.ui.form.equipment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.form.FormMode;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class AddEquipmentViewModel extends EquipmentFormViewModel {

    private final Subject<Event> events = PublishSubject.create();

    private final EquipmentApi api;

    private final EquipmentValidation validation;


    public AddEquipmentViewModel(@NonNull EquipmentApi api) {
        Objects.requireNonNull(api, "api is null");

        this.api = api;

        this.validation = new EquipmentValidation(events);
    }

    @NonNull
    @Override
    public LiveData<Equipment> getInitialEquipment() {
        return new MutableLiveData<>();
    }

    @NonNull
    @Override
    public LiveData<FormMode> getFormMode() {
        return new MutableLiveData<>(FormMode.Add);
    }

    @Override
    public void saveEquipment(@NonNull EquipmentInput input) {

        Equipment equipment = validation.validate(input);

        if (equipment != null) {

            Disposable subscription = api.getDetailsById(equipment.getDetailsId())
                    .map(success -> true)
                    .onErrorReturn(error -> false)
                    .flatMap(detailsExists -> {
                        if (detailsExists) {
                            return api.addEquipment(equipment)
                                    .map(success -> true);
                        } else {
                            return Single.just(false);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(success -> {
                        if (success) {
                            events.onNext(Event.Success);
                        } else {
                            events.onNext(Event.NotFoundDetailsId);
                        }
                    }, error -> {
                        error.printStackTrace();
                        events.onNext(Event.Error);
                    });

            subscriptions.add(subscription);
        }
    }

    private void handleError(Throwable error) {

        error.printStackTrace();

        events.onNext(Event.Error);
    }

    @NonNull
    @Override
    public Observable<Event> getEvents() {
        return events;
    }
}
