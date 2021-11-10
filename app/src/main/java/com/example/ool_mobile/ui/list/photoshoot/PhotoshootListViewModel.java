package com.example.ool_mobile.ui.list.photoshoot;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.util.ErrorEvent;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.view_model.ViewModelFactory;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import timber.log.Timber;

public class PhotoshootListViewModel extends SubscriptionViewModel {

    @NonNull
    private final PhotoshootApi photoshootApi;

    private final MutableLiveData<List<Photoshoot>> photoshootList = new MutableLiveData<>();

    private final Subject<ErrorEvent> events = PublishSubject.create();

    public PhotoshootListViewModel(@NonNull PhotoshootApi photoshootApi) {
        this.photoshootApi = photoshootApi;
    }

    @NonNull
    public LiveData<List<Photoshoot>> getCurrentPhotoshootList() {

        photoshootApi.listAll()
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe(photoshootList::setValue, this::handleError);

        return photoshootList;
    }

    public Observable<ErrorEvent> getEvents() {
        return events;
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable);
        events.onNext(ErrorEvent.Error);
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull PhotoshootApi photoshootApi) {

        Objects.requireNonNull(photoshootApi, "photoshootApi is null");

        return ViewModelFactory.create(
                PhotoshootListViewModel.class,
                () -> new PhotoshootListViewModel(photoshootApi)
        );
    }

}
