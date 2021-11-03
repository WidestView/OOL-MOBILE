package com.example.ool_mobile.ui.form.equipment_details;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.service.ModelUrls;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.form.FormMode;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

class UpdateDetailsViewModel extends CommonDetailsViewModel {

    private final int initialId;

    private MutableLiveData<Uri> imageUrl;

    private MutableLiveData<EquipmentDetailsInput> input;

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


    @Override
    public LiveData<EquipmentDetailsInput> getInput() {

        if (input == null) {
            input = new MutableLiveData<>();

            loading.setValue(true);

            subscriptions.add(
                    api.getDetailsById(initialId)
                            .zipWith(
                                    fetchKinds(),
                                    EquipmentDetailsInput::new
                            )
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally(() -> {
                                loading.setValue(false);
                            })
                            .subscribe(value -> {
                                this.input.setValue(value);
                            }, this::handleError)
            );
        }

        return input;
    }


    @NonNull
    @Override
    public LiveData<Uri> getImageUrl() {

        if (imageUrl == null) {
            imageUrl = new MutableLiveData<>();
            imageUrl.setValue(new ModelUrls().imageUrlFromDetailsId(initialId));
        }
        return imageUrl;
    }

    @Nullable
    @Override
    public Integer getInitialId() {
        return initialId;
    }

    @Override
    public void onSave() {

        if (input.getValue() == null) {
            return;
        }

        loading.setValue(true);

        fetchKinds()
                .flatMapMaybe(kinds -> validation.validate(input.getValue(), kinds))
                .flatMapSingle(details ->
                        api.updateDetails(initialId, details).toSingleDefault(true))
                .flatMapSingle(success -> uploadBitmap(initialId).toSingleDefault(true))
                .switchIfEmpty(Single.just(false))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    loading.setValue(false);
                })
                .to(disposedWhenCleared())
                .subscribe(success -> {

                    if (success) {
                        events.onNext(Event.Success);
                    }

                }, this::handleError);
    }
}
