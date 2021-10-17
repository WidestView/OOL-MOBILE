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
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class UpdateEquipmentViewModel extends EquipmentFormViewModel {

    private final Subject<Event> events = PublishSubject.create();

    private MutableLiveData<Equipment> initialEquipment;

    private final EquipmentApi api;

    private final int initialId;

    public UpdateEquipmentViewModel(@NonNull EquipmentApi api, int initialId) {
        Objects.requireNonNull(api, "api is null");
        this.api = api;
        this.initialId = initialId;
    }

    @NonNull
    @Override
    public Observable<Event> getEvents() {
        return events;
    }

    @NonNull
    @Override
    public LiveData<Equipment> getInitialEquipment() {
        if (initialEquipment == null) {
            initialEquipment = new MutableLiveData<>();

            subscriptions.add(
                    api.getEquipmentById(initialId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> {
                                initialEquipment.setValue(result);
                            }, error -> {
                                error.printStackTrace();
                                events.onNext(Event.Error);
                            })
            );
        }

        return initialEquipment;
    }

    @NonNull
    @Override
    public LiveData<FormMode> getFormMode() {
        return new MutableLiveData<>(FormMode.Update);
    }

    @Override
    public void saveEquipment(@NonNull EquipmentInput equipment) {
        throw new UnsupportedOperationException("Updates are still not available in the api");
    }

}
