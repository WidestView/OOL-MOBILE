package com.example.ool_mobile.ui.form.add_photo_shoot;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.api.PhotoshootApi;

import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class UpdatePhotoshootViewModel extends PhotoshootFormViewModel {

    private MutableLiveData<Photoshoot> photoshootInfo;

    @NonNull
    private PhotoshootApi photoshootApi;

    @NonNull
    private UUID resourceId;

    protected UpdatePhotoshootViewModel(@NonNull PhotoshootApi photoshootApi, @NonNull UUID resourceId) {

        Objects.requireNonNull(photoshootApi, "photoshootApi is null");
        Objects.requireNonNull(resourceId, "resourceId is null");

        this.photoshootApi = photoshootApi;
        this.resourceId = resourceId;
    }

    @NonNull
    public LiveData<Photoshoot> getPhotoshootInfo() {

        if (photoshootInfo == null) {
            photoshootInfo = new MutableLiveData<>();

            subscriptions.add(
                    photoshootApi.getPhotoshootWithId(resourceId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(photoshoot ->
                                    photoshootInfo.setValue(photoshoot)
                            )
            );
        }

        return photoshootInfo;
    }

    @Override
    protected void savePhotoshoot(@NonNull Photoshoot photoshoot) {

        // todo: implement this
    }
}
