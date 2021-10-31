package com.example.ool_mobile.ui.form.equipment_withdraw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.ui.util.form.FormMode;

import java.util.Objects;

public class AddWithdrawViewModel extends CommonWithdrawViewModel {

    private final MutableLiveData<WithdrawInput> input = new MutableLiveData<>(new WithdrawInput());

    protected AddWithdrawViewModel(@NonNull ApiProvider provider) {
        super(provider);
    }

    @Nullable
    @Override
    public Integer getInitialId() {
        return null;
    }

    @NonNull
    @Override
    public FormMode getFormMode() {
        return FormMode.Add;
    }

    @NonNull
    @Override
    public LiveData<WithdrawInput> getInput() {
        return input;
    }

    @NonNull
    @Override
    public LiveData<Boolean> getCanBeFinished() {
        return new MutableLiveData<>(false);
    }

    @Override
    public void finishWithdraw() {

    }

    @Override
    public void saveInput() {

        Objects.requireNonNull(input.getValue());

        fetchListFields()
                .flatMapMaybe(fields ->
                        validation.validate(input.getValue(), fields)
                )
                .flatMapSingle(result -> withdrawApi.addWithdraw(result).toSingleDefault(true))
                .to(disposedWhenCleared())
                .subscribe(success -> {
                    events.onNext(Event.Success);
                });

    }
}
