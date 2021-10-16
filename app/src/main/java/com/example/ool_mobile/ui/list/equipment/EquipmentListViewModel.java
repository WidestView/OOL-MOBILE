package com.example.ool_mobile.ui.list.equipment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.ViewModelFactory;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class EquipmentListViewModel extends SubscriptionViewModel {

    private MutableLiveData<List<Equipment>> equipments;

    @NonNull
    private EquipmentApi api;

    public EquipmentListViewModel(@NonNull EquipmentApi api) {
        this.api = api;
    }

    @NonNull
    public LiveData<List<Equipment>> getEquipments() {

        if (equipments == null) {
            equipments = new MutableLiveData<>();

            subscriptions.add(
                    api.listEquipments()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(equipments -> this.equipments.setValue(equipments))
            );
        }

        return equipments;
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
