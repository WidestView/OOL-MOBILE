package com.example.ool_mobile.ui.form.equipment_withdraw;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.service.api.EmployeeApi;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.service.api.EquipmentWithdrawApi;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public abstract class CommonWithdrawViewModel extends SubscriptionViewModel implements WithdrawViewModel {

    protected final Subject<Event> events = PublishSubject.create();

    private Single<WithdrawInput.ListFields> fields;

    private MutableLiveData<WithdrawInput.ListFields> fieldsLiveData;

    protected final EquipmentWithdrawApi withdrawApi;
    protected final EquipmentApi equipmentApi;
    protected final PhotoshootApi photoshootApi;
    protected final EmployeeApi employeeApi;

    protected final WithdrawValidation validation = new WithdrawValidation(events);

    protected CommonWithdrawViewModel(@NonNull ApiProvider provider) {
        this.withdrawApi = provider.getWithdrawApi();
        this.equipmentApi = provider.getEquipmentApi();
        this.photoshootApi = provider.getPhotoshootApi();
        this.employeeApi = provider.getEmployeeApi();
    }

    @NonNull
    @Override
    public Subject<Event> getEvents() {
        return events;
    }

    @NonNull
    @Override
    public LiveData<WithdrawInput.ListFields> getLists() {

        if (fieldsLiveData == null) {
            fieldsLiveData = new MutableLiveData<>();

            fetchListFields()
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe(fieldsLiveData::setValue);
        }

        return fieldsLiveData;
    }

    protected Single<WithdrawInput.ListFields> fetchListFields() {

        if (fields == null) {

            fields = equipmentApi.listEquipments().zipWith(
                    photoshootApi.listAll(),
                    Pair::create
            ).zipWith(employeeApi.listEmployees(),
                    (pair, item) -> (WithdrawInput.ListFields) ImmutableListFields
                            .builder()
                            .equipments(pair.first)
                            .photoshoots(pair.second)
                            .employees(item)
                            .build()
            ).cache();

        }

        return fields;
    }

}
