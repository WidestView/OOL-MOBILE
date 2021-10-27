package com.example.ool_mobile.ui.list.equipment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.service.util.ErrorEvent;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.view_model.ViewModelFactory;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class EquipmentListViewModel extends SubscriptionViewModel {

    private final MutableLiveData<List<Equipment>> equipments = new MutableLiveData<>();

    @NonNull
    private final EquipmentApi api;

    private final Subject<ErrorEvent> events = PublishSubject.create();

    public EquipmentListViewModel(@NonNull EquipmentApi api) {
        this.api = api;
    }

    @NonNull
    public LiveData<List<Equipment>> getEquipments() {

        subscriptions.add(
                api.listEquipments()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this.equipments::setValue, this::handleError)
        );

        return equipments;
    }

    public void archiveEquipment(Equipment equipment) {

        api.archiveEquipment(equipment.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe(this::getEquipments, this::handleError);
    }

    public Observable<ErrorEvent> getEvents() {
        return events;
    }

    private void handleError(Throwable error) {
        error.printStackTrace();
        events.onNext(ErrorEvent.Error);
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull EquipmentApi api) {

        Objects.requireNonNull(api, "api is null");

        return ViewModelFactory.create(
                EquipmentListViewModel.class,
                () -> new EquipmentListViewModel(api)
        );
    }
}
