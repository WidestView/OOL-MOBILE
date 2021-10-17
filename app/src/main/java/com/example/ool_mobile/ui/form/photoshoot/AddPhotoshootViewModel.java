package com.example.ool_mobile.ui.form.photoshoot;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.util.form.FormMode;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

class AddPhotoshootViewModel extends PhotoshootViewModel {

    private final PhotoshootApi photoshootApi;

    private final Subject<Event> events = PublishSubject.create();

    private final PhotoshootValidation validation = new PhotoshootValidation(events);

    public AddPhotoshootViewModel(
            @NonNull PhotoshootApi photoshootApi
    ) {
        this.photoshootApi = photoshootApi;
    }

    @NonNull
    @Override
    public LiveData<Photoshoot> getInitialPhotoshoot() {
        return new MutableLiveData<>();
    }

    @NonNull
    @Override
    public Observable<Event> getEvents() {
        return events;
    }

    @Override
    public void savePhotoshoot(@NonNull PhotoshootInput input) {

        Photoshoot photoshoot = validation.validate(input);

        if (photoshoot == null) {
            return;
        }

        subscriptions.add(
                photoshootApi.addPhotoshoot(photoshoot)
                        .subscribe(
                                success -> events.onNext(Event.Success),
                                error -> {
                                    error.printStackTrace();
                                    events.onNext(Event.Error);
                                }
                        )
        );
    }

    @NonNull
    @Override
    public LiveData<FormMode> getFormMode() {
        return new MutableLiveData<>(FormMode.Add);
    }
}
