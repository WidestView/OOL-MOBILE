package com.example.ool_mobile.ui.list.equipment_details;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.ViewModelFactory;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class EquipmentDetailsListViewModel extends SubscriptionViewModel {

    @NonNull
    private final MutableLiveData<List<EquipmentDetails>> details = new MutableLiveData<>();

    @NonNull
    private final EquipmentApi api;

    public EquipmentDetailsListViewModel(@NonNull EquipmentApi api) {
        this.api = api;
    }

    @NonNull
    public LiveData<List<EquipmentDetails>> getDetails() {

        subscriptions.add(
                api.listDetails()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this.details::setValue)
        );

        return details;
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull EquipmentApi api) {

        Objects.requireNonNull(api, "api is null");

        return ViewModelFactory.create(
                EquipmentDetailsListViewModel.class,
                () -> new EquipmentDetailsListViewModel(api)
        );
    }
}
