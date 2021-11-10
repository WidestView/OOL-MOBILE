package com.example.ool_mobile.ui.form.equipment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import timber.log.Timber;

import static io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread;

public class AddEquipmentViewModel extends EquipmentFormViewModel {

    private final Subject<Event> events = PublishSubject.create();

    private final EquipmentApi api;

    private final EquipmentValidation validation;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private final MutableLiveData<EquipmentInput> input = new MutableLiveData<>(
            new EquipmentInput()
    );

    private MutableLiveData<List<EquipmentDetails>> details;

    private Single<List<EquipmentDetails>> detailsRequest;

    public AddEquipmentViewModel(@NonNull EquipmentApi api) {
        Objects.requireNonNull(api, "api is null");

        this.api = api;

        this.validation = new EquipmentValidation(events);
    }

    @NonNull
    @Override
    public LiveData<EquipmentInput> getInput() {
        return input;
    }

    @NonNull
    @Override
    public LiveData<FormMode> getFormMode() {
        return new MutableLiveData<>(FormMode.Add);
    }

    @Override
    public void saveEquipment() {

        Objects.requireNonNull(input.getValue());

        loading.setValue(true);

        fetchDetails()
                .flatMapMaybe(details ->
                        validation.validate(input.getValue(), details).flatMap(equipment ->
                                api.addEquipment(equipment).toMaybe()
                        )
                )
                .observeOn(mainThread())
                .doFinally(() -> {
                    loading.setValue(false);
                })
                .to(disposedWhenCleared())
                .subscribe(success -> {
                    events.onNext(Event.Success);
                }, this::handleError);
    }

    private Single<List<EquipmentDetails>> fetchDetails() {
        if (detailsRequest == null) {
            detailsRequest = api.listDetails().cache();
        }

        return detailsRequest;
    }

    @NonNull
    @Override
    public Observable<Event> getEvents() {
        return events;
    }

    @NonNull
    @Override
    public LiveData<List<EquipmentDetails>> getDetailsList() {

        if (details == null) {

            details = new MutableLiveData<>();

            loading.setValue(true);

            fetchDetails()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {
                        loading.setValue(false);
                    })
                    .to(disposedWhenCleared())
                    .subscribe(value -> {
                        details.setValue(value);
                    }, this::handleError);
        }

        return details;
    }

    @Nullable
    @Override
    public Integer getInitialId() {
        return null;
    }

    @Override
    public LiveData<Boolean> isLoading() {
        return loading;
    }

    private void handleError(Throwable error) {

        Timber.e(error);

        events.onNext(Event.Error);
    }
}
