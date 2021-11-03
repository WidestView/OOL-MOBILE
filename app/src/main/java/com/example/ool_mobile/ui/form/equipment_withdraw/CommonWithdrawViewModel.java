package com.example.ool_mobile.ui.form.equipment_withdraw;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.service.QrMessageHandler;
import com.example.ool_mobile.service.api.EmployeeApi;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.service.api.EquipmentWithdrawApi;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;

import java.util.Objects;

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

    protected final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    protected CommonWithdrawViewModel(@NonNull ApiProvider provider) {
        this.withdrawApi = provider.getWithdrawApi();
        this.equipmentApi = provider.getEquipmentApi();
        this.photoshootApi = provider.getPhotoshootApi();
        this.employeeApi = provider.getEmployeeApi();
    }

    @Override
    public void handleReceivedQr(@NonNull String jsonString) {

        Objects.requireNonNull(jsonString, "jsonString is null");

        // we can't do anything if the input has not been loaded yet >~<

        if (getInput().getValue() == null) {
            return;
        }

        QrMessageHandler handler = new QrMessageHandler();

        handler.parseQrString(jsonString).accept(new QrMessageHandler.Result.Visitor() {
            @Override
            public void visitInvalidQr() {
                events.onNext(Event.UnknownQr);
            }

            @Override
            public void visitUnsupportedQr() {
                events.onNext(Event.UnsupportedQr);
            }

            @Override
            public void visitSuccess(int equipmentId) {

                fetchListFields()
                        .map(WithdrawInput.ListFields::getEquipments)
                        .observeOn(AndroidSchedulers.mainThread())
                        .to(disposedWhenCleared())
                        .subscribe(items -> {
                            Objects.requireNonNull(getInput().getValue())
                                    .selectEquipmentWithId(equipmentId, items);
                        });
            }
        });

    }


    @NonNull
    @Override
    public Subject<Event> getEvents() {
        return events;
    }

    @Override
    public LiveData<Boolean> isLoading() {
        return loading;
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
