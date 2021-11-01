package com.example.ool_mobile.ui.list.equipment_withdraw;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.EquipmentWithdraw;
import com.example.ool_mobile.service.api.EquipmentWithdrawApi;
import com.example.ool_mobile.ui.util.ErrorEvent;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.view_model.ViewModelFactory;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class WithdrawListViewModel extends SubscriptionViewModel {

    @NonNull
    private final EquipmentWithdrawApi api;

    private final MutableLiveData<List<EquipmentWithdraw>> withdraws = new MutableLiveData<>();


    private final Subject<ErrorEvent> events = PublishSubject.create();

    public WithdrawListViewModel(@NonNull EquipmentWithdrawApi api) {
        this.api = api;
    }

    public Observable<ErrorEvent> getEvents() {
        return events;
    }

    public LiveData<List<EquipmentWithdraw>> fetchWithdraws() {

        api.listWithdraws()
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe(withdraws::setValue, this::handleError);

        return withdraws;
    }

    private void handleError(Throwable error) {
        error.printStackTrace();

        events.onNext(ErrorEvent.Error);
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull EquipmentWithdrawApi api) {

        Objects.requireNonNull(api);

        return ViewModelFactory.create(
                WithdrawListViewModel.class,
                () -> new WithdrawListViewModel(api)
        );
    }


}
