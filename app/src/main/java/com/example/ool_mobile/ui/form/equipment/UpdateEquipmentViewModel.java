package com.example.ool_mobile.ui.form.equipment;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private final EquipmentApi api;

    private final int initialId;

    private Single<List<EquipmentDetails>> detailsRequest;

    private MutableLiveData<EquipmentInput> input;

    private MutableLiveData<List<EquipmentDetails>> detailsList;

    private final EquipmentValidation validation = new EquipmentValidation(events);


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

            loading.setValue(true);

            fetchDetails()
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe(result -> {
                        detailsList.setValue(result);
                        loading.setValue(false);
                    }, this::handleError);
        }

        return detailsList;
    }

    @Nullable
    @Override
    public Integer getInitialId() {
        return initialId;
    }

    @Override
    public LiveData<Boolean> isLoading() {
        return loading;
    }

    @NonNull
    @Override
    public LiveData<EquipmentInput> getInput() {
        if (input == null) {
            input = new MutableLiveData<>();

            loading.setValue(true);

            api.getEquipmentById(initialId)
                    .zipWith(
                            fetchDetails(),
                            Pair::create
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe(result -> {
                        this.input.setValue(new EquipmentInput(result.first, result.second));
                        loading.setValue(false);
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

        if (input.getValue() == null) {
            return;
        }

        loading.setValue(true);

        fetchDetails()
                .flatMapMaybe(details -> validation.validate(input.getValue(), details))
                .flatMapCompletable(details ->
                        api.updateEquipment(initialId, details)
                )
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe(() -> {
                    events.onNext(Event.Success);
                    loading.setValue(false);
                }, this::handleError);
    }

    private Single<List<EquipmentDetails>> fetchDetails() {

        if (detailsRequest == null) {
            detailsRequest = api.listDetails().cache();
        }

        return detailsRequest;
    }
}
