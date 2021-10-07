package com.example.ool_mobile.ui.form.add_photo_shoot;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.util.ViewModelFactory;
import com.example.ool_mobile.ui.util.form.FormMode;

import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

class UpdatePhotoshootViewModel extends PhotoshootViewModel {

    private MutableLiveData<Photoshoot> photoshootInfo;

    private final Subject<Event> events = PublishSubject.create();

    @NonNull
    private final PhotoshootApi photoshootApi;

    @NonNull
    private final UUID resourceId;

    protected UpdatePhotoshootViewModel(@NonNull PhotoshootApi photoshootApi, @NonNull UUID resourceId) {

        Objects.requireNonNull(photoshootApi, "photoshootApi is null");
        Objects.requireNonNull(resourceId, "resourceId is null");

        this.photoshootApi = photoshootApi;
        this.resourceId = resourceId;
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull PhotoshootApi api, @NonNull UUID resourceId) {
        return ViewModelFactory.create(
                UpdatePhotoshootViewModel.class,
                () -> new UpdatePhotoshootViewModel(api, resourceId)
        );
    }

    @NonNull
    @Override
    public LiveData<Photoshoot> getInitialPhotoshoot() {

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

    @NonNull
    @Override
    public Observable<Event> getEvents() {
        return events;
    }

    @Override
    public void savePhotoshoot(@NonNull PhotoshootInput input) {
        // todo: implement this

        if (Math.random() < 0.5) {
            events.onNext(Event.Error);
        } else {
            events.onNext(Event.Success);
        }
    }

    @NonNull
    @Override
    public LiveData<FormMode> getFormMode() {
        return new MutableLiveData<>(FormMode.Update);
    }
}
