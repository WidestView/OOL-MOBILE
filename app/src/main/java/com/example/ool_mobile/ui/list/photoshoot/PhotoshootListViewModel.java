package com.example.ool_mobile.ui.list.photoshoot;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.util.view_model.ViewModelFactory;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PhotoshootListViewModel extends ViewModel {

    @NonNull
    private final PhotoshootApi photoshootApi;
    @NonNull
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<List<Photoshoot>> photoshootList = new MutableLiveData<>();

    public PhotoshootListViewModel(@NonNull PhotoshootApi photoshootApi) {
        this.photoshootApi = photoshootApi;
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull PhotoshootApi photoshootApi) {

        Objects.requireNonNull(photoshootApi, "photoshootApi is null");

        return ViewModelFactory.create(
                PhotoshootListViewModel.class,
                () -> new PhotoshootListViewModel(photoshootApi)
        );
    }

    @NonNull
    public LiveData<List<Photoshoot>> getCurrentPhotoshootList() {

        compositeDisposable.add(
                photoshootApi.listAll()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(photoshootList::setValue)
        );

        return photoshootList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
