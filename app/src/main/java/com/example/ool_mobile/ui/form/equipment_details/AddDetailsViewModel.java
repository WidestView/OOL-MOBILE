package com.example.ool_mobile.ui.form.equipment_details;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.form.FormMode;

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

    @Override
    public void onSave() {

        subscriptions.add(
                fetchKinds().flatMapMaybe(
                        kinds -> validation.validate(input.getValue(), kinds)
                                .flatMapSingle(api::addDetails)
                ).subscribe(result -> {
                    events.onNext(Event.Success);
                })
        );
    }


}
