package com.example.ool_mobile.ui.form.equipment_details;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.form.FormMode;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;

class AddDetailsViewModel extends CommonDetailsViewModel {

    private final MutableLiveData<EquipmentDetailsInput> input = new MutableLiveData<>(
            new EquipmentDetailsInput()
    );

    public AddDetailsViewModel(@NonNull EquipmentApi api) {
        super(api);
    }

    @NonNull
    public FormMode getFormMode() {
        return FormMode.Add;
    }

    @NonNull
    public LiveData<EquipmentDetailsInput> getInput() {
        return input;
    }

    @NonNull
    @Override
    public LiveData<Uri> getImageUrl() {
        return new MutableLiveData<>();
    }

    @Nullable
    @Override
    public Integer getInitialId() {
        return null;
    }

    @Override
    public void onSave() {

        if (input.getValue() == null) {
            return;
        }

        fetchKinds()
                .flatMapMaybe(kinds -> validation.validate(input.getValue(), kinds))
                .flatMapSingle(api::addDetails)
                .map(EquipmentDetails::getId)
                .flatMapSingle(id -> uploadBitmap(id).toSingleDefault(true))
                .switchIfEmpty(Single.just(false))
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe(success -> {
                    if (success) {
                        events.onNext(Event.Success);
                    }
                }, this::handleError);
    }

}
