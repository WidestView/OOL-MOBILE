package com.example.ool_mobile.ui.calendar;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.service.util.ErrorEvent;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.view_model.ViewModelFactory;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class CalendarViewModel extends SubscriptionViewModel {

    private MutableLiveData<List<Photoshoot>> photoshootList;

    private final Subject<ErrorEvent> events = PublishSubject.create();

    @NonNull
    private final PhotoshootApi photoshootApi;

    private CalendarViewModel(@NonNull PhotoshootApi photoshootApi) {
        this.photoshootApi = photoshootApi;
    }

    public Observable<ErrorEvent> getEvents() {
        return events;
    }


    @NonNull
    public LiveData<List<Photoshoot>> getPhotoshootList() {

        if (photoshootList == null) {
            photoshootList = new MutableLiveData<>();

            subscriptions.add(
                    photoshootApi.listFromCurrentEmployee()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    photoshoots -> photoshootList.setValue(photoshoots),
                                    this::handleError
                            )
            );
        }

        return photoshootList;
    }

    private void handleError(Throwable error) {
        error.printStackTrace();
        events.onNext(ErrorEvent.Error);
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull PhotoshootApi photoshootApi) {
        return ViewModelFactory.create(
                CalendarViewModel.class,
                () -> new CalendarViewModel(photoshootApi)
        );
    }
}
