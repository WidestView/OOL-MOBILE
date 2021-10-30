package com.example.ool_mobile.ui.form.photoshoot;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.util.form.FormMode;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

class AddPhotoshootViewModel extends PhotoshootViewModel {

    private final MutableLiveData<PhotoshootInput> input = new MutableLiveData<>(
            new PhotoshootInput()
    );

    private final Subject<Event> events = PublishSubject.create();

    private final PhotoshootValidation validation = new PhotoshootValidation(events);

    private final PhotoshootApi photoshootApi;

    public AddPhotoshootViewModel(
            @NonNull PhotoshootApi photoshootApi
    ) {
        this.photoshootApi = photoshootApi;
    }

    @NonNull
    @Override
    public LiveData<PhotoshootInput> getInput() {
        return input;
    }

    @NonNull
    @Override
    public Observable<Event> getEvents() {
        return events;
    }

    @Override
    public void savePhotoshoot() {

        subscriptions.add(
                validation.validate(input.getValue())
                        .flatMapSingle(
                                photoshootApi::addPhotoshoot
                        )
                        .subscribe(
                                success -> events.onNext(Event.Success),
                                this::handleError
                        )
        );
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        events.onNext(Event.Error);
    }

    @NonNull
    @Override
    public LiveData<FormMode> getFormMode() {
        return new MutableLiveData<>(FormMode.Add);
    }
}
