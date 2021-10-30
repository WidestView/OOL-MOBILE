package com.example.ool_mobile.ui.form.equipment_withdraw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.ui.util.form.FormMode;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

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

            withdrawApi.getById(initialId)
                    .zipWith(fetchListFields(), WithdrawInput::new)
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe(input::setValue);
        }

        return input;
    }

    @Override
    public void saveInput() {

    }
}
