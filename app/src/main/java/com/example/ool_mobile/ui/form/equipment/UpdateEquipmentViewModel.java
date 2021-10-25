package com.example.ool_mobile.ui.form.equipment;


import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.form.FormMode;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class UpdateEquipmentViewModel extends EquipmentFormViewModel {

    private final Subject<Event> events = PublishSubject.create();

    private final EquipmentApi api;

    private final int initialId;

    private Single<List<EquipmentDetails>> detailsRequest;

    private MutableLiveData<EquipmentInput> input;

    private MutableLiveData<List<EquipmentDetails>> detailsList;


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
    public LiveData<List<EquipmentDetails>> getDetailsList() {

        if (detailsList == null) {
            detailsList = new MutableLiveData<>();

            fetchDetails()
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe(result -> {
                        detailsList.setValue(result);
                    }, this::handleError);
        }

        return detailsList;
    }

    @NonNull
    @Override
    public LiveData<EquipmentInput> getInput() {
        if (input == null) {
            input = new MutableLiveData<>();

            api.getEquipmentById(initialId)
                    .zipWith(
                            fetchDetails(),
                            Pair::create
                    )
                    .to(disposedWhenCleared())
                    .subscribe(result -> {

                        this.input.postValue(
                                new EquipmentInput(result.first, result.second)
                        );
                    }, this::handleError);
        }

        return input;
    }

    private void handleError(Throwable error) {
        error.printStackTrace();
        events.onNext(Event.Error);
    }

    @NonNull
    @Override
    public LiveData<FormMode> getFormMode() {
        return new MutableLiveData<>(FormMode.Update);
    }

    @Override
    public void saveEquipment() {
        throw new UnsupportedOperationException("Updates are still not available in the api");
    }

    private Single<List<EquipmentDetails>> fetchDetails() {

        if (detailsRequest == null) {
            detailsRequest = api.listDetails().cache();
        }

        return detailsRequest;
    }
}
