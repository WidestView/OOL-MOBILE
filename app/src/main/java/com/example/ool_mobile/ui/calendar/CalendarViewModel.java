package com.example.ool_mobile.ui.calendar;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.util.ViewModelFactory;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class CalendarViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @NonNull
    private final PhotoshootApi photoshootApi;
    private MutableLiveData<List<Photoshoot>> photoshootList;

    private CalendarViewModel(@NonNull PhotoshootApi photoshootApi) {
        this.photoshootApi = photoshootApi;
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull PhotoshootApi photoshootApi) {
        return ViewModelFactory.create(
                CalendarViewModel.class,
                () -> new CalendarViewModel(photoshootApi)
        );
    }

    @NonNull
    public LiveData<List<Photoshoot>> getPhotoshootList() {

        if (photoshootList == null) {
            photoshootList = new MutableLiveData<>();

            compositeDisposable.add(
                    photoshootApi.listFromCurrentEmployee()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(photoshoots -> photoshootList.setValue(photoshoots))
            );
        }

        return photoshootList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
