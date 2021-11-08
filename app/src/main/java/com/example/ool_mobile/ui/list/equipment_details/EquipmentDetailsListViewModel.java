package com.example.ool_mobile.ui.list.equipment_details;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.ErrorEvent;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.view_model.ViewModelFactory;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class EquipmentDetailsListViewModel extends SubscriptionViewModel {

    @NonNull
    private final MutableLiveData<List<EquipmentDetails>> details = new MutableLiveData<>();

    @NonNull
    private final EquipmentApi api;

    private final Subject<ErrorEvent> events = PublishSubject.create();

    public EquipmentDetailsListViewModel(@NonNull EquipmentApi api) {
        this.api = api;
    }

    @NonNull
    public LiveData<List<EquipmentDetails>> getDetails() {

        subscriptions.add(
                api.listDetails()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this.details::setValue, this::handleError)
        );

        return details;
    }

    public void archiveDetails(@NonNull EquipmentDetails details) {
        Objects.requireNonNull(details);

        api.archiveDetails(details.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::handleError)
                .to(disposedWhenCleared())
                .subscribe(this::getDetails, this::handleError);
    }

    public Observable<ErrorEvent> getEvents() {
        return events;
    }

    private void handleError(Throwable error) {

        Timber.e(error);

        events.onNext(ErrorEvent.Error);
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
