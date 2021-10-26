package com.example.ool_mobile.ui.form.equipment_details;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.form.FormMode;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

class UpdateDetailsViewModel extends CommonDetailsViewModel {

    private final int initialId;

    public UpdateDetailsViewModel(@NonNull EquipmentApi api, int initialId) {
        super(api);
        this.initialId = initialId;
    }

    @NonNull
    @Override
    public Observable<Event> getEvents() {
        return events;
    }

    @NonNull
    @Override
    public FormMode getFormMode() {
        return FormMode.Update;
    }

    private MutableLiveData<EquipmentDetailsInput> input;

    @Override
    public LiveData<EquipmentDetailsInput> getInput() {

        if (input == null) {
            input = new MutableLiveData<>();

            subscriptions.add(
                    api.getDetailsById(initialId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .zipWith(
                                    fetchKinds(),
                                    EquipmentDetailsInput::new
                            )
                            .subscribe(this.input::setValue)
            );
        }

        return input;
    }

    @Override
    public void onSave() {

    }
}
