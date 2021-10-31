package com.example.ool_mobile.ui.form.equipment_withdraw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.model.EquipmentWithdraw;
import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.ui.util.form.FormMode;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;

class UpdateWithdrawViewModel extends CommonWithdrawViewModel {

    private MutableLiveData<WithdrawInput> input;

    private final int initialId;

    UpdateWithdrawViewModel(@NonNull ApiProvider provider, int initialId) {
        super(provider);

        this.initialId = initialId;
    }

    @Nullable
    @Override
    public Integer getInitialId() {
        return initialId;
    }

    @NonNull
    @Override
    public FormMode getFormMode() {
        return FormMode.Update;
    }

    @NonNull
    @Override
    public LiveData<WithdrawInput> getInput() {

        if (input == null) {
            input = new MutableLiveData<>();

            fetchWithdraw()
                    .zipWith(fetchListFields(), WithdrawInput::new)
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe(input::setValue);
        }

        return input;
    }

    private MutableLiveData<Boolean> canBeFinished;

    @NonNull
    @Override
    public LiveData<Boolean> getCanBeFinished() {

        if (canBeFinished == null) {
            canBeFinished = new MutableLiveData<>(false);

            fetchWithdraw()
                    .map(withdraw -> withdraw.getEffectiveDevolutionDate() == null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe(canBeFinished::setValue);
        }

        return canBeFinished;
    }

    private Single<EquipmentWithdraw> initialWithdraw;

    private Single<EquipmentWithdraw> fetchWithdraw() {

        if (initialWithdraw == null) {
            initialWithdraw = withdrawApi.getById(initialId).cache();
        }

        return initialWithdraw;
    }

    @Override
    public void finishWithdraw() {

        withdrawApi
                .finishWithdraw(initialId)
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe(() -> {
                    events.onNext(Event.WithdrawFinished);
                    canBeFinished.setValue(false);
                });

    }

    @Override
    public void saveInput() {

        if (input.getValue() == null || getLists().getValue() == null) {
            return;
        }

        validation.validate(input.getValue(), getLists().getValue())
                .flatMapSingle(result -> withdrawApi.updateWithdraw(initialId, result))
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe(success -> {
                    events.onNext(Event.Success);
                });

    }

}
